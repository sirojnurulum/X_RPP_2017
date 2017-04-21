/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.util.ArrayList;

/**
 *
 * @author ABC
 */
public class MeetingPoint {

    private Coordinate coordinate;
    private ArrayList<Street> streets;

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public ArrayList<Street> getStreets() {
        return streets;
    }

    public void setStreets(ArrayList<Street> streets) {
        this.streets = streets;
    }

    public MeetingPoint() {
    }

    public MeetingPoint(Coordinate coordinate, ArrayList<Street> streets) {
        this.coordinate = coordinate;
        this.streets = streets;
    }

}
