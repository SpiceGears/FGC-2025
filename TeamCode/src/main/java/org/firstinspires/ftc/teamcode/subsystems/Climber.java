package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.Subsystem;

public class Climber extends Subsystem {

    DcMotor leftClimb;
    DcMotor rightClimb;
    HardwareMap hardwareMap;

    public Climber(HardwareMap hardwareMap) {
        super("Climber");
        this.hardwareMap = hardwareMap;
    }

    public void init() {
        leftClimb = hardwareMap.get(DcMotor.class, Config.CLIMBER_LEFT_MOTOR);
        rightClimb = hardwareMap.get(DcMotor.class, Config.CLIMBER_RIGHT_MOTOR);

        leftClimb.setDirection(Config.CLIMBER_LEFT_REVERSE ? Direction.REVERSE : Direction.FORWARD);
        leftClimb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftClimb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightClimb.setDirection(Config.CLIMBER_RIGHT_REVERSE ? Direction.REVERSE : Direction.FORWARD);
        rightClimb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightClimb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void startMotors() {
        leftClimb.setPower(1);
        rightClimb.setPower(1);
    }

    private void reverseMotors() {
        leftClimb.setPower(-1);
        rightClimb.setPower(-1);
    }

    private void stopMotors() {
        rightClimb.setPower(0);
        leftClimb.setPower(0);
    }

    public void  handle(boolean climbForward, boolean climbBackward)
    {
        if (climbForward) startMotors();
        else if (climbBackward) reverseMotors();
        else stopMotors();
    }
}
