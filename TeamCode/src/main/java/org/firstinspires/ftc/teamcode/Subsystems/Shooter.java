package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Shooter {
    private final LinearOpMode opMode;
    private DcMotor leftShooter;
    private DcMotor rightShooter;
    private double power;

    public Shooter(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        leftShooter = opMode.hardwareMap.get(DcMotor.class, "leftShooter");
        rightShooter = opMode.hardwareMap.get(DcMotor.class, "rightShooter");

        leftShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightShooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftShooter.setDirection(DcMotorSimple.Direction.REVERSE);
        rightShooter.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void startSpinning(float speed) {
        leftShooter.setPower(speed);
        rightShooter.setPower(speed);
    }

    public void stopSpinning() {
        leftShooter.setPower(0);
        rightShooter.setPower(0);
    }
}