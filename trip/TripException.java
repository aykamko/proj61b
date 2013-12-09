package trip;

/** An unchecked exception in the trip package.
 *  @author Aleks Kamko
 */
public class TripException extends RuntimeException {

    /** A TripException with no message. */
    public TripException() {
    }

    /** A TripException with MSG as its message. */
    public TripException(String msg) {
        super(msg);
    }

}
