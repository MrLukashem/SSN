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

    protected double LEARNING_CONSTANT = .10;
    protected double mMomentumRate = .5;
    protected boolean mHasMomentum = false;

    private double getRandomWeight() {
        return Math.random() * 2.0 - 1.0;
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
                .setThreshold(Math.random() * 2 - 1);

        for (int i = 0; i < nHidden; i++) {
            HiddenNeuron neuron = new HiddenNeuron();
            neuron.setActivationFunction(x -> 1.0 / (1.0 + Math.exp(-x)));
            mHiddenNeurons.add(neuron);
        }
        mHiddenNeurons.get(mHiddenNeurons.size() - 1)
                .setThreshold(Math.random() * 2 - 1);

        for (int i = 0; i < nOutput; i++) {
            OutputNeuron neuron = new OutputNeuron();
            neuron.setActivationFunction(x -> 1.0 / (1.0 + Math.exp(-x)));
            mOutputsNeurons.add(neuron);
        }

        initConnections(mInputsNeurons, mHiddenNeurons);

        for (INeuron hidden : mHiddenNeurons) {
            for (INeuron input : mInputsNeurons) {
                Connection connection = new Connection(input, hidden, getRandomWeight());
                hidden.addConnection(connection);
            }
        }
        initConnections(mHiddenNeurons, mOutputsNeurons);
    }

    private void commitChanges() {
        for (INeuron oNeuron : mOutputsNeurons) {
            oNeuron.getConnections().forEach(Connection::commitUpdate);
        }
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

    public void trainMe(List<TrainingInput<Double> > trainingInputs, int nor) {
        mHasMomentum = false;
        TrainMeCore(trainingInputs, nor);
    }

    public void trainMeWithMomentum(List<TrainingInput<Double> > trainingInputs, int nor) {
        mHasMomentum = true;
        TrainMeCore(trainingInputs, nor);
    }

    private double getMomentum(Connection connection) {
        if (mHasMomentum) {
            return mMomentumRate * (connection.getWeight() - connection.getLastWeight());
        }

        // momentum mode is disabled.
        return .0;
    }

    private void TrainMeCore(List<TrainingInput<Double> > trainingInputs, int nor) {
        List<Connection> connections;

        for (int i = 0; i < nor; i++) {
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
                connections = outputNeuron.getConnections();
                for (Connection connection : connections) {
                    double output = connection.getFrom().getOutput();
                    double dWeight = (output * delta * LEARNING_CONSTANT);
                    connection.updateWeight(dWeight);
                    connection.setDelta(delta);
                }
                outputNeuronNumber++;
            }

            for (INeuron neuron : mHiddenNeurons) {
                connections = neuron.getConnections();
                double sum = .0;

                for (INeuron oNeuron : mOutputsNeurons) {
                    for (Connection oConnection : oNeuron.getConnections()) {
                        if (oConnection.getFrom() == neuron) {
                            sum += oConnection.getWeight() * oConnection.getDelta();
                        }
                    }
                }

                for (Connection connection : connections) {
                    if (connection.getTo() == neuron) {
                        double answerHidden = neuron.getOutput();
                        double deltaHidden = sum * answerHidden * (1 - answerHidden);

                        INeuron from = connection.getFrom();
                        double dWeight = from.getOutput() * deltaHidden * LEARNING_CONSTANT;
                        connection.updateWeight(dWeight + getMomentum(connection));
                        connection.commitUpdate();
                    }
                }
            }

            commitChanges();
        }
    }
}
