package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.subsystems.Climber;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import pl.spicegears.fgc.lib.Logger;

@TeleOp(name="Inspection", group="Crystal")
public class Inspection extends LinearOpMode {

    private final Logger log = new Logger(telemetry);
    private final Drivetrain drive = new Drivetrain();
    private final Intake intake = new Intake();
    private final Indexer indexer = new Indexer();
    private final Shooter shooter = new Shooter();
    private final Climber climber = new Climber();

    enum Screen {
        MENU,
        DRIVE,
        INTAKE,
        INDEXER,
        SHOOTER,
        CLIMBER
    }
    char[] arr = {' ', ' ', ' ', ' ', ' '};
    private void set(int index) {
        for(int i = 0; i < 5; i++) arr[i] = ' ';
        arr[index] = '<';
    }
    private void logMenu() {
        telemetry.addData("DRIVE    ", arr[0]);
        telemetry.addData("INTAKE   ", arr[1]);
        telemetry.addData("INDEXER ", arr[2]);
        telemetry.addData("SHOOTER", arr[3]);
        telemetry.addData("CLIMBER ", arr[4]);
    }

    int index = 0;

    @Override
    public void runOpMode() {
        drive.init(hardwareMap);
        intake.init(hardwareMap);
        indexer.init(hardwareMap);
        shooter.init(hardwareMap);
        climber.init(hardwareMap);

        set(index);
        logMenu();

        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            if(gamepad1.dpad_down) {
                index++;
            }
            if(gamepad1.dpad_up) {
                index--;
            }
            telemetry.addData("", index);
            telemetry.update();
            //index = Range.clip(index, 0, 4);

//            set(index);
//            logMenu();
//            telemetry.update();
        }
    }
}
