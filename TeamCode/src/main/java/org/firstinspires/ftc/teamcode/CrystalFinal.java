package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Climber;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.Logger;

@TeleOp(name="CrystalFinal", group="Crystal")
public class CrystalFinal extends LinearOpMode {
    private final Logger log = new Logger(telemetry);
    private final Drivetrain drive = new Drivetrain(hardwareMap);
    private final Intake intake = new Intake(hardwareMap);
    private final Indexer indexer = new Indexer(hardwareMap);
    private final Shooter shooter = new Shooter(hardwareMap);
    private final Climber climber = new Climber(hardwareMap);

    @Override
    public void runOpMode() {

        drive.init();
        intake.init();
        indexer.init();
        shooter.init();
        climber.init();

        logSubsystemsStatus();
        log.send();

        waitForStart();

        while(opModeIsActive()) {

            drive.drive(gamepad1.left_stick_y, -gamepad1.right_stick_x);

            intake.handle(gamepad1.right_trigger, gamepad1.left_trigger);

            indexer.handle(gamepad1.right_bumper || gamepad2.right_bumper);

            shooter.handleShooter((gamepad1.x || gamepad2.x), (gamepad1.a || gamepad2.a));

            shooter.handlePass((gamepad1.right_bumper || gamepad2.right_bumper), (gamepad1.left_bumper || gamepad2.left_bumper));

            climber.handle((gamepad1.dpad_up || gamepad2.dpad_up), (gamepad1.dpad_down || gamepad2.dpad_down));

            if(Config.DEBUG) {
                logSubsystemsStatus();
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
}