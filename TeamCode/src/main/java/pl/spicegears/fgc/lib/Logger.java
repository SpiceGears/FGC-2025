package pl.spicegears.fgc.lib;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Logger {

    private final Telemetry telemetry;

    public Logger(Telemetry telemetry) {
        this.telemetry = telemetry;
        //add ftcdashboard
    }

    public void addLine(String caption, Object value) {
        telemetry.addData(caption, value);
    }

    public void addStatus(Subsystem subsystem) {
        telemetry.addData(subsystem.getName(), subsystem.logStatus());
    }

    public void send() {
        telemetry.update();
    }

}
