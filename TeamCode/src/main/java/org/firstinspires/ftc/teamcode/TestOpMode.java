package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TestOpMode", group="Testing")
public class TestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "Running");
        telemetry.update();

        while(opModeIsActive()) {

            telemetry.addData("OpMode Status", "Active");

            telemetry.update();
        }
    }
}