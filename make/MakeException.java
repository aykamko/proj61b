package make;

/** An unchecked exception during a build.
 *  @author Aleks Kamko
 */
class MakeException extends RuntimeException {

    /** A MakeException with no message. */
    MakeException() {
    }

    /** A MakeException with MSG as its message. */
    MakeException(String msg) {
        super(msg);
    }

    /** Returns an exception containing an error message formatted according
     *  to FORMAT and ARGS, as for printf or String.format. Typically, one uses
     *  this by throwing the result in a context where there is a 'try' block
     *  that handles it by printing the message (esp. via reportError). */
    static MakeException error(String format, Object... args) {
        return new MakeException(String.format(format, args));
    }

}
