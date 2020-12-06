package com.company.third;

import com.company.math.Vector3;

/**
 * Описывает основную функциональность камеры - превращение координат
 * из мировой системы координат в систему координат камеры.
 * @author Alexey
 */
public interface IViewer {
    /**
     * Преобразует точку из мировой системы координат в систему координат камеры
     * @param v преобразуемая точка
     * @return новая точка
     */
    public Vector3 w2s(Vector3 v);
}