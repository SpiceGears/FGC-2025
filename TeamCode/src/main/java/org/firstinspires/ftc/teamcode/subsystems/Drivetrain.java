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
    HardwareMap hardwareMap;

    public Drivetrain(HardwareMap hardwareMap) {
        super("Drivetrain");
        this.hardwareMap = hardwareMap;
    }

    public void init() {
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

    /// ### Get current position which is written as ticks
    /// Returns an array with the following indexes
    /// - 0 - left motor ticks
    /// - 1 - right motor ticks
    public int[] getCurrentPosition()
    {
        int leftTicks = leftMotor.getCurrentPosition();
        int rightTicks = rightMotor.getCurrentPosition();
        return new int[]{leftTicks, rightTicks};
    }


    /// ## Drives the robot to the position specified by encoder ticks
    /// ### Note: the positions are absolute, and not offset-ed by current encoder positions
    public void driveToPos(int leftTargetTicks, int rightTargetTicks, double power)
    {
        leftMotor.setTargetPosition(leftTargetTicks);
        rightMotor.setTargetPosition(rightTargetTicks);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(power);
        rightMotor.setPower(power);

        while (leftMotor.isBusy() || rightMotor.isBusy())
        {
            setStatus(StatusCode.INITIATED, "Active run to position");
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
        setStatus(StatusCode.INITIATED);
    }
}
