package g54915.model.util.exception;

/**
 * <code>RepositoryException</code> is thrown when an resources can't be access.
 */
public class RepositoryException extends Exception {

    /**
     * Creates a new instance of <code>RepositoryException</code> without detail
     * message.
     */
    public RepositoryException() {
        super();
    }

    /**
     * Constructs an instance of <code>RepositoryException</code> with the specified
     * detail message.
     *
     * @param msg message of the exception.
     */
    public RepositoryException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>RepositoryException</code> and wrapped the
     * source exception.
     *
     * @param exception wrapped exception.
     */
    public RepositoryException(Exception exception) {
        super(exception);
    }
}