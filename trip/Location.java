package trip;

import graph.Weightable;

/** Represents a Location in a graph of the map.
 *  @author Aleks Kamkp
 */
class Location implements Weightable, Comparable<Location> {

    /** Default constructor for location. Takes in a name PLACE, an
     *  x-position XPOS, and a y-position YPOS. */
    Location(String place, Double xPos, Double yPos) {
        _place = place;
        _xPos = xPos;
        _yPos = yPos;
        _weight = 0;
    }

    /** Returns the name of this Location. */
    public String place() {
        return _place;
    }

    /** Returns the x-position of this Location. */
    public Double xPos() {
        return _xPos;
    }

    /** Returns the y-position of this Location. */
    public Double yPos() {
        return _yPos;
    }

    @Override
    public void setWeight(double weight) {
        _weight = weight;
    }

    @Override
    public double weight() {
        return _weight;
    }

    @Override
    public int compareTo(Location other) {
        return _place.compareTo(other.place());
    }

    /** Name of the place for this location. */
    private final String _place;
    /** X coordinate of this location. */
    private final Double _xPos;
    /** Y coordinate of this location. */
    private final Double _yPos;
    /** Weight of this vertex. */
    private double _weight;

}
