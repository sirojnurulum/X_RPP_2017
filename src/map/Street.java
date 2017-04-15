/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.util.List;

/**
 *
 * @author ABC
 */
public class Street {

    private String name;
    private List<Coordinate> points;
    private double length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Coordinate> getPoints() {
        return points;
    }

    public void setPoints(List<Coordinate> points) {
        this.points = points;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public Street(String name, List<Coordinate> points, double length) {
        this.name = name;
        this.points = points;
        this.length = length;
    }

    public Street() {
    }

}
