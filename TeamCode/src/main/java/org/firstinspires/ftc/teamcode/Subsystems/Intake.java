package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Intake {
    private final LinearOpMode opMode;
    private DcMotor intake;
    public Intake(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        intake = opMode.hardwareMap.get(DcMotor.class, "intake");

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void handle(boolean up, boolean down) {
        if(up) {
            take();
        } else if(down) {
            reverseTake();
        } else {
            stop();
        }
    }

    public void take() {
        intake.setPower(-1);
    }

    public void reverseTake() { intake.setPower(1); }

    public void stop() {
        intake.setPower(0);
    }
}