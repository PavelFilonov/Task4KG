package com.company.panels;

import com.company.CameraController;
import com.company.RealCoordinates;
import com.company.ViewManager;
import com.company.draw.IDrawer;
import com.company.draw.SimpleEdgeDrawer;
import com.company.math.Matrix4Factories;
import com.company.math.Vector3;
import com.company.math.Vector4;
import com.company.models.CommonModel;
import com.company.models.Parallelepiped;
import com.company.screen.ScreenConverter;
import com.company.screen.ScreenPoint;
import com.company.third.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;

public class DrawPanel extends JPanel
        implements CameraController.RepaintListener {

    private Scene scene;
    private ScreenConverter sc;
    private Camera camera;
    private ProjectionViewer pv;
    private CameraController camController;
    private ViewManager manager;

    public DrawPanel() {
        super();
        sc = new ScreenConverter(-1, 1, 2, 2, 1, 1);
        camera = new Camera();
        pv = new ProjectionViewer();
        camController = new CameraController(camera, sc);
        manager = new ViewManager(pv);
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

    public void setPointsProjection(RealCoordinates[] rc) {
//        List<Vector3> list = new ArrayList<>();
//        list.add(new Vector3(rc[0].getX(), rc[0].getY(), rc[0].getZ()));
//        list.add(new Vector3(rc[1].getX(), rc[1].getY(), rc[1].getZ()));
//        scene.getModelsList().add(new CommonModel(list, false));
//        Vector3 v = getVectorNewCoordinates(rc);
//        scene.setX0(v.getX());
//        scene.setY0(v.getY());
//        scene.setZ0(v.getZ());
        manager.setProjection(rc);
        repaint();
    }

    private Vector3 getVectorNewCoordinates(RealCoordinates[] rc) {
        CoordinateSystem cs = new CoordinateSystem(
                rc[1].getX(),
                rc[0].getY(),
                rc[0].getZ() + rc[1].getZ()
        );

        return new Vector3(
                cs.getX0(),
                cs.getY0(),
                cs.getZ0()
        );
    }

    @Override
    public void paint(Graphics g) {
        sc.setScreenSize(getWidth(), getHeight());
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D)bi.getGraphics();
        IDrawer dr = new SimpleEdgeDrawer(sc, graphics);
        scene.drawScene(dr, camera, pv);
        g.drawImage(bi, 0, 0, null);
        graphics.dispose();
    }

    @Override
    public void shouldRepaint() {
        repaint();
    }
}
