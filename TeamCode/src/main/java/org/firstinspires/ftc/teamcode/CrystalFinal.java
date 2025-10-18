package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Climber;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.Logger;

@TeleOp(name="CrystalFinal", group="Crystal")
public class CrystalFinal extends LinearOpMode {
    private final Logger log = new Logger(telemetry);
    private final Drivetrain drive = new Drivetrain();
    private final Intake intake = new Intake();
    private final Indexer indexer = new Indexer();
    private final Shooter shooter = new Shooter();
    private final Climber climber = new Climber();

    @Override
    public void runOpMode() {

        drive.init(hardwareMap);
        intake.init(hardwareMap);
        indexer.init(hardwareMap);
        shooter.init(hardwareMap);
        climber.init(hardwareMap);

        vision.init();

        logSubsystemsStatus();
        log.send();

        waitForStart();

        while(opModeIsActive()) {

            drive.drive(gamepad1.left_stick_y, -gamepad1.right_stick_x);

            intake.handle(gamepad1.right_trigger, gamepad1.left_trigger, gamepad2.right_trigger, gamepad2.left_trigger) ;

            indexer.handle(gamepad1.right_bumper || gamepad2.right_bumper, gamepad1.left_bumper || gamepad2.left_bumper);

            shooter.handleShooter((gamepad1.x || gamepad2.x), (gamepad1.a || gamepad2.a));

            shooter.handlePass((gamepad1.right_bumper || gamepad2.right_bumper), (gamepad1.left_bumper || gamepad2.left_bumper));

            climber.handleClimber((gamepad1.dpad_up || gamepad2.dpad_up), (gamepad1.dpad_down || gamepad2.dpad_down));

            climber.handleLock(gamepad2.dpad_right, gamepad2.dpad_left);

            vision.handle(gamepad2.y || gamepad1.y, gamepad1.left_stick_y, -gamepad1.right_stick_x);

            if(Config.DEBUG) {
                logSubsystemsStatus();
                logShooter();
            }
            log.send();

        }
    }

    void logSubsystemsStatus() {
        log.addStatus(drive);
        log.addStatus(intake);
        log.addStatus(indexer);
        log.addStatus(shooter);
        log.addStatus(climber);
    }
    boolean blipped = false;
    double threshold = 1700;
    void logShooter() {
        double leftVel = shooter.getVelocities()[0];
        double rightVel = shooter.getVelocities()[1];
        log.addLine("Left Velocity", shooter.getVelocities()[0]);
        log.addLine("Right Velocity", shooter.getVelocities()[1]);

        if(blipped && (leftVel < threshold || rightVel < threshold)) {
            blipped = false;
        }
        if(!blipped && (leftVel > threshold || rightVel > threshold)) {
            gamepad2.rumbleBlips(1);
            blipped = true;
        }
    }
}

