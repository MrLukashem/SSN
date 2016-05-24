package base;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by mrlukashem on 21.05.16.
 */
public class Neuron implements INeuron {
    protected List<Connection> mConnections;
    protected float mThreshold;
    protected boolean mIsThreshold = false;

    protected ActivationFunction mAF;
    protected float mOutput;

    private void init() {
        // more custom operations in future.
        mConnections = new ArrayList<>();
        mOutput = .0f;
        // default activation function.
        mAF = (x -> x >= 0 ? 1 : 0);
    }

    public interface ActivationFunction {
        float call(float exc);
    }

    public Neuron() {
        init();
    }

    public Neuron(float threshold) {
        setThreshold(threshold);
        init();
    }

    public void addConnection(Connection connection) throws IllegalArgumentException {
        if (connection.getTo() != this) {
            throw new IllegalArgumentException();
        }

        mConnections.add(connection);
    }

    public List<Connection> getConnections() {
        return mConnections;
    }

    public void setThreshold(float threshold) {
        mIsThreshold = true;
        mThreshold = threshold;
    }

    public void removeThreshold() {
        mIsThreshold = false;
    }

    public float getOutput() {
        return mOutput;
    }

    public void input(float input) {
        mOutput = input;
        if (mIsThreshold) {
            mOutput += mThreshold;
        }
    }

    public void setActivationFunction(@NotNull ActivationFunction af) {
        mAF = af;
    }

    protected float f(float exc) {
        return mAF.call(exc);
    }

    public float compute() {
        float exc = .0f;
        for (Connection connection : mConnections) {
            float out = connection.getFrom().getOutput();
            exc += out * connection.getWeight();
        }

        if (mIsThreshold) {
            exc += mThreshold;
        }

        return (mOutput = f(exc));
    }
}
