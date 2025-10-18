package pl.spicegears.fgc.lib;

public abstract class Subsystem {

    private Status subsystemStatus;
    private final String subsystemName;

    public Subsystem(String subsystemName) {
        this.subsystemName = subsystemName;
        init();
    }

    public abstract void init();
    public String logStatus() {
        return subsystemStatus.getStatus();
    }

    public String getName() {
        return subsystemName;
    }

    protected void setStatus(StatusCode statusCode) {
        this.subsystemStatus = new Status(statusCode);
    }

    protected void setStatus(StatusCode statusCode, String statusMessage) {
        this.subsystemStatus = new Status(statusCode, statusMessage);
    }

    public int getStatusCode() {
        return subsystemStatus.getStatusCode().getCode();
    }

}
