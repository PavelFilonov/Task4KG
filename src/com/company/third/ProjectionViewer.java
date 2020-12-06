package com.company.third;

import com.company.math.Matrix4;
import com.company.math.Vector3;
import com.company.math.Vector4;

public class ProjectionViewer implements IViewer {
    private Matrix4 translate, rotate, scale, projection;


    /**
     * Создаёт простую камеру
     */
    public ProjectionViewer() {
        translate = Matrix4.one();
        rotate = Matrix4.one();
        scale = Matrix4.one();
        projection = Matrix4.one();
    }

    /**
     * Метод, преобразуюший точку из меировой системы координат в систему координат камеры.
     * Сначала к вектору применяется масштаб(S), далее поворот(R), потом перенос(T) и в конце - проекция(P).
     * r = P * T * R * S * v
     * @param v вектор, который надо перевести
     * @return новый вектор
     */
    @Override
    public Vector3 w2s(Vector3 v) {
        return projection.mul(
                translate.mul(
                        rotate.mul(
                                scale.mul(
                                        new Vector4(v, 1)
                                )
                        )
                )
        ).asVector3();
    }

    public void modifyProjection(Matrix4 dp) {
        this.projection = dp.mul(this.projection);
    }

    public void initProjection() {
        this.projection = Matrix4.one();
    }

    public void initTranslate() {
        this.translate = Matrix4.one();
    }

    public void modifyTranslate(Matrix4 dp) {
        this.translate = dp.mul(this.translate);
    }

    public Matrix4 getProjection() {
        return projection;
    }

    public void setProjection(Matrix4 projection) {
        this.projection = projection;
    }

    public Matrix4 getTranslate() {
        return translate;
    }

    public Matrix4 getRotate() {
        return rotate;
    }

    public Matrix4 getScale() {
        return scale;
    }
}
