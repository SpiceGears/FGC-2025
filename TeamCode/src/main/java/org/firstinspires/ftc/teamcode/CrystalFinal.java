package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.Logger;

@TeleOp(name="CrystalFinal", group="Crystal")
public class CrystalFinal extends LinearOpMode {
    Logger log = new Logger(telemetry);
    private final Drivetrain drivetrain = new Drivetrain(hardwareMap);
    private final Intake intake = new Intake(hardwareMap);

    @Override
    public void runOpMode() {

        drivetrain.init();
        intake.init();

        log.addStatus(drivetrain);
        log.addStatus(intake);
        log.send();

        waitForStart();

        while(opModeIsActive()) {

            drivetrain.drive(gamepad1.left_stick_y, -gamepad1.right_stick_x);

            intake.handle(gamepad1.right_trigger, gamepad1.left_trigger);



            if(Config.DEBUG) {
                log.addStatus(drivetrain);
                log.addStatus(intake);
            }

        }
    }
}