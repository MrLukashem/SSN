package base;

import com.sun.istack.internal.NotNull;
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

    protected float LEARNING_CONSTANT = 0.1f;

    private void initConnections(List<INeuron> from, List<INeuron> to, float w) {
        for (INeuron toNeuron : to) {
            for (INeuron fromNeuron : from) {
                Connection connection = new Connection(fromNeuron, toNeuron, w);
                toNeuron.addConnection(connection);
            }
        }
    }

    private void init(int nInput, int nHidden, int nOutput, float iw) {
        mInputsNeurons = new ArrayList<>(nInput);
        mHiddenNeurons = new ArrayList<>(nHidden);
        mOutputsNeurons = new ArrayList<>(nOutput);

        for (int i = 0; i < nInput; i++) {
            mInputsNeurons.add(new InputNeuron());
        }

        for (int i = 0; i < nHidden; i++) {
            mHiddenNeurons.add(new HiddenNeuron());
        }

        for (int i = 0; i < nOutput; i++) {
            mOutputsNeurons.add(new OutputNeuron());
        }

        initConnections(mInputsNeurons, mHiddenNeurons, iw);
        initConnections(mHiddenNeurons, mOutputsNeurons, iw);
    }

    public SSN(int nInput, int nHidden, int nOutput) {
        init(nInput, nHidden, nOutput, (float)Math.random());
    }

    public SSN(int nInput, int nHidden, int nOutput, float iw) {
        init(nInput, nHidden, nOutput, iw);
    }

    public void setLearningConstant(float newValue) {
        LEARNING_CONSTANT = newValue;
    }

    public float pushInput(@NotNull List<Float> input) {
        if (input.size() < mInputsNeurons.size()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < mInputsNeurons.size(); i++) {
            mInputsNeurons.get(i).input(input.get(i));
        }

        mHiddenNeurons.stream().forEach(neuron -> neuron.compute());
        mOutputsNeurons.stream().forEach(neuron -> neuron.compute());

        float result = .0f;
        for (INeuron outputNeuron : mOutputsNeurons) {
            result += outputNeuron.getOutput();
        }

        return result;
    }

    public void trainMe(List<TrainingInput<Float> > trainingInputs) {
        for (TrainingInput<Float> ti : trainingInputs) {
            float ssnAnswer = pushInput(ti.getInputs());
            float delta = ssnAnswer * (1 - ssnAnswer) * (ti.getExpectedOutput() - ssnAnswer);

            List<Connection> connections = mOutputsNeurons.get(0).getConnections();
            for (Connection connection : connections) {
                float output = connection.getFrom().getOutput();
                float dWeight = output * delta;

                connection.updateWeight(dWeight * LEARNING_CONSTANT);
            }

            for (INeuron neuron : mHiddenNeurons) {
                connections = neuron.getConnections();
                float sum = 1.0f;

                /*
                for (Connection connection : connections) {
                    sum += connection.getWeight() * ssnAnswer;
                } */

                for (Connection connection : connections) {
                    float output = neuron.getOutput();
                    float dHidden = output * (1 - output);
                    dHidden *= sum;

                    INeuron from = connection.getFrom();
                    float dWeight = from.getOutput() * dHidden;
                    connection.updateWeight(dWeight);
                }
            }
        }
    }
}
