package trip;

import graph.Weightable;

class Location implements Weightable, Comparable<Location> {

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

    public Double xPos() {
        return _xPos;
    }

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
