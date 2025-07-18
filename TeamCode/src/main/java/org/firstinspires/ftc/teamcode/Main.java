package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

@TeleOp(name = "Wojtek_runningsystem")
public class Main extends LinearOpMode {

    @Override
    public void runOpMode() {
        DriveSubsystem drive = new DriveSubsystem(this);
        drive.init();

        // Check motor status and display diagnostics
        boolean motorsOK = drive.checkMotors();
        telemetry.addLine(motorsOK ? "✅ All motors working." : "❌ Error: One or more motors not working!");
        telemetry.addLine("--- Motor status ---");
        telemetry.addLine(drive.getMotorStatus());
        telemetry.update();

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.y) {
                int newMode = drive.getDriveMode() + 1;
                if (newMode > 2) newMode = 0;
                drive.setDriveMode(newMode);
                sleep(300); // debounce delay
            }

            // Get joystick input
            double driveVal = -gamepad1.left_stick_y; // forward/backward
            double turnVal = gamepad1.left_stick_x; // turning
            boolean fullSpeed = gamepad1.right_bumper; // speed mode

            drive.drive(driveVal, turnVal, fullSpeed);

            String modeText;
            switch (drive.getDriveMode()) {
                case 0:
                    modeText = "All motors";
                    break;
                case 1:
                    modeText = "Front only";
                    break;
                case 2:
                    modeText = "Rear only";
                    break;
                default:
                    modeText = "Unknown";
            }

            // Display telemetry
            telemetry.addData("Drive mode", modeText);
            telemetry.addData("Drive", "%.2f", driveVal);
            telemetry.addData("Turn", "%.2f", turnVal);
            telemetry.addData("Full speed", fullSpeed ? "Yes" : "No");
            telemetry.update();
        }

        // Send a message when the OpMode stops
        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}
