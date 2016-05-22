package base;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Created by mrlukashem on 21.05.16.
 */
public class Neuron {
    private List<Float> mInputs;
    private List<Float> mWeights;

    private int mSize = 0;
    private float mThreshold;

    private ActivationFunction mAF;

    private void init(int size) {
        mSize = size;
        mThreshold = Float.NaN;
        // more custom operations in future.
    }

    public interface ActivationFunction {
        int call(float exc);
    }

    public Neuron(int nSize) throws IllegalArgumentException {
        if (nSize <= 0) {
            throw new IllegalArgumentException();
        }

        init(nSize);
    }

    public void setInput(@NotNull List<Float> inputs) {
        if (inputs.size() < mSize) {
            throw new IllegalArgumentException();
        }

        mInputs = inputs.stream().collect(Collectors.toList());
    }

    public void setWeights(@NotNull List<Float> weights) {
        if (weights.size() < mSize) {
            throw new IllegalArgumentException();
        }

        mWeights = weights.stream().collect(Collectors.toList());
    }

    public boolean updateWeights(@NotNull List<Float> update) {
        if (update.size() < mWeights.size()) {
            return false;
        }

        for (int i = 0; i < mWeights.size(); i++) {
            mWeights.set(i, mWeights.get(i) + update.get(i));
        }

        return true;
    }

    public void setThreshold(float threshold) {
        mThreshold = threshold;
        mWeights.add(0, mThreshold);
        mInputs.add(0, 1.0f);
    }

    public void setActivationFunction(@NotNull ActivationFunction af) {
        mAF = af;
    }

    protected int f(float exc) {
        return mAF.call(exc);
    }

    public int compute() {
        if (mWeights.isEmpty() || mWeights.isEmpty() || mThreshold == Float.NaN) {
            throw new IllegalArgumentException();
        }

        float exc = .0f;
        for(Float xi : mInputs) {
            for(Float wi : mWeights) {
                exc += xi * wi;
            }
        }

        return f(exc);
    }
}
