package pl.spicegears.fgc.lib;

public class Status {

    private StatusCode statusCode = StatusCode.NOT_INITIATED;
    private String statusMessage = "";

    public Status(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Status(StatusCode statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public String getStatus() {
        if(!statusMessage.isEmpty())
            return statusCode.toString() + " - " + statusMessage;
        return statusCode.toString();
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

}
