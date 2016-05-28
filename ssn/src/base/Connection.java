package base;

import com.sun.istack.internal.NotNull;

/**
 * Created by mrlukashem on 22.05.16.
 */
public class Connection {
    protected INeuron mRefFrom;
    protected INeuron mRefTo;
    protected double mWeight;
    protected double mUpdate;
    protected double mDelta;
    protected double mLastWeight;

    public Connection(@NotNull INeuron from, @NotNull INeuron to) {
        mRefFrom = from;
        mRefFrom = to;
        mLastWeight = 0;
    }

    public Connection(@NotNull INeuron from, @NotNull INeuron to, double w) {
        mRefFrom = from;
        mRefTo = to;
        mWeight = w;
        mLastWeight = 0;
    }

    public INeuron getFrom() {
        return mRefFrom;
    }

    public INeuron getTo() {
        return mRefTo;
    }

    public double getWeight() {
        return mWeight;
    }

    public void updateWeight(double update) {
        mUpdate = update;
    }

    public void setDelta(double delta) {
        mDelta = delta;
    }

    public double getDelta() {
        return mDelta;
    }

    public void commitUpdate() {
        mLastWeight = mWeight;
        mWeight += mUpdate;
    }

    public double getLastWeight() {
        return mLastWeight;
    }
}
