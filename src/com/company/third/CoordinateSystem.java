package com.company.third;

import com.company.math.Vector3;

public class CoordinateSystem {

    private float x0, y0, z0;

    public CoordinateSystem(float x0, float y0, float z0) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
    }

    public Vector3 toThisSystem(Vector3 v) {
        return new Vector3(
                v.getX() - x0,
                v.getY() - y0,
                v.getZ() - z0
        );
    }

    public Vector3 fromThisSystem(Vector3 v) {
        return new Vector3(
                x0 + v.getX(),
                y0 + v.getY(),
                z0 + v.getZ()
        );
    }

    public float getX0() {
        return x0;
    }

    public float getY0() {
        return y0;
    }

    public float getZ0() {
        return z0;
    }
}
