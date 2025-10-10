package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "IntakeTest", group = "Test")
public class IntakeTest extends LinearOpMode {

    private DcMotor intakeMotor;

    @Override
    public void runOpMode() {

        intakeMotor = hardwareMap.get(DcMotor.class, "intake");

        telemetry.addData("Status", "Gotowy do startu");
        telemetry.update();

        waitForStart();

        intakeMotor.setPower(1.0);

        while (opModeIsActive()) {
            telemetry.addData("Intake Power", "1.0");
            telemetry.update();
        }

        intakeMotor.setPower(0);
    }
}