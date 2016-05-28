package ui;

import base.SSN;
import io.InputHandler;
import train.TrainingInput;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

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
    private JLabel passRate;
    private JTextField mHLTextField;
    private JButton loadFileButton;
    private JTextField mTrainIterationsNumber;
    private InputHandler mInputHandler;

    private final static String APP_NAME = "SSN";
    private List<TrainingInput<Double> > inputs;
    private SSN ssn;

    public MainWindow() throws IOException {
        super(APP_NAME);
        initWindow();

        mInputHandler = new InputHandler("/home/mrlukashem/Pulpit/testingData");
        mInputHandler.setContentReceiver(x -> {
            inputs = x;
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

    private void initListeners() {
        checkPassRateButton.addActionListener(e -> {
            int total = 0;
            int good = 0;

            for (TrainingInput<Double> i : inputs) {
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

            double r = (double)good / (double)total;
            passRate.setText(String.valueOf(r));
        });

        trainButton.addActionListener(e -> {
            int itr = 0;
            int ni = 0;

            try {
                String n = mHLTextField.getText();
                String iterations = mTrainIterationsNumber.getText();
                ni = Integer.valueOf(n);
                itr = Integer.valueOf(iterations);
            } catch(Exception exc) {
                JOptionPane.showMessageDialog(this, "Not valid input");
                return;
            }

            ssn = new SSN(31 + 1 /* bias */, ni + 1 /* bias */, 8);
            ssn.trainMe(inputs, itr);
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
