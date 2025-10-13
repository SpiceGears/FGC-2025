package pl.spicegears.fgc.lib;

import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class Subsystem {

    private Status subsystemStatus;
    private final String subsystemName;

    public Subsystem(String subsystemName) {
        this.subsystemName = subsystemName;
    }

    public abstract void init(HardwareMap hardwareMap);
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

    protected int getStatusCode() {
        return subsystemStatus.getStatusCode().getCode();
    }

}
