package main;

import base.Neuron;
import base.SSN;
import io.InputHandler;
import train.Trainer;
import train.TrainingInput;
import train.TrainingSet;
import ui.MainWindow;

import java.io.IOException;
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
       // MainWindow mainWindow = new MainWindow();
        try {
            InputHandler inputHandler = new InputHandler("/home/mrlukashem/Pulpit/testingData");
            inputHandler.setContentReceiver(x -> {
                List<TrainingInput<Double> > inputs = x;

                SSN ssn = new SSN(31 + 1 /* bias */, 8 + 1 /* bias */, 8);
                List<Double> r1 = ssn.pushInput(inputs.get(0).getInputs());
                ssn.trainMe(inputs);

                for (TrainingInput<Double> test : inputs) {
                    List<Double> r2 = ssn.pushInput(test.getInputs());
                    List<Double> e = r1;
                }

         //       ssn.showSSNOnConsole();
            });
            inputHandler.pullData();
        } catch (IOException e) {

        }
    }
}
