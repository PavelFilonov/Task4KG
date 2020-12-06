package com.company;

import com.company.math.Matrix4Factories;
import com.company.math.Vector3;
import com.company.math.Vector4;
import com.company.third.IViewer;
import com.company.third.ProjectionViewer;

public class ViewManager {

    private ProjectionViewer viewer;

    private RealCoordinates[] rc;

    public ViewManager(ProjectionViewer viewer) {
        this.viewer = viewer;
    }

    public void setProjection(RealCoordinates[] rc) {
        this.rc = rc;
        viewer.initProjection();
        viewer.modifyProjection(Matrix4Factories.centralProjection(rc[0].getX(), Matrix4Factories.Axis.X));
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
