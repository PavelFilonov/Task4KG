package com.company;

import com.company.math.Vector3;
import com.company.panels.CreatingModelPanel;
import com.company.panels.DrawPanel;
import com.company.panels.DataPanel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ManagerPanels implements DataPanel.Closeable, CreatingModelPanel.CreatingModel {

    private JFrame frame;

    private DrawPanel drawPanel;

    private DataPanel dataPanel;

    private CreatingModelPanel cm;

    private RealCoordinates rc;

    private int width = 700, height = 700;

    private boolean openDrawPanel = false, openDataPanel = true, openCreatingModelPanel = false;

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
        dataPanel = new DataPanel();
        dataPanel.setCloseable(this);
        frame.add(dataPanel);
        frame.setVisible(true);
    }

    @Override
    public void shouldClose() {
        rc = dataPanel.getRealCoordinates();
//        drawPanel.setPointsProjection(rc.getX(), rc.getY());
        drawPanel.setPointsProjection(rc.getX(), rc.getY(), rc.getZ());
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
        if (!openDrawPanel) {
            frame.add(drawPanel);
            openDrawPanel = true;
        }

        if (openDataPanel) {
            frame.remove(dataPanel);
            openDataPanel = false;
        }

        if (openCreatingModelPanel) {
            frame.remove(cm);
            openCreatingModelPanel = false;
        }

        repaint();
    }

    private void toDataPanel() {
        if (openDrawPanel) {
            frame.remove(drawPanel);
            openDrawPanel = false;
        }

        if (!openDataPanel) {
            frame.add(dataPanel);
            openDataPanel = true;
        }

        if (openCreatingModelPanel) {
            frame.remove(cm);
            openCreatingModelPanel = false;
        }

        repaint();
    }

    private void toCreatingModelPanel() {
        if (openDrawPanel) {
            frame.remove(drawPanel);
            openDrawPanel = false;
        }

        if (openDataPanel) {
            frame.remove(dataPanel);
            openDataPanel = false;
        }

        if (!openCreatingModelPanel) {
            cm = new CreatingModelPanel();
            cm.setCreating(this);
            frame.add(cm);
            openCreatingModelPanel = true;
        }

        repaint();
    }

    private void repaint() {
        frame.setSize(width + 1, height + 1);
        frame.setSize(width, height);
    }
}
