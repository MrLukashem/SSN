package base;

import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import train.TrainingInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrlukashem on 21.05.16.
 */
public class SSN {
    protected List<INeuron> mInputsNeurons;
    protected List<INeuron> mHiddenNeurons;
    protected List<INeuron> mOutputsNeurons;

    protected double LEARNING_CONSTANT = .1;

    private double getRandomWeight() {
        return Math.random() * 4.0 - 2.0;
    }

    private void initConnections(List<INeuron> from, List<INeuron> to) {
        for (INeuron toNeuron : to) {
            for (INeuron fromNeuron : from) {
                if (!toNeuron.hasThreshold()) {
                    Connection connection = new Connection(fromNeuron, toNeuron, getRandomWeight());
                    toNeuron.addConnection(connection);
                }
            }
        }
    }

    private void init(int nInput, int nHidden, int nOutput) {
        mInputsNeurons = new ArrayList<>(nInput);
        mHiddenNeurons = new ArrayList<>(nHidden);
        mOutputsNeurons = new ArrayList<>(nOutput);

        for (int i = 0; i < nInput; i++) {
            mInputsNeurons.add(new InputNeuron());
        }
        mInputsNeurons.get(mInputsNeurons.size() - 1)
                .setThreshold(.0);

        for (int i = 0; i < nHidden; i++) {
            HiddenNeuron neuron = new HiddenNeuron();
            neuron.setActivationFunction(x -> 1.0 / (1.0 + Math.exp(-x)));
            mHiddenNeurons.add(neuron);
        }
        mHiddenNeurons.get(mHiddenNeurons.size() - 1)
                .setThreshold(.0);

        for (int i = 0; i < nOutput; i++) {
            OutputNeuron neuron = new OutputNeuron();
            neuron.setActivationFunction(x -> 1.0 / (1.0 + Math.exp(-x)));
            mOutputsNeurons.add(neuron);
        }

        initConnections(mInputsNeurons, mHiddenNeurons);
        //initConnections(mHiddenNeurons, mInputsNeurons, iw);
        for (INeuron hidden : mHiddenNeurons) {
            for (INeuron output : mOutputsNeurons) {
                Connection connection = new Connection(hidden, output, getRandomWeight());
                hidden.addConnection(connection);
            }
        }
        for (INeuron hidden : mHiddenNeurons) {
            for (INeuron input : mInputsNeurons) {
                Connection connection = new Connection(input, hidden, getRandomWeight());
                hidden.addConnection(connection);
            }
        }
        initConnections(mHiddenNeurons, mOutputsNeurons);
    }

    public SSN(int nInput, int nHidden, int nOutput) {
        init(nInput, nHidden, nOutput);
    }

    public void setLearningConstant(double newValue) {
        LEARNING_CONSTANT = newValue;
    }

    public List<Double> pushInput(@NotNull List<Double> input) {
        if (input.size() + 1 /* + bias */< mInputsNeurons.size()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < mInputsNeurons.size() - 1; i++) {
            mInputsNeurons.get(i).input(input.get(i));
        }

        mHiddenNeurons.stream().forEach(neuron -> neuron.compute());
        mOutputsNeurons.stream().forEach(neuron -> neuron.compute());

        List<Double> result = new ArrayList<>();
        mOutputsNeurons.stream().forEach(neuron -> result.add(neuron.getOutput()));
        return result;
    }

    public void trainMe(List<TrainingInput<Double> > trainingInputs) {
            for (int i = 0; i < 1000; i++) {
                int randomIndex = (int)Math.abs(Math.random() * (double)(trainingInputs.size() - 1));
                TrainingInput<Double> ti = trainingInputs.get(randomIndex);
                List<Double> ssnAnswers = pushInput(ti.getInputs());

                // for every output neuron back propagation.
                int outputNeuronNumber = 1;
                for (Double ssnAnswer : ssnAnswers) {
                    double expectedOutput = .0;
                    if (ti.getExpectedOutput() == outputNeuronNumber) {
                        expectedOutput = 1.0;
                    }

                    // f'(x) * (1 - f'(x)) * e - y
                    double factor = (1 - ssnAnswer) * ssnAnswer;
                    double delta = factor * (expectedOutput - ssnAnswer);

                    INeuron outputNeuron = mOutputsNeurons.get(outputNeuronNumber - 1);
                    List<Connection> connections = outputNeuron.getConnections();
                    for (Connection connection : connections) {
                        double output = connection.getFrom().getOutput();
                        double dWeight = ( output * delta * LEARNING_CONSTANT);
                        connection.updateWeight(dWeight);
                    }

                    for (INeuron neuron : mHiddenNeurons) {
                        connections = neuron.getConnections();
                        double sum = .0;

                        for (Connection connection : connections) {
                            if (connection.getFrom() == neuron && connection.getTo() == outputNeuron) {
                                sum += connection.getWeight() * delta;
                            }
                        }

                        for (Connection connection : connections) {
                            if (connection.getTo() == neuron) {
                                double answerHidden = neuron.getOutput();
                                double deltaHidden = sum * answerHidden * (1 - answerHidden);

                                INeuron from = connection.getFrom();
                                double dWeight = from.getOutput() * deltaHidden * LEARNING_CONSTANT;
                                connection.updateWeight(dWeight);
                            }
                        }
                    }
                    // switch to next output neuron.
                    outputNeuronNumber++;
                }
            }
    }

    public void showSSNOnConsole() {
        for(INeuron hNeuron : mHiddenNeurons) {
            hNeuron.getConnections().stream().forEach(x -> {
                if (x.getTo() == hNeuron) {
                    System.out.print(" " + x.getWeight());
                }
            });
            System.out.println("");
        }

        System.out.println("");
        System.out.println("");
        System.out.println("");

        for(INeuron oNeuron : mOutputsNeurons) {
            oNeuron.getConnections().stream().forEach(x -> {
                if (x.getTo() == oNeuron) {
                    System.out.print(" " + x.getWeight());
                }
            });
            System.out.println("");
        }
    }
}
