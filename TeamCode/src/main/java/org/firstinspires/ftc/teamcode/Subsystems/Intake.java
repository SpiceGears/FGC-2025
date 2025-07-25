package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Intake {
    private final LinearOpMode opMode;
    private DcMotor intake;
    private double power;

    public Intake(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        intake = opMode.hardwareMap.get(DcMotor.class, "intake");

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void take(float power) {
        intake.setPower(power);
    }

    public void untake(float power) {
        intake.setPower(-power);
    }

    public void stop() {
        intake.setPower(0);
    }
}