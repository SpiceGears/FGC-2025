package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.Climbing;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;

@TeleOp(name="testDrive", group="Linear Opmode")
public class TestDrive extends LinearOpMode {

    private final Drive drivetrain = new Drive(this);
    private final Climbing climbing = new Climbing(this);
//    private final Shooter shooter = new Shooter(this);
    private final Intake intake = new Intake(this);
    private final Bucket bucket = new Bucket(this);

    @Override
    public void runOpMode() {
        // Initialize the drivetrain subsystem
        drivetrain.init();
        climbing.init();
//        shooter.init();
        intake.init();
        bucket.init();

        // Send a message to the Driver Station that the OpMode is initialized
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Indicate that the OpMode has started
        telemetry.addData("Status", "Running");
        telemetry.update();

        // Loop while the OpMode is active and not stopped
        while(opModeIsActive()) {
            // Get gamepad input for driving
            double drive = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;

            drivetrain.drive(drive, turn);

            if(gamepad1.right_bumper) {
                bucket.setMotorPower(1);
            } else if (gamepad1.left_bumper) {
                bucket.setMotorPower(-1);
            } else {
                bucket.stop();
            }

            if(gamepad1.dpad_down) {
                climbing.drive();
            }

            else if(gamepad1.dpad_up) {
                climbing.reverse();
            }
            else { climbing.stop(); }

            // Command the drivetrain to move
            drivetrain.drive(drive, turn);

            intake.take(gamepad1.right_trigger);
            intake.untake(gamepad1.left_trigger);



//            shooter.shoot(gamepad1.right_trigger);

            // --- TELEMETRY ADDITIONS START HERE ---

            // Display gamepad input values
            telemetry.addData("Gamepad Input", "---");
            telemetry.addData("Drive (Left Stick Y)", "%.2f", drive); // Format to 2 decimal places
            telemetry.addData("Turn (Right Stick X)", "%.2f", turn);

            // Display calculated motor powers from the Drive subsystem
            telemetry.addData("Motor Powers (Raw)", "---");
            telemetry.addData("Left Power", "%.2f", drivetrain.getLeftPower());
            telemetry.addData("Right Power", "%.2f", drivetrain.getRightPower());

            // Display current speed modifier
            telemetry.addData("Speed Modifier", "%.2f", drivetrain.getSpeedModifier());

            // Display overall OpMode status (optional, but good for debugging)
            telemetry.addData("OpMode Status", "Active");

            // Update the telemetry display on the Driver Station
            telemetry.update();

            // Optional: Add a small sleep to reduce CPU load if necessary,
            // though for basic drive, it's usually not critical.
            // sleep(5); // Sleep for 5 milliseconds
        }

        // Send a message when the OpMode stops
        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}