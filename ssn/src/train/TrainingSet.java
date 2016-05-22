package train;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mrlukashem on 22.05.16.
 */
public class TrainingSet <E> {
    private List<TrainingInput<E> > mInputs;

    public TrainingSet(List<TrainingInput<E> > inputs) throws IllegalArgumentException {
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException();
        }

        mInputs = inputs.stream().collect(Collectors.toList());
    }

    public List<TrainingInput<E> > getInput() {
        return mInputs;
    }
}
