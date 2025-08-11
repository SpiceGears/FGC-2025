package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Intake {
    private final LinearOpMode opMode;
    private DcMotor intake;
    private DcMotor pass;
    private double power;

    public Intake(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        intake = opMode.hardwareMap.get(DcMotor.class, "intake");
        pass = opMode.hardwareMap.get(DcMotor.class, "pass");

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        pass.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void take(float power) {
        intake.setPower(power);
    }

    public void stopTaking() {
        intake.setPower(0);
    }

    public void pass(float power) {
        pass.setPower(power);
    }

    public void stopPassing() {
        pass.setPower(0);
    }
}