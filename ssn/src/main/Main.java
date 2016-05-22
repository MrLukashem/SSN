package main;

import base.Neuron;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mrlukashem on 21.05.16.
 */
public class Main {
    public static void main(String[] args) {
        Neuron neuron = new Neuron(3);
        neuron.setActivationFunction(exc -> {
            return exc >= 0 ? 1 : 0;
        });

        List<Float> inputs = Arrays.asList(1.0f, 2.0f, 3.0f);
        neuron.setInput(inputs);
        List<Float> weights = Arrays.asList(1.5f, -2.0f, 4.0f);
        neuron.setWeights(weights);
        neuron.setThreshold(2);

        int out = neuron.compute();
        System.out.println("out = " + out);
    }
}
