package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.StatusCode;
import pl.spicegears.fgc.lib.Subsystem;

public class Intake extends Subsystem {

    HardwareMap hardwareMap;
    DcMotor motor;

    public Intake(HardwareMap hardwareMap) {
        super("Intake");
        this.hardwareMap = hardwareMap;
    }
    public void init() {
        try {
            motor = hardwareMap.get(DcMotor.class, Config.INTAKE_MOTOR);
            motor.setDirection(Config.INTAKE_REVERSE ? Direction.REVERSE : Direction.FORWARD);
            motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);

            setStatus(StatusCode.INITIATED);
        } catch (Exception e) {
            setStatus(StatusCode.HARDWARE_NOT_FOUND);
        }
    }

    public void handle(double intakeForward, double intakeReverse) {
        if(getStatusCode() < 10) return;

        if (intakeForward > Config.TRIGGER_THRESHOLD) startMotor();
        else if (intakeReverse > Config.TRIGGER_THRESHOLD) startMotorReverse();
        else stopMotor();
    }

    public void handle(boolean intakeForward, boolean intakeReverse) {
        if(getStatusCode() < 10) return;

        if (intakeForward) startMotor();
        else if (intakeReverse) startMotorReverse();
        else stopMotor();
    }

    private void startMotor() {
        motor.setPower(1);
    }

    private void startMotorReverse() {
        motor.setPower(-1);
    }

    private void stopMotor() {
        motor.setPower(0);
    }
}
