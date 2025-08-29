package org.firstinspires.ftc.teamcode.CrystalSubsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Intake {
    private LinearOpMode opMode;
    private DcMotor intakeMotor;
    public Intake(LinearOpMode opMode) {this.opMode = opMode;}

    public void init()
    {
        intakeMotor = opMode.hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void  handle(boolean intakeForward, boolean intakeReverse)
    {
        if (intakeForward) intake();
        else if (intakeReverse) extake();
        else stop();
    }

    public void intake()
    {
        intakeMotor.setPower(1);
    }

    public void extake()
    {
        intakeMotor.setPower(-1);
    }

    public void stop()
    {
        intakeMotor.setPower(0);
    }

}
