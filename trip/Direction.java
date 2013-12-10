package trip;

/** Direction for a Road.
 *  @author Aleks Kamko
 */
enum Direction {

    /** Possible directions. */
    NORTH, EAST, SOUTH, WEST;

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
