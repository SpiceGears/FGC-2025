package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class Climbing {
    private final LinearOpMode opMode;
    private DcMotor leftClimb;
    private DcMotor rightClimb;

    public Climbing(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        leftClimb = opMode.hardwareMap.get(DcMotor.class, "leftClimb");
        rightClimb = opMode.hardwareMap.get(DcMotor.class, "rightClimb");

        leftClimb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightClimb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftClimb.setDirection(DcMotorSimple.Direction.REVERSE);
        rightClimb.setDirection(DcMotorSimple.Direction.FORWARD);

        leftClimb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightClimb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void drive() {
        leftClimb.setPower(1);
        rightClimb.setPower(1);
    }

    public void reverse() {
        leftClimb.setPower(-1);
        rightClimb.setPower(-1);
    }

    public void stop() {
        rightClimb.setPower(0);
        leftClimb.setPower(0);
    }
}