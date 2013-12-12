package trip;

/** An unchecked exception in the trip package.
 *  @author Aleks Kamko
 */
class TripException extends RuntimeException {

    /** A TripException with no message. */
    TripException() {
    }

    /** A TripException with MSG as its message. */
    TripException(String msg) {
        super(msg);
    }

    /** Returns an exception containing an error message formatted according
     *  to FORMAT and ARGS, as for printf or String.format. Typically, one uses
     *  this by throwing the result in a context where there is a 'try' block
     *  that handles it by printing the message (esp. via reportError). */
    static TripException error(String format, Object... args) {
        return new TripException(String.format(format, args));
    }

}
