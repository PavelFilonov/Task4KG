package com.company.third;

import com.company.math.Matrix4;
import com.company.math.Vector3;
import com.company.math.Vector4;

public class TranslateViewer implements IViewer {

    private Matrix4 translate, rotate, scale, projection;

    public TranslateViewer() {
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

    public void initTranslate() {
        this.translate = Matrix4.one();
    }

    public void modifyTranslate(Matrix4 dp) {
        this.translate = dp.mul(this.translate);
    }
}
