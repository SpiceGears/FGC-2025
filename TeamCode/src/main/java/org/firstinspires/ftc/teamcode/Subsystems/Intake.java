package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Intake {
    private final LinearOpMode opMode;
    private DcMotor intakeRight;
    private DcMotor intakeLeft;
    private double power;

    public Intake(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        intakeLeft = opMode.hardwareMap.get(DcMotor.class, "intakeLeft");
        intakeRight = opMode.hardwareMap.get(DcMotor.class, "intakeRight");

        intakeLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void take(float power) {
        intakeLeft.setPower(power);
        intakeRight.setPower(power);
    }

    public void stop() {
        intakeLeft.setPower(0);
        intakeRight.setPower(0);
    }
}