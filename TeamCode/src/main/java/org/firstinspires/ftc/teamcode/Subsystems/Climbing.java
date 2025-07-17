package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Climbing {
    private final LinearOpMode opMode;
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private double leftPower;
    private double rightPower;

    public Climbing(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        leftDrive = opMode.hardwareMap.get(DcMotor.class, "leftClimb");
        rightDrive = opMode.hardwareMap.get(DcMotor.class, "rightClimb");

        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive() {
        leftDrive.setPower(1);
        rightDrive.setPower(1);
    }

    public void reverse() {
        leftDrive.setPower(-1);
        rightDrive.setPower(-1);
    }

    public void stop() {
        rightDrive.setPower(0);
        leftDrive.setPower(0);
    }
}