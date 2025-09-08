package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.Climbing;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;
import org.firstinspires.ftc.teamcode.Subsystems.Vision;
import org.openftc.apriltag.AprilTagDetection;

@TeleOp(name="Crystal", group="Linear Opmode")
@Disabled
public class  CrystalTeleOp extends LinearOpMode {

    private final Drive drivetrain = new Drive(this);
    private final Intake intake = new Intake(this);
    private final Shooter shooter = new Shooter(this);
    private final Climbing climbing = new Climbing(this);
    Vision vision;

    private boolean approachMode = false; // NEW: auto-approach state

    @Override
    public void runOpMode() {
        drivetrain.init();
        //climbing.init();
        shooter.init();
        intake.init();
        vision = new Vision(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "Running");
        telemetry.update();

        while (opModeIsActive()) {
            // Always update vision
            vision.update();
            AprilTagDetection detection = vision.getLastDetection();

            // Always show detection status
            if (detection != null) {
                telemetry.addData("Tag ID", detection.id);
                telemetry.addData("Z Distance", "%.2f m", detection.pose.z);
                telemetry.addData("X Distance", "%.2f m", detection.pose.x);
            } else {
                telemetry.addLine("No tag detected");
            }

            // --- Start approach mode when Triangle is pressed ---
            if (gamepad1.triangle && !approachMode) {
                approachMode = true;
            }

            if (gamepad1.triangle) {
                if (vision.getLastDetection() != null) {
                    double kP = 2;
                        Double angle = vision.getAngleToTag();
                        if (angle == null) continue;

                        double error = angle;
                        double power = kP * error;

                        power = Math.max(-0.6, Math.min(0.6, power));

                        drivetrain.turn(-power, power);

                        //if (Math.abs(error) < 1.0) continue;

                    drivetrain.stop();
                } else {
                    drivetrain.stop();
                    telemetry.addLine("No tag detected");
                }
            } else if (gamepad1.circle) {
                if (vision.getLastDetection() != null) {
                    double forwardError = vision.getForwardDistance() - 0.36;
                        double kPforward = 2;
                        double forwardPower = kPforward * forwardError;
                        forwardPower = Math.max(-0.4, Math.min(0.4, forwardPower));

                        drivetrain.turn(forwardPower, forwardPower);
                    }
            }
            else {
                // Manual drive
                double drive = -gamepad1.left_stick_y;
                double turn = gamepad1.right_stick_x;
                drivetrain.drive(-drive, -turn);
            }

            // Intake controls
            if (gamepad1.right_trigger > 0.1)
                intake.take(1);
            else if (gamepad1.left_trigger > 0.1)
                intake.take(-1);
            else intake.stopTaking();

            if (gamepad1.right_bumper)
                intake.pass(1);
            else if (gamepad1.left_bumper)
                intake.pass(-1);
            else intake.stopPassing();

            // Shooter controls
            if (gamepad1.square)
                shooter.startSpinning(1);
            else if (gamepad1.cross)
                shooter.stopSpinning();

//            // Climbing controls
            if (gamepad1.dpad_up)
                climbing.drive();
         else if (gamepad1.dpad_down)
               climbing.reverse();
          else climbing.stop();

            telemetry.update();

        }
    }
}