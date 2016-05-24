package main;

import base.Neuron;
import train.Trainer;
import train.TrainingInput;
import train.TrainingSet;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by mrlukashem on 21.05.16.
 */
public class Main {
    public static float f(float x) {
        return 2 * x + 1;
    }

    public static void main(String[] args) {
        Neuron neuron = new Neuron(2);
        neuron.setActivationFunction(exc -> {
            return exc >= 0 ? 1 : 0;
        });

        neuron.setThreshold(1);

        Trainer trainer = Trainer.getTrainer();
        Random random = new Random();

        List<TrainingInput<Float> > inputsList = new LinkedList<>();
        for(int i = 0; i < 1000; i++) {
            float x = random.nextFloat();
            x *= 100.0f;
            x -= 50.0f;
            float y = random.nextFloat();
            y *= 100.0f;
            y -= 50.0f;
            int answer = 1;
            if (y < f(x)) {
                answer = 0;
            }
            TrainingInput<Float> input = new TrainingInput<>(Arrays.asList(x, y), answer);
            inputsList.add(input);
        }

        TrainingSet<Float> set = new TrainingSet<>(inputsList);
        trainer.addTrainingSet(set);
        trainer.train(neuron);

        float out = neuron.compute();
        System.out.println("out = " + out);
    }
}
