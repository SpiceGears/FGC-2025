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

        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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

    public void setSpeedModifier(double modifier) {
        this.speedModifier = modifier;
    }

    public double getSpeedModifier() {
        return speedModifier;
    }

    public double getLeftPower() { return leftPower; }
    public double getRightPower() { return rightPower; }
}
