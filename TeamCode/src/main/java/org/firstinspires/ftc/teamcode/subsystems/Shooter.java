package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.StatusCode;
import pl.spicegears.fgc.lib.Subsystem;

public class Shooter extends Subsystem {

    DcMotorEx leftShooterMotor;
    DcMotorEx rightShooterMotor;
    DcMotor passMotor;
    boolean shooterActive;
    public Shooter() {
        super("Shooter");
    }

    public void init(HardwareMap hardwareMap) {
        try {
            leftShooterMotor = hardwareMap.get(DcMotorEx.class, Config.SHOOTER_LEFT_MOTOR);
            rightShooterMotor = hardwareMap.get(DcMotorEx.class, Config.SHOOTER_RIGHT_MOTOR);
            passMotor = hardwareMap.get(DcMotor.class, Config.PASS_MOTOR);

            leftShooterMotor.setDirection(Config.SHOOTER_LEFT_REVERSE ? Direction.REVERSE : Direction.FORWARD);
            leftShooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftShooterMotor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);

            rightShooterMotor.setDirection(Config.SHOOTER_RIGHT_REVERSE ? Direction.REVERSE : Direction.FORWARD);
            rightShooterMotor.setMode(RunMode.RUN_USING_ENCODER);
            rightShooterMotor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);

            passMotor.setDirection(Config.PASS_REVERSE ? Direction.REVERSE : Direction.FORWARD);
            passMotor.setMode(RunMode.RUN_USING_ENCODER);
            passMotor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);

            shooterActive = false;

            setStatus(StatusCode.INITIATED);
        } catch (Exception e) {
            setStatus(StatusCode.HARDWARE_NOT_FOUND);
            if(Config.DEBUG) setStatus(StatusCode.HARDWARE_NOT_FOUND, e.getMessage());
        }
    }

    public void startShooterMotors()
    {
        leftShooterMotor.setPower(1);
        rightShooterMotor.setPower(1);
    }

    public void stopShooterMotors()
    {
        leftShooterMotor.setPower(0);
        rightShooterMotor.setPower(0);
    }

    public void startPassMotors()
    {
        passMotor.setPower(1);
    }

    public void reversePassMotors()
    {
        passMotor.setPower(-1);
    }

    public void stopPassMotors()
    {
        passMotor.setPower(0);
    }

    public void handlePass(boolean forward, boolean reverse)
    {
        if(getStatusCode() < 10) return;
        if (forward) startPassMotors();
        else if (reverse) reversePassMotors();
        else stopPassMotors();
    }

    public void handleShooter(boolean start, boolean stop)
    {
        if(getStatusCode() < 10) return;
        if (start) shooterActive = true;
        else if (stop) shooterActive = false;

        if (shooterActive) startShooterMotors();
        else stopShooterMotors();

        //double vel=leftShooterMotor.getVelocity();

        //if (shooterActive) this.setStatus(StatusCode.INITIATED, String.valueOf("Current velocity: " + vel));
    }

    public double[] getVelocities() {
        return new double[]{leftShooterMotor.getVelocity(), rightShooterMotor.getVelocity()};
    }
}