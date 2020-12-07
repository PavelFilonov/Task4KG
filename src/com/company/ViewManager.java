package com.company;

import com.company.math.Matrix4;
import com.company.math.Matrix4Factories;
import com.company.math.Vector3;
import com.company.math.Vector4;
import com.company.screen.ScreenConverter;
import com.company.screen.ScreenPoint;
import com.company.third.Camera;
import com.company.third.ProjectionViewer;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;

public class ViewManager implements MouseListener, MouseMotionListener, MouseWheelListener {

    /**
     * Интерфейс, объявляющий набор метод, которые обязан реализовать слушатель
     */
    public static interface RepaintListener {
        /**
         * Метод, вызываемый при изменении
         */
        void shouldRepaint();
    }

    private Set<RepaintListener> listeners = new HashSet<>();

    /**
     * Метод добавления слушателя
     * @param listener слушатель
     */
    public void addRepaintListener(RepaintListener listener) {
        listeners.add(listener);
    }

    /**
     * Метод удаления слушателя
     * @param listener слушатель
     */
    public void removeRepaintListener(RepaintListener listener) {
        listeners.remove(listener);
    }

    /**
     * Вспомогательный метод, который оповещает всех слушателей о произошедшем событии.
     */
    protected void onRepaint() {
        for (RepaintListener cl : listeners)
            cl.shouldRepaint();
    }

    private Camera camera;
    private ProjectionViewer viewer;
    private ScreenConverter sc;
    private RealCoordinates[] rc;

    public ViewManager(Camera camera, ScreenConverter sc, ProjectionViewer viewer) {
        this.camera = camera;
        this.viewer = viewer;
        this.sc = sc;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /*Здесь запоминаем последнее положение мыши, для которого обрабатывали событие*/
    private Point last;
    /*Флаг, фиксирующий, зажата ли сейчас левая кнопка мыши*/
    private boolean leftFlag = false;
    /*Флаг, фиксирующий, зажата ли сейчас правая кнопка мыши*/
    private boolean rightFlag = false;
    /*Флаг, фиксирующий, зажата ли сейчас средняя кнопка мыши*/
    private boolean middleFlag = false;

    @Override
    public void mousePressed(MouseEvent e) {
        /*Устанавливаем флаги кнопок мыши*/
        if (SwingUtilities.isLeftMouseButton(e))
            leftFlag = true;
        if (SwingUtilities.isRightMouseButton(e))
            rightFlag = true;
        if (SwingUtilities.isMiddleMouseButton(e))
            middleFlag = true;
        last = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        /*Снимаем флаги кнопок мыши*/
        if (SwingUtilities.isLeftMouseButton(e))
            leftFlag = false;
        if (SwingUtilities.isRightMouseButton(e))
            rightFlag = false;
        if (SwingUtilities.isMiddleMouseButton(e))
            middleFlag = false;

        /*Если оба сняты, то забываем точку*/
        if (!leftFlag && !rightFlag && !middleFlag)
            last = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point current = e.getPoint();
        if (last != null) {
            /*Вычисляем разницу в пикселях*/
            int dx = current.x - last.x;
            int dy = current.y - last.y;
            /*Если двигаем с зажатой левой кнопкой мыши, то вращаем камеру*/
            if (leftFlag) {
                double da = dx * Math.PI / 180;
                double db = dy * Math.PI / 180;
                camera.modifyRotate(
                        Matrix4Factories.rotationXYZ(da, Matrix4Factories.Axis.Y)
                                .mul(
                                        Matrix4Factories.rotationXYZ(db, Matrix4Factories.Axis.X)
                                )
                );
            }
            /*Если двигаем с зажатой правой кнопкой мыши, то перемещаем камеру вдоль осей X и Y*/
            if (rightFlag) {
                Vector4 zero = new Vector4(sc.s2r(new ScreenPoint(0, 0)), 0);
                Vector4 cur = new Vector4(sc.s2r(new ScreenPoint(dx, dy)), 0);

                /*Вектор смещения в реальных координатах с точки зрения камеры*/
                Vector3 delta = cur.add(zero.mul(-1)).asVector3();
                camera.modifyTranslate(Matrix4Factories.translation(delta));
            }
            /* Если двигаем с зажатой средней кнопкой мыши, то перемещаем камеру
             * вдоль оси Z на расстояние равное изменению положения мыши в реальных координатах.
             * Направление выбирается положительное при движении вверх.
             */
            if (middleFlag && dy != 0) {
                Vector4 zero = new Vector4(sc.s2r(new ScreenPoint(0, 0)), 0);
                Vector4 cur = new Vector4(sc.s2r(new ScreenPoint(dx, dy)), 0);
                /*Длина вектор смещения в реальных координатах с точки зрения камеры*/
                float length = cur.add(zero.mul(-1)).asVector3().length();
                if (dy < 0)
                    length = -length;
                camera.modifyTranslate(Matrix4Factories.translation(0, 0, length));
            }
        }
        last = current;
        onRepaint(); /*Оповещаем всех, что мы изменили камеру и её надо перерисовать*/
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int delta = e.getWheelRotation();
        /*Если зажат Control, то будем менять параметры перспективы, иначе - масштаба*/
        if (e.isControlDown()) {
            /*delta*5f - экспериментально подобранное число. Чем меньше, тем быстрее будет изменяться точка схода*/
            viewer.modifyProjection(Matrix4Factories.centralProjection(delta*5f, Matrix4Factories.Axis.Z));
        } else {
            /*Вычислим коэффициент масштаба*/
            float factor = 1;
            float scale = delta < 0 ? 0.9f : 1.1f;
            int counter = delta < 0 ? -delta : delta;
            while (counter-- > 0)
                factor *= scale;
            camera.modifyScale(Matrix4Factories.scale(factor));
        }
        onRepaint();
    }

    public void setProjection(RealCoordinates[] rc) {
        this.rc = rc;
        viewer.initProjection();
//        double da = rc[0].getZ() * Math.PI / 180;
//        Matrix4 m = Matrix4Factories.rotationXYZ(da, Matrix4Factories.Axis.X);
        viewer.modifyProjection(Matrix4Factories.centralProjection(rc[0].getX(), Matrix4Factories.Axis.X));

//        da = (rc[0].getZ() + rc[1].getZ()) * Math.PI / 180;
//        m = Matrix4Factories.rotationXYZ(da, Matrix4Factories.Axis.Y);
        viewer.modifyProjection(Matrix4Factories.centralProjection(rc[1].getY(), Matrix4Factories.Axis.Y));
    }

    public Vector3 getVector(Vector3 v) {
        viewer.initProjection();
        viewer.initTranslate();

        Vector3 vector3 = q(v);
        viewer.modifyTranslate(Matrix4Factories.translation(rc[0].getX(), rc[0].getY(), rc[0].getZ()));
        viewer.modifyProjection(Matrix4Factories.centralProjection(rc[0].getX(), Matrix4Factories.Axis.X));
        viewer.initTranslate();

        vector3 = q(vector3);

        viewer.modifyTranslate(Matrix4Factories.translation(rc[1].getX(), rc[1].getY(), rc[1].getZ()));
        viewer.modifyProjection(Matrix4Factories.centralProjection(rc[1].getY(), Matrix4Factories.Axis.Y));
        viewer.initTranslate();

        vector3 = q(vector3);
        return vector3;
    }

    private Vector3 q(Vector3 v) {
        return viewer.getProjection().mul(
                viewer.getTranslate().mul(
                        viewer.getRotate().mul(
                                viewer.getScale().mul(
                                        new Vector4(v, 1)
                                )
                        )
                )
        ).asVector3();
    }
}