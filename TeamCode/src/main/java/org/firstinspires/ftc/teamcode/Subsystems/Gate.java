package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Gate {
    private final LinearOpMode opMode;
    private DcMotor intake;
    private double power;

    public Gate(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        intake = opMode.hardwareMap.get(DcMotor.class, "gate");

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void openGate() {
        intake.setPower(1);
    }

    public void closeGate() {
        intake.setPower(-1);
    }

    public void stop() {
        intake.setPower(0);
    }
}