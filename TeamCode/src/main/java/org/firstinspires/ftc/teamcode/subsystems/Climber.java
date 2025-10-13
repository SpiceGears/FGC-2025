package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.StatusCode;
import pl.spicegears.fgc.lib.Subsystem;

public class Climber extends Subsystem {

    DcMotor leftClimb;
    DcMotor rightClimb;
    Servo leftLock, rightLock;

    public Climber() {
        super("Climber");
    }

    public void init(HardwareMap hardwareMap) {
        try {
            leftClimb = hardwareMap.get(DcMotor.class, Config.CLIMBER_LEFT_MOTOR);
            rightClimb = hardwareMap.get(DcMotor.class, Config.CLIMBER_RIGHT_MOTOR);

            leftClimb.setDirection(Config.CLIMBER_LEFT_REVERSE ? Direction.REVERSE : Direction.FORWARD);
            leftClimb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftClimb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            rightClimb.setDirection(Config.CLIMBER_RIGHT_REVERSE ? Direction.REVERSE : Direction.FORWARD);
            rightClimb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightClimb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            leftLock = hardwareMap.get(Servo.class, Config.LOCK_LEFT_SERVO);
            rightLock = hardwareMap.get(Servo.class, Config.LOCK_RIGHT_SERVO);

            setStatus(StatusCode.INITIATED);
        } catch (Exception e) {
            setStatus(StatusCode.HARDWARE_NOT_FOUND);
            if(Config.DEBUG) setStatus(StatusCode.HARDWARE_NOT_FOUND, e.getMessage());
        }
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

    private void unlock() {
        leftLock.setPosition(Config.UNLOCK_POS);
        rightLock.setPosition(Config.UNLOCK_POS);
    }

    private void lock() {
        leftLock.setPosition(Config.LOCK_POS);
        rightLock.setPosition(Config.LOCK_POS);
    }

    public void handleLock(boolean unlock, boolean lock) {
        if(getStatusCode() < 10) return;
        if(unlock) unlock();
        else if(lock) lock();
    }

    public void handleClimber(boolean climbForward, boolean climbBackward)
    {
        if(getStatusCode() < 10) return;
        if (climbForward) startMotors();
        else if (climbBackward) reverseMotors();
        else stopMotors();
    }
}
