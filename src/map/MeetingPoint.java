/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

/**
 *
 * @author ABC
 */
public class MeetingPoint {

    private Coordinate coordinate;
    private String[] street;

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String[] getStreet() {
        return street;
    }

    public void setStreet(String[] street) {
        this.street = street;
    }

    public MeetingPoint(Coordinate coordinate, String[] street) {
        this.coordinate = coordinate;
        this.street = street;
    }

    public MeetingPoint() {
    }

}
