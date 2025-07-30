package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Bucket {
    private final LinearOpMode opMode;
    private DcMotor upperMotor;
    private double speedModifier;
    private double leftPower;
    private double rightPower;

    public Bucket(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        upperMotor = opMode.hardwareMap.get(DcMotor.class, "upperMotor");

        upperMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        speedModifier = 1.0;
    }

    public void setMotorPower(double power) {
        upperMotor.setPower(power);
    }

    public void stop() {
        upperMotor.setPower(0);
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
