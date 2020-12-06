package com.company;

import com.company.math.Vector3;
import com.company.panels.CreatingModelPanel;
import com.company.panels.DrawPanel;
import com.company.panels.DataPanel;
import com.company.third.CoordinateSystem;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ManagerPanels implements DataPanel.Closeable, CreatingModelPanel.CreatingModel {

    private JFrame frame;

    private DrawPanel drawPanel;

    private DataPanel dataPanel;

    private CreatingModelPanel cm;

    private RealCoordinates[] rc = new RealCoordinates[2];

    private int width = 700, height = 700;

    public ManagerPanels() {
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setFocusable(true);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_1)
                    toDrawPanel();

                if (e.getKeyCode() == KeyEvent.VK_2)
                    toDataPanel();

                if (e.getKeyCode() == KeyEvent.VK_3)
                    toCreatingModelPanel();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        drawPanel = new DrawPanel();
        dataPanel = new DataPanel(this);
        frame.add(dataPanel);
        frame.setVisible(true);
    }

    @Override
    public void shouldClose() {
        rc[0] = dataPanel.getRealCoordinatesFirstVector();
        rc[1] = dataPanel.getRealCoordinatesSecondVector();
        drawPanel.setPointsProjection(rc);
        toDrawPanel();
    }

    @Override
    public void shouldAddModel() {
        List<Vector3> list = cm.getList();
        boolean closed = cm.isClosed();

        if (list.size() == 0)
            return;

        drawPanel.addModel(list, closed);
        toDrawPanel();
    }

    private void toDrawPanel() {
        removeAll();
        frame.add(drawPanel);

        repaint();
    }

    private void toDataPanel() {
        removeAll();
        frame.add(dataPanel);

        repaint();
    }

    private void toCreatingModelPanel() {
        removeAll();
        cm = new CreatingModelPanel(this);
        frame.add(cm);

        repaint();
    }

    private void removeAll() {
        frame.remove(drawPanel);
        frame.remove(dataPanel);
        if (cm != null)
            frame.remove(cm);
    }

    private void repaint() {
        frame.setSize(width + 1, height + 1);
        frame.setSize(width, height);
    }
}
