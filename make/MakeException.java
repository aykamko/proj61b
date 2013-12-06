package make;

/** An unchecked exception during a build.
 *  @author Aleks Kamko
 */
public class MakeException extends RuntimeException {

    /** A MakeException with no message. */
    public MakeException() {
    }

    /** A MakeException with MSG as its message. */
    public MakeException(String msg) {
        super(msg);
    }

}
