package com.company.panels;

import com.company.RealCoordinates;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataPanel extends JPanel implements ActionListener {

    private JTextField x1TF = new JTextField(5);
    private JTextField y1TF = new JTextField(5);
    private JTextField z1TF = new JTextField(5);
    private JTextField x2TF = new JTextField(5);
    private JTextField y2TF = new JTextField(5);
    private JTextField z2TF = new JTextField(5);

    private RealCoordinates[] rc;

    private JButton b = new JButton("Выполнить");

    public DataPanel(Closeable closeable) {
        this.closeable = closeable;

        x1TF.setText("3");
        y1TF.setText("3");
        z1TF.setText("3");
        x2TF.setText("1");
        y2TF.setText("1");
        z2TF.setText("1");

        add(new JLabel("X1:"));
        add(x1TF);
        add(new JLabel("Y1:"));
        add(y1TF);
        add(new JLabel("Z1:"));
        add(z1TF);

        add(new JLabel("X2:"));
        add(x2TF);
        add(new JLabel("Y2:"));
        add(y2TF);
        add(new JLabel("Z2:"));
        add(z2TF);

        add(b);

        b.addActionListener(this);
    }

    public interface Closeable {
        void shouldClose();
    }

    private Closeable closeable;

    @Override
    public void actionPerformed(ActionEvent e) {
        float x1;
        float y1;
        float z1;
        float x2;
        float y2;
        float z2;

        try {
            x1 = Float.parseFloat(x1TF.getText());
            y1 = Float.parseFloat(y1TF.getText());
            z1 = Float.parseFloat(z1TF.getText());
            x2 = Float.parseFloat(x2TF.getText());
            y2 = Float.parseFloat(y2TF.getText());
            z2 = Float.parseFloat(z2TF.getText());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return;
        }

        rc = new RealCoordinates[2];
        rc[0] = new RealCoordinates(x1, y1, z1);
        rc[1] = new RealCoordinates(x2, y2, z2);

        closeable.shouldClose();
    }

    public RealCoordinates getRealCoordinatesFirstVector() {
        return rc[0];
    }

    public RealCoordinates getRealCoordinatesSecondVector() {
        return rc[1];
    }
}
