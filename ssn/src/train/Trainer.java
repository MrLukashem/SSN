package train;

import base.Neuron;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mrlukashem on 22.05.16.
 */
public class Trainer {
 /*   private Trainer() {
        mError = (output, eoutput) -> output - eoutput;
    }

    private static Trainer mMe = new Trainer();

    private List<TrainingSet<Float> > mSetsList =
            new ArrayList<>();

    private Error mError;

    private float LEARNING_CONSTANT = 0.1f;

    private List<Float> calculateDeltas(@NotNull List<Float> inputs, float c, int error) {
        List<Float> deltas = new LinkedList<>();

        inputs.stream().forEach(input -> deltas.add(c * error * input));
        return deltas;
    }

    private void triggerTraining(@NotNull List<TrainingSet<Float> > trainingSetList, @NotNull Neuron neuron) {
        neuron.setActivationFunction(exc -> exc >= 0 ? 1 : 0);

        for (TrainingSet<Float> set : trainingSetList) {
            for (TrainingInput<Float> input : set.getInput()) {
                neuron.setInput(input.getInputs());
                int error = mError.calculate(neuron.compute(), input.getExpectedOutput());

                neuron.updateWeights(calculateDeltas(input.getInputs(), LEARNING_CONSTANT, error));
            }
        }
    }

    private void trainCore(@NotNull Neuron neuron, boolean singleTrainSetMode) throws IllegalArgumentException {
        if (mSetsList.isEmpty()) {
            // TODO: meybe other exception?
            throw new IllegalArgumentException();
        }

        if (singleTrainSetMode) {
            triggerTraining(mSetsList.subList(mSetsList.size() - 2, mSetsList.size() - 1), neuron);
        } else {
            triggerTraining(mSetsList, neuron);
        }
    }

    public static Trainer getTrainer() {
        return mMe;
    }

    public void addTrainingSet(@NotNull TrainingSet<Float> set) {
        mSetsList.add(set);
    }

    public void train(@NotNull TrainingSet<Float> set, @NotNull Neuron neuron) {
        mSetsList.add(set);
        trainCore(neuron, true);
        mSetsList.remove(mSetsList.size() - 1);
    }

    public void train(@NotNull Neuron neuron) {
        trainCore(neuron, false);
    }

    public void setErrorFunction(@NotNull Error errorFunction) {
        mError = errorFunction;
    }

    public void setLearningConstant(float learningConstant) {
        LEARNING_CONSTANT = learningConstant;
    }

    public float getLearningConstant() {
        return LEARNING_CONSTANT;
    }

    public interface Error {
        int calculate(int output, int expectedOutput);
    } */
}