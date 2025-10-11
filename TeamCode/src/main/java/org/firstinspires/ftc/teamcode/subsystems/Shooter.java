package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.StatusCode;
import pl.spicegears.fgc.lib.Subsystem;

public class Shooter extends Subsystem {

    DcMotor leftShooterMotor;
    DcMotor rightShooterMotor;
    DcMotor passMotor;
    HardwareMap hardwareMap;
    boolean shooterActive;
    public Shooter(HardwareMap hardwareMap) {
        super("Shooter");
        this.hardwareMap = hardwareMap;
    }

    public void init() {
        try {
            leftShooterMotor = hardwareMap.get(DcMotor.class, Config.SHOOTER_LEFT_MOTOR);
            rightShooterMotor = hardwareMap.get(DcMotor.class, Config.SHOOTER_RIGHT_MOTOR);
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
        }
    }

    private void startShooterMotors()
    {
        leftShooterMotor.setPower(1);
        rightShooterMotor.setPower(1);
    }

    private void stopShooterMotors()
    {
        leftShooterMotor.setPower(0);
        rightShooterMotor.setPower(0);
    }

    private void startPassMotors()
    {
        passMotor.setPower(1);
    }

    private void reversePassMotors()
    {
        passMotor.setPower(-1);
    }

    private void stopPassMotors()
    {
        passMotor.setPower(0);
    }

    public void handlePass(boolean forward, boolean reverse)
    {
        if (forward) startPassMotors();
        else if (reverse) reversePassMotors();
        else stopPassMotors();
    }

    public void handleShooter(boolean start, boolean stop)
    {
        if (start) shooterActive = true;
        else if (stop) shooterActive = false;

        if (shooterActive) startShooterMotors();
        else stopShooterMotors();
    }
}
