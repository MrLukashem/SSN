package ui;

import base.SSN;
import com.sun.istack.internal.NotNull;
import io.InputHandler;
import train.TrainingInput;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by mrlukashem on 24.05.16.
 */
public class MainWindow extends JFrame {
    private JTextField mFilePathField;
    private JTextField mFeaturesField;
    private JButton computeButton;
    private JButton trainButton;
    private JPanel mPanel;
    private JButton checkPassRateButton;
    private JLabel mPassRateTextField;
    private JTextField mHLTextField;
    private JButton loadFileButton;
    private JButton mTrainWithMomentumButton;
    private JTextField mLearningRate;
    private InputHandler mInputHandler;

    private final static String APP_NAME = "SSN";
    private List<TrainingInput<Double> > inputs;
    private SSN ssn;

    private boolean mIsInitialized = false;

    public MainWindow() throws IOException {
        super(APP_NAME);
        initWindow();

        mInputHandler = new InputHandler("/home/mrlukashem/Pulpit/testingData");
        mInputHandler.setContentReceiver(x -> {
            inputs = x;
            mIsInitialized = true;
        });
        mInputHandler.pullData();

        initListeners();
    }

    private void initWindow() {
        setContentPane(mPanel);
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private double checkPassRate(List<TrainingInput<Double> > testingSet) {
        int total = 0;
        int good = 0;

        for (TrainingInput<Double> i : testingSet) {
            List<Double> res = ssn.pushInput(i.getInputs());
            int index = -1;
            double temp = .0;

            for (int j = 0; j < res.size();j++) {
                if (res.get(j) > temp) {
                    temp = res.get(j);
                    index = j;
                }
            }

            if (index + 1 == i.getExpectedOutput()) {
                good++;
            }
            total++;
        }

        return (double)good / (double)total;
    }

    private void initListeners() {
        checkPassRateButton.addActionListener(e -> {
            mPassRateTextField.setText(String.valueOf(checkPassRate(inputs)));
        });

        trainButton.addActionListener(e -> {
            triggerTraining(false);
        });

        mTrainWithMomentumButton.addActionListener(e -> {
            triggerTraining(true);
        });

        loadFileButton.addActionListener(e -> {
            try {
                String path = mFilePathField.getText();
                mInputHandler.pullData(path);
            } catch(IOException exc) {
                // have to be filled.
            }
        });

        computeButton.addActionListener(e -> {
            // to be filled.
        });
    }

    private double testCore(List<TrainingInput<Double> > trainingSet,
                            List<TrainingInput<Double> > testingSet, boolean momentum) {
        int counter5x2cv = 100; // Does not work for value = 5;
        while (counter5x2cv >= 0) {
            if (!momentum) {
                ssn.trainMe(trainingSet, trainingSet.size());
            } else {
                ssn.trainMeWithMomentum(trainingSet, trainingSet.size());
            }

            --counter5x2cv;
        }

        return checkPassRate(testingSet);
    }

    private void triggerTraining(boolean momentum) {
        int ni;
        double lr = 0;

        try {
            String n = mHLTextField.getText();
            String slr = mLearningRate.getText();
            ni = Integer.valueOf(n);

            if (!slr.isEmpty()) {
                lr = Double.valueOf(slr);
            }
        } catch(Exception exc) {
            JOptionPane.showMessageDialog(this, "Not valid input" /* Switch to a field */);
            return;
        }

        ssn = new SSN(31 + 1 /* bias */, ni + 1 /* bias */, 8);
        if (lr != 0) {
            ssn.setLearningConstant(lr);
        }
        List<TrainingInput<Double> > trainingSet = new LinkedList<>();
        List<TrainingInput<Double> > testingSet = new LinkedList<>();
        create2cvSets(trainingSet, testingSet);

        double passRateSum = .0;
        testCore(trainingSet, testingSet, momentum);

        // Train it with testing set now.
        passRateSum += testCore(testingSet, trainingSet, momentum);

        double resultPassRate = passRateSum;
        mPassRateTextField.setText(String.valueOf(resultPassRate));
    }

    private boolean create2cvSets(@NotNull List<TrainingInput<Double> > trainingSet /* output arg */,
                                  @NotNull List<TrainingInput<Double> > testingSet /* output arg */) {
        if (!mIsInitialized) {
            return false;
        }

        trainingSet.clear();
        testingSet.clear();

        Random random = new Random();
        List<TrainingInput<Double> > inputsClone = new ArrayList<>(inputs);

        int counter = (int)Math.abs((double)inputsClone.size() / 2.0);
        int rNumber = 0;
        int size = 0;
        while(counter > 0) {
            rNumber = (size = (inputsClone.size() - 1)) == 0 ? 0 : random.nextInt(size);
            // Get a random features set and then remove it from inputs.
            trainingSet.add(inputsClone.remove(rNumber));

            rNumber = (size = (inputsClone.size() - 1)) == 0 ? 0 : random.nextInt(size);
            testingSet.add(inputsClone.remove(rNumber));
            --counter;
        }

        // Add last element if sets are not symmetrical.
        return inputsClone.size() <= 0 || trainingSet.add(inputsClone.remove(random.nextInt(inputsClone.size() - 1)));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
