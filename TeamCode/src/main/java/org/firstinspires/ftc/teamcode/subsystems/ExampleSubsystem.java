package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.StatusCode;
import pl.spicegears.fgc.lib.Subsystem;

public class ExampleSubsystem extends Subsystem {

    DcMotor motor;
    HardwareMap hardwareMap;

    public ExampleSubsystem(HardwareMap map) {
        super("example");
        hardwareMap = map;
    }

    public void init() {
        try {
            motor = hardwareMap.get(DcMotor.class, Config.exampleMotorName);
            //get motor hardware connection with name from Config

            setStatus(StatusCode.INITIATED);
        } catch (Exception e) {
            setStatus(StatusCode.HARDWARE_NOT_FOUND);
        }
    }

    public void exampleCommand() {
        if(getStatusCode() < 10) return;
        //When hardware not initiated correctly,
        //just return every command and don't use hardware until resolved

        motor.setPower(1);
    }
}
