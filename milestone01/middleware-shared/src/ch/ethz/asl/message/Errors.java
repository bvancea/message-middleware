package ch.ethz.asl.message;

/**
 * Contains error codes returned to the client.
 */
public class Errors {

    public static final int AUTHENTICATION_FAILED = -1;
    public static final int QUEUE_CREATION_FAILED = -1;

    /**
     * All purpose error for operations concerning the database.
     */
    public static final int DATABASE_FAILURE = -1;

    public static final int NO_SUCH_QUEUE = -1;
    public static final int NO_SUCH_RECEIVER = -2;

    public static String[] encodeError(int errorCode) {
        return new String[] {String.valueOf(errorCode)};
    }

}
