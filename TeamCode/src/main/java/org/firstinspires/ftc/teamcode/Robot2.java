package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.UniversalDiffDrive;

public class Robot2 extends LinearOpMode {

    UniversalDiffDrive drivetrain;


    @Override
    public void runOpMode()
    {

        Motor left = new Motor(hardwareMap, "leftDrive");
        Motor right = new Motor(hardwareMap, "rightDrive");

        drivetrain = new UniversalDiffDrive(
                new MotorGroup(left),
                new MotorGroup(right)
        );






        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Indicate that the OpMode has started
        telemetry.addData("Status", "Running");
        telemetry.update();

        // Loop while the OpMode is active and not stopped
        while(opModeIsActive()) {


            double forward = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            drivetrain.drive(forward, turn);

            telemetry.addData("Forward: ", forward);
            telemetry.addData("Turn: ", turn);
            telemetry.update();
        }
    }

}
