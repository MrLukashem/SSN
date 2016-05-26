package base;

import java.util.List;

/**
 * Created by mrlukashem on 24.05.16.
 */
public interface INeuron {
    void addConnection(Connection connection);
    double getOutput();
    void input(double input);
    double compute();
    List<Connection> getConnections();
    void setThreshold(double threshold);
    boolean hasThreshold();
}
