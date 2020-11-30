package com.company.panels;

import com.company.RealCoordinates;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataPanel extends JPanel implements ActionListener {

    private JTextField xTF = new JTextField(5);
    private JTextField yTF = new JTextField(5);
    private JTextField zTF = new JTextField(5);

    private RealCoordinates rc;

    private JButton b = new JButton("Выполнить");

    public DataPanel() {
        xTF.setText("3");
        yTF.setText("3");
        zTF.setText("3");

        add(new JLabel("Координата X:"));
        add(xTF);

        add(new JLabel("Координата Y:"));
        add(yTF);

        add(new JLabel("Координата Z:"));
        add(zTF);

        add(b);

        b.addActionListener(this);
    }

    public interface Closeable {
        void shouldClose();
    }

    private Closeable closeable;

    public void setCloseable(Closeable closeable) {
        this.closeable = closeable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        float x;
        float y;
        float z;

        try {
            x = Float.parseFloat(xTF.getText());
            y = Float.parseFloat(yTF.getText());
            z = Float.parseFloat(zTF.getText());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return;
        }

        if (rc == null)
            rc = new RealCoordinates(x, y, z);
        else {
            rc.setX(x);
            rc.setY(y);
            rc.setZ(z);
        }

        closeable.shouldClose();
    }

    public RealCoordinates getRealCoordinates() {
        return rc;
    }


}
