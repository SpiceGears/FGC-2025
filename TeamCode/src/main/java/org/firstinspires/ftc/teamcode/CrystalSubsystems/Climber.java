package org.firstinspires.ftc.teamcode.CrystalSubsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Climber {
    private LinearOpMode opMode;
    private DcMotor intakeMotor;
    public Climber(LinearOpMode opMode) {this.opMode = opMode;}

    public void init()
    {
        intakeMotor = opMode.hardwareMap.get(DcMotor.class, "intake");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void  handle(boolean climbForward, boolean climbBackward)
    {
        if (climbForward) climbForward();
        else if (climbBackward) climbBackward();
        else stop();
    }

    public void climbForward()
    {
        intakeMotor.setPower(1);
    }

    public void climbBackward()
    {
        intakeMotor.setPower(-1);
    }

    public void stop()
    {
        intakeMotor.setPower(0);
    }

}
