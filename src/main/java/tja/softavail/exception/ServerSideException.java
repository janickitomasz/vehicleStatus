package tja.softavail.exception;

public class ServerSideException extends RuntimeException{
    String message;

    public ServerSideException() {
    }

    public ServerSideException(String message) {
        this.message = message;
    }

    public ServerSideException(String message, String message1) {
        super(message);
        this.message = message1;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
