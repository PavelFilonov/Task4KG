package com.company.panels;

import com.company.ViewManager;
import com.company.RealCoordinates;
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
        implements ViewManager.RepaintListener {

    private Scene scene;
    private ScreenConverter sc;
    private Camera camera;
    private ProjectionViewer pv;
    private TranslateViewer tv;
    private RotateViewer rv;
    private ViewManager viewController;

    public DrawPanel() {
        super();
        sc = new ScreenConverter(-1, 1, 2, 2, 1, 1);
        camera = new Camera();
        pv = new ProjectionViewer();
        tv = new TranslateViewer();
        rv = new RotateViewer();
        viewController = new ViewManager(camera, sc, pv);
        scene = new Scene(Color.WHITE.getRGB());
        scene.showAxes();

        scene.getModelsList().add(new Parallelepiped(
                new Vector3(-0.4f, -0.4f, -0.4f),
                new Vector3(0.4f, 0.4f, 0.4f)
        ));

        viewController.addRepaintListener(this);
        addMouseListener(viewController);
        addMouseMotionListener(viewController);
        addMouseWheelListener(viewController);
    }

    public void addModel(List<Vector3> list, boolean closed) {
        scene.getModelsList().add(new CommonModel(list, closed));
    }

    public void setPointsProjection(RealCoordinates[] rc) {
        viewController.setProjection(rc);

//        double dx = rc[1].getX() * Math.PI / 180;
//        double dy = rc[0].getY() * Math.PI / 180;
//        double dz = (rc[0].getZ() + rc[1].getZ()) * Math.PI / 180;
//        rv.modifyRotate(
//                Matrix4Factories.rotationXYZ(dz, Matrix4Factories.Axis.Z)
//                        .mul(Matrix4Factories.rotationXYZ(dy, Matrix4Factories.Axis.Z)
//                                .mul(Matrix4Factories.rotationXYZ(dx, Matrix4Factories.Axis.Z)))
//        );

//        Vector4 zero = new Vector4(sc.s2r(new ScreenPoint(0, 0)), 0);
//        Vector4 cur = new Vector4(rc[1].getX(), rc[0].getY(), rc[0].getZ() + rc[1].getZ());
//        Vector3 delta = cur.add(zero.mul(-1)).asVector3();
//        tv.modifyTranslate(Matrix4Factories.translation(delta));
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
        scene.drawScene(dr, camera, pv, rv);
        g.drawImage(bi, 0, 0, null);
        graphics.dispose();
    }

    @Override
    public void shouldRepaint() {
        repaint();
    }
}
