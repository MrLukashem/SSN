package base;

import com.sun.istack.internal.NotNull;

/**
 * Created by mrlukashem on 22.05.16.
 */
public class Connection {
    INeuron mRefFrom;
    INeuron mRefTo;
    float mWeight;

    public Connection(@NotNull INeuron from, @NotNull INeuron to) {
        mRefFrom = from;
        mRefFrom = to;
    }

    public Connection(@NotNull INeuron from, @NotNull INeuron to, float w) {
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

    public float getWeight() {
        return mWeight;
    }

    public void updateWeight(float update) {
        mWeight += update;
    }
}
