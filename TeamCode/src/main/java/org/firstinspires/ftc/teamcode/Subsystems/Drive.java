package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Drive {
    private final LinearOpMode opMode;
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private double speedModifier;
    private double leftPower;
    private double rightPower;

    public Drive(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        leftDrive = opMode.hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = opMode.hardwareMap.get(DcMotor.class, "rightDrive");

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        speedModifier = 1.0;
    }

    public void drive(double drive, double turn) {
        leftPower = Range.clip(drive + turn, -1.0, 1.0) * speedModifier;
        rightPower = Range.clip(drive - turn, -1.0, 1.0) * speedModifier;

        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    public void stop() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }

    public void turn(Double left, Double right) {
        leftDrive.setPower(left);
        rightDrive.setPower(right);
    }

    public void driveDistanceMeters(double meters, double power) {
        double TICKS_PER_REV = 537.7;
        double WHEEL_DIAMETER_METERS = 0.070;
        double WHEEL_CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER_METERS;

        // Convert meters to ticks
        int ticks = (int) ((meters / WHEEL_CIRCUMFERENCE) * TICKS_PER_REV);

        // Reset encoders
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set target
        leftDrive.setTargetPosition(ticks);
        rightDrive.setTargetPosition(ticks);

        // Run to position
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power
        leftDrive.setPower(power);
        rightDrive.setPower(power);

        // Wait until done
        while (opMode.opModeIsActive() && leftDrive.isBusy() && rightDrive.isBusy()) {
            opMode.telemetry.addData("Left Pos", leftDrive.getCurrentPosition());
            opMode.telemetry.addData("Right Pos", rightDrive.getCurrentPosition());
            opMode.telemetry.update();
        }

        // Stop motors
        leftDrive.setPower(0);
        rightDrive.setPower(0);

        // Back to normal mode
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setSpeedModifier(double modifier) {
        this.speedModifier = modifier;
    }

    public double getSpeedModifier() {
        return speedModifier;
    }

    public double getLeftPower() { return leftPower; }
    public double getRightPower() { return rightPower; }
}
