package com.company.models;

import com.company.math.Vector3;
import com.company.third.IModel;
import com.company.third.PolyLine3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommonModel implements IModel {

    private List<Vector3> points;

    private boolean closed;

    public CommonModel(List<Vector3> points, boolean closed) {
        this.points = new ArrayList<>(points);
        this.closed = closed;
    }

    @Override
    public List<PolyLine3D> getLines() {

        List<PolyLine3D> lines = new LinkedList<>();

        if (points.size() == 1)
            lines.add(getLine(0, 0));
        else {

            for (int i = 1; i < points.size(); i++) {
                lines.add(getLine(i - 1, i));
            }

            if (closed && points.size() > 2)
                lines.add(getLine(points.size() - 1, 0));
        }

        return lines;
    }

    private PolyLine3D getLine(int firstIndex, int secondIndex) {

        return new PolyLine3D(Arrays.asList(
                points.get(firstIndex),
                points.get(secondIndex)),
                false
        );
    }
}
