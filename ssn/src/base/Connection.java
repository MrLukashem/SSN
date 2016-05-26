package base;

import com.sun.istack.internal.NotNull;

/**
 * Created by mrlukashem on 22.05.16.
 */
public class Connection {
    INeuron mRefFrom;
    INeuron mRefTo;
    double mWeight;

    public Connection(@NotNull INeuron from, @NotNull INeuron to) {
        mRefFrom = from;
        mRefFrom = to;
    }

    public Connection(@NotNull INeuron from, @NotNull INeuron to, double w) {
        mRefFrom = from;
        mRefTo = to;
        mWeight = w;
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
        mWeight += update;
    }
}
