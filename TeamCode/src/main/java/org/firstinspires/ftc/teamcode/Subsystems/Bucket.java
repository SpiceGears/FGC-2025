package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Bucket {
    private final LinearOpMode opMode;
    private CRServo leftServo;
    private CRServo rightServo;
    private double speedModifier;
    private double leftPower;
    private double rightPower;

    public Bucket(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        leftServo = opMode.hardwareMap.get(CRServo.class, "leftServo");
        rightServo = opMode.hardwareMap.get(CRServo.class, "rightServo");

        leftServo.setDirection(DcMotorSimple.Direction.REVERSE);
        rightServo.setDirection(DcMotorSimple.Direction.FORWARD);

        speedModifier = 1.0;
    }

    public void setMotorPower(double power) {
        leftServo.setPower(power);
        rightServo.setPower(power);
    }

    public void stop() {
        leftServo.setPower(0);
        rightServo.setPower(0);
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
