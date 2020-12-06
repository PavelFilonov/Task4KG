package com.company.panels;

import com.company.math.Vector3;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CreatingModelPanel extends JPanel {

    private List<Vector3> list = new ArrayList<>();
    private boolean closed = false;

    private JTextField xTF = new JTextField(5);
    private JTextField yTF = new JTextField(5);
    private JTextField zTF = new JTextField(5);

    private JCheckBox cb = new JCheckBox();

    private JButton addVectorButton = new JButton("Добавить точку");
    private JButton removeVectorButton = new JButton("Удалить точку");
    private JButton createModelButton = new JButton("Создать модель");

    public CreatingModelPanel(CreatingModel creating) {
        this.creating = creating;

        add(new JLabel("Координата X:"));
        add(xTF);

        add(new JLabel("Координата Y:"));
        add(yTF);

        add(new JLabel("Координата Z:"));
        add(zTF);

        add(new JLabel("Замкнутая модель"));
        add(cb);

        add(addVectorButton);
        add(removeVectorButton);
        add(createModelButton);

        addVectorButton.addActionListener(e -> {
            Vector3 vector = getVector();

            if (vector != null)
                list.add(vector);
        });

        removeVectorButton.addActionListener(e -> {
            Vector3 vector = getVector();

            if (vector != null)
                list.remove(vector);
        });

        createModelButton.addActionListener(e -> {
            closed = cb.isSelected();
            creating.shouldAddModel();
        });
    }

    public interface CreatingModel {
        void shouldAddModel();
    }

    private CreatingModel creating;

    private Vector3 getVector() {
        float x;
        float y;
        float z;

        try {
            x = Float.parseFloat(xTF.getText());
            y = Float.parseFloat(yTF.getText());
            z = Float.parseFloat(zTF.getText());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }

        return new Vector3(x, y, z);
    }

    public List<Vector3> getList() {
        return list;
    }

    public boolean isClosed() {
        return closed;
    }
}
