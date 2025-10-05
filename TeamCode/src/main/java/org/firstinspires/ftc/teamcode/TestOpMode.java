package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.ExampleSubsystem;

import pl.spicegears.fgc.lib.Logger;

@TeleOp(name="TestOpMode", group="Testing")
public class TestOpMode extends LinearOpMode {
    Logger log = new Logger(telemetry);
    ExampleSubsystem exampleSubsystem = new ExampleSubsystem(hardwareMap);

    @Override
    public void runOpMode() {

        exampleSubsystem.init();

        log.addStatus(exampleSubsystem);
        log.send();

        waitForStart();

        while(opModeIsActive()) {
            exampleSubsystem.exampleCommand();
        }
    }
}