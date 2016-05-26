package train;

import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mrlukashem on 22.05.16.
 */
public class TrainingInput <E> {
    private List<E> mInputs;

    // at the moment, it uses as number of expected class/label class.
    private int mExpectedOutput = 0;

    private void checkInput(List<E> inputs) throws IllegalArgumentException {
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public TrainingInput(@NotNull List<E> inputs, int expectedOut) throws IllegalArgumentException {
        checkInput(inputs);
        mExpectedOutput = expectedOut;
        mInputs = inputs.stream().collect(Collectors.toList());
    }

    public void setInputs(@NotNull List<E> inputs) {
        checkInput(inputs);
        mInputs = inputs.stream().collect(Collectors.toList());
    }

    public void setExpectedOut(int expectedOut) {
        mExpectedOutput = expectedOut;
    }

    public List<E> getInputs() {
        return mInputs;
    }

    public int getExpectedOutput() {
        return mExpectedOutput;
    }
}
