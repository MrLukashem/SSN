package base;

/**
 * Created by mrlukashem on 24.05.16.
 */
public interface INeuron {
    void addConnection(Connection connection);
    float getOutput();
    void input(float input);
    float compute();
}
