package base;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrlukashem on 21.05.16.
 */
public class Neuron implements INeuron {
    protected List<Connection> mConnections;
    protected double mThreshold;
    protected boolean mIsThreshold = false;

    protected ActivationFunction mAF;
    protected double mOutput;

    private void init() {
        // more custom operations in future.
        mConnections = new ArrayList<>();
        mOutput = .0f;
        // default activation function.
        mAF = (x -> x >= 0.0f ? 1.0f : .0f);
    }

    public interface ActivationFunction {
        double call(double exc);
    }

    public Neuron() {
        init();
    }

    public Neuron(double threshold) {
        setThreshold(threshold);
        init();
    }

    public void addConnection(Connection connection) throws IllegalArgumentException {
    /*    if (connection.getTo() != this) {
            throw new IllegalArgumentException();
        } */

        mConnections.add(connection);
    }

    public List<Connection> getConnections() {
        return mConnections;
    }

    public void setThreshold(double threshold) {
        mIsThreshold = true;
        mThreshold = threshold;
    }

    public boolean hasThreshold() {
        return mIsThreshold;
    }

    public void removeThreshold() {
        mIsThreshold = false;
    }

    public double getOutput() {
        return mOutput;
    }

    public void input(double input) {
        if (!mIsThreshold) {
            mOutput = input;
        }
    }

    public void setActivationFunction(@NotNull ActivationFunction af) {
        mAF = af;
    }

    protected double f(double exc) {
        return mAF.call(exc);
    }

    public double compute() {
        if (mIsThreshold) {
            return (mOutput = mThreshold);
        }

        double exc = .0f;
        for (Connection connection : mConnections) {
            double out = connection.getFrom().getOutput();
            exc += out * connection.getWeight();
        }

        return (mOutput = f(exc));
    }
}
