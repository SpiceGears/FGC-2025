package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.StatusCode;
import pl.spicegears.fgc.lib.Subsystem;

public class Drivetrain extends Subsystem {

    DcMotor leftMotor;
    DcMotor rightMotor;

    public Drivetrain() {
        super("Drivetrain");
    }

    public void init(HardwareMap hardwareMap) {
        try {
            leftMotor = hardwareMap.get(DcMotor.class, Config.DRIVE_LEFT_MOTOR);
            rightMotor = hardwareMap.get(DcMotor.class, Config.DRIVE_RIGHT_MOTOR);

            leftMotor.setDirection(Config.DRIVE_LEFT_REVERSE ? Direction.REVERSE : Direction.FORWARD);
            rightMotor.setDirection(Config.DRIVE_RIGHT_REVERSE ? Direction.REVERSE : Direction.FORWARD);

            leftMotor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);
            rightMotor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);

            leftMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);
            rightMotor.setMode(RunMode.STOP_AND_RESET_ENCODER);

            leftMotor.setMode(RunMode.RUN_USING_ENCODER);
            rightMotor.setMode(RunMode.RUN_USING_ENCODER); //TEST

            setStatus(StatusCode.INITIATED);
        } catch (Exception e) {
            setStatus(StatusCode.HARDWARE_NOT_FOUND);
            if(Config.DEBUG) setStatus(StatusCode.HARDWARE_NOT_FOUND, e.getMessage());
        }
    }

    double leftPower, rightPower;

    public void drive(double forward, double turn) {

        if(getStatusCode() < 10) return;

        leftPower = Range.clip(forward + turn, -1.0, 1.0);
        rightPower = Range.clip(forward - turn, -1.0, 1.0);

        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }
}
