package pds.stardust.scaccount.exception;

/**
 * CustomException : represents a specific exception related to service's processes
 */
public class CustomException extends RuntimeException {

    private int id;
    private String message;
    private String details;

    public CustomException(int id, String message, String details) {
        this.id = id;
        this.message = message;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
