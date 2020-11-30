package com.company.panels;

import com.company.CameraController;
import com.company.draw.IDrawer;
import com.company.draw.SimpleEdgeDrawer;
import com.company.math.Vector3;
import com.company.models.CommonModel;
import com.company.models.Parallelepiped;
import com.company.screen.ScreenConverter;
import com.company.third.Camera;
import com.company.third.Scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class DrawPanel extends JPanel
        implements CameraController.RepaintListener {

    private Scene scene;
    private ScreenConverter sc;
    private Camera cam;
    private CameraController camController;

    public DrawPanel() {
        super();
        sc = new ScreenConverter(-1, 1, 2, 2, 1, 1);
        cam = new Camera();
        camController = new CameraController(cam, sc);
        scene = new Scene(Color.WHITE.getRGB());
        scene.showAxes();

        scene.getModelsList().add(new Parallelepiped(
                new Vector3(-0.4f, -0.4f, -0.4f),
                new Vector3(0.4f, 0.4f, 0.4f)
        ));

        camController.addRepaintListener(this);
        addMouseListener(camController);
        addMouseMotionListener(camController);
        addMouseWheelListener(camController);
    }

    public void addModel(List<Vector3> list, boolean closed) {
        scene.getModelsList().add(new CommonModel(list, closed));
    }

    public void setPointsProjection(float x, float y) {
        List<Vector3> list = new ArrayList<>();
        list.add(new Vector3(x, 0, 0));
        list.add(new Vector3(0, y, 0));
        scene.getModelsList().add(new CommonModel(list, false));

        camController.setProjection(x, y);
    }

    public void setPointsProjection(float x, float y, float z) {
        List<Vector3> list = new ArrayList<>();
        list.add(new Vector3(x, 0, 0));
        list.add(new Vector3(0, y, 0));
        list.add(new Vector3(0, 0, z));
        scene.getModelsList().add(new CommonModel(list, true));

        camController.setProjection(x, y, z);
    }

    @Override
    public void paint(Graphics g) {
        sc.setScreenSize(getWidth(), getHeight());
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D)bi.getGraphics();
        IDrawer dr = new SimpleEdgeDrawer(sc, graphics);
        scene.drawScene(dr, cam);
        g.drawImage(bi, 0, 0, null);
        graphics.dispose();
    }

    @Override
    public void shouldRepaint() {
        repaint();
    }
}
