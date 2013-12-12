package trip;

/** Direction for a Road.
 *  @author Aleks Kamko
 */
enum Direction {

    /** Possible directions. */
    NORTH, EAST, SOUTH, WEST;

    /** Returns the direction for the first character in DIRSTRING.
     *  Throws a TripException if a bad direction is given. */
    public static Direction firstCharDirection(String dirString) {
        Direction direction = null;
        switch (dirString) {
        case "NS": {
            direction = SOUTH;
            break;
        } case "SN": {
            direction = NORTH;
            break;
        } case "EW": {
            direction = WEST;
            break;
        } case "WE": {
            direction = EAST;
            break;
        } default: {
            throw new TripException("bad direction given");
        }
        }
        return direction;
    }

    /** Returns the opposite cardinal direction. */
    public Direction opposite() {
        Direction result = null;
        switch (this) {
        case NORTH: {
            result = SOUTH;
            break;
        } case SOUTH: {
            result = NORTH;
            break;
        } case EAST: {
            result = WEST;
            break;
        } case WEST: {
            result = EAST;
            break;
        } default:
            break;
        }
        return result;
    }

    /** Return my lower-case name. */
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
