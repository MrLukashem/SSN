package ui;

import javax.swing.*;

/**
 * Created by mrlukashem on 24.05.16.
 */
public class MainWindow extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JButton computeButton;
    private JButton trainButton;
    private JPanel mPanel;

    private final static String APP_NAME = "SSN";

    public MainWindow() {
        super(APP_NAME);
        setContentPane(mPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
