package org.firstinspires.ftc.teamcode.CrystalSubsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Shooter {

    private LinearOpMode opMode;

    private DcMotor shooterMotorL;
    private DcMotor shooterMotorR;

    private DcMotor passerMotor;

    private boolean shooterActive;

    public Shooter(LinearOpMode opm) {this.opMode = opm;}

    public void init()
    {
        shooterMotorL = opMode.hardwareMap.get(DcMotor.class, "leftShooter");
        shooterMotorR = opMode.hardwareMap.get(DcMotor.class, "rightShooter");
        passerMotor = opMode.hardwareMap.get(DcMotor.class, "passShooter");

        passerMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterMotorR.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterMotorL.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void spin()
    {
        shooterMotorL.setPower(-1);
        shooterMotorR.setPower(-1);
    }

    public void stopSpin()
    {
        shooterMotorL.setPower(0);
        shooterMotorR.setPower(0);
    }

    public void pass()
    {
        passerMotor.setPower(1);
    }

    public void reversePass()
    {
        passerMotor.setPower(-1);
    }

    public void stopPass()
    {
        passerMotor.setPower(0);
    }

    public void handlePasser(boolean forward, boolean reverse)
    {
        if (forward) pass();
        else if (reverse) reversePass();
        else stopPass();
    }

    public void handleShooter(boolean start, boolean stop)
    {
        if (start) shooterActive = true;
        else if (stop) shooterActive = false;

        if (shooterActive) spin();
        else stopSpin();
    }
}
