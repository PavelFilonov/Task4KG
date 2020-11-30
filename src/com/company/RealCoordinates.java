package com.company;

public class RealCoordinates {

    private float x, y, z;

    public RealCoordinates(float z) {
        this.z = z;
    }

    public RealCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public RealCoordinates(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
