package io;

import base.InputNeuron;
import com.sun.istack.internal.NotNull;
import train.TrainingInput;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrlukashem on 24.05.16.
 */
public class InputHandler {
    protected File mHandle;
    protected ContentReceiver mReceiver;

    protected String mSplittingChar = ";";

    // TODO: we can create async handler.
    public interface ContentReceiver {
        void handler(List<TrainingInput<Float> > content);
    }

    public InputHandler(@NotNull String fileName) throws IOException {
        mHandle = new File(fileName);

        if (!mHandle.canRead()) {
            throw new IOException();
        }
    }

    public void setContentReceiver(@NotNull ContentReceiver receiver) {
        mReceiver = receiver;
    }

    public void pullData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(mHandle));

        String line;
        List<TrainingInput<Float> > content = new ArrayList<>();
        ArrayList<Float> inputs = new ArrayList<>();
        while((line = reader.readLine()) != null) {
            String[] result = line.split(mSplittingChar);
            if (result.length != 34 /* temporary hardcoded number of features value.*/) {
                throw new IOException();
            }

            for (String feature /* includes expected result also.*/: result) {
                Float convertedFeature = Float.valueOf(feature);
                inputs.add(convertedFeature);
            }

            float answer = inputs.remove(inputs.size() - 1);
            content.add(new TrainingInput<>((List<Float>)inputs.clone() /* don't push ref, allocate new memory.*/,
                    (int)answer));
        }

        // trigger client's callback.
        mReceiver.handler(content);
    }
}
