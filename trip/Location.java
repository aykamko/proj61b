package trip;

class Location {

    Location(String place, Integer xCoord, Integer yCoord) {
        _place = place;
        _xCoord = xCoord;
        _yCoord = yCoord;
    }

    /** Name of the place for this location. */
    private final String _place;
    /** X coordinate of this location. */
    private final Integer _xCoord;
    /** Y coordinate of this location. */
    private final Integer _yCoord;

}
