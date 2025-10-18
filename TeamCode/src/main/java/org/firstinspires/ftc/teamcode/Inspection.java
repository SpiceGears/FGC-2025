package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.subsystems.Climber;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import pl.spicegears.fgc.lib.Logger;

@TeleOp(name = "Inspection", group = "Crystal")
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

    private Screen screen = Screen.MENU;
    private int index = 0;
    private char[] arr = {' ', ' ', ' ', ' ', ' '};

    private void set(int idx) {
        for (int i = 0; i < arr.length; i++) arr[i] = ' ';
        arr[idx] = '<';
    }

    private void logMenu() {
        telemetry.addData("DRIVE   ", arr[0]);
        telemetry.addData("INTAKE  ", arr[1]);
        telemetry.addData("INDEXER ", arr[2]);
        telemetry.addData("SHOOTER ", arr[3]);
        telemetry.addData("CLIMBER ", arr[4]);
        telemetry.addLine("Press ► to inspect selected subsystem");
    }

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

        while (opModeIsActive()) {
            telemetry.clear();

            switch (screen) {
                case MENU:
                    if (gamepad1.dpad_down) {
                        index = Range.clip(index + 1, 0, 4);
                        sleep(150);
                    } else if (gamepad1.dpad_up) {
                        index = Range.clip(index - 1, 0, 4);
                        sleep(150);
                    } else if (gamepad1.dpad_right) {
                        screen = Screen.values()[index + 1];
                        sleep(200);
                    }

                    set(index);
                    logMenu();
                    break;

                case DRIVE:
                    drive.drive(gamepad1.left_stick_y, -gamepad1.right_stick_x);
                    telemetry.addLine("DRIVE MODE (◄ to return)");
                    if (gamepad1.a) drive.drive(1,0);
                    else drive.drive(0, 0);
                    if (gamepad1.dpad_left) {
                        screen = Screen.MENU;
                        sleep(200);
                    }
                    break;

                case INTAKE:
                    telemetry.addLine("INTAKE MODE (◄ to return)");
                    if (gamepad1.a) intake.startMotor();
                    else if (gamepad1.b) intake.reverseMotor();
                    else intake.stopMotor();

                    if (gamepad1.dpad_left) {
                        screen = Screen.MENU;
                        sleep(200);
                    }
                    break;

                case INDEXER:
                    telemetry.addLine("INDEXER MODE (◄ to return)");
                    if (gamepad1.a) indexer.spinServos();
                    else if (gamepad1.b) indexer.spinServosReverse();
                    else indexer.stopServos();

                    if (gamepad1.dpad_left) {
                        screen = Screen.MENU;
                        sleep(200);
                    }
                    break;

                case SHOOTER:
                    telemetry.addLine("SHOOTER MODE (◄ to return)");
                    if (gamepad1.a) shooter.startShooterMotors();
                    else if (gamepad1.b) shooter.stopShooterMotors();
                    else if (gamepad1.x) shooter.startPassMotors();
                    else if (gamepad1.y) shooter.stopPassMotors();

                    if (gamepad1.dpad_left) {
                        screen = Screen.MENU;
                        sleep(200);
                    }
                    break;

                case CLIMBER:
                    telemetry.addLine("CLIMBER MODE (◄ to return)");
                    if (gamepad1.a) climber.startMotors();
                    else if (gamepad1.b) climber.reverseMotors();
                    else climber.stopMotors();

                    if (gamepad1.dpad_left) {
                        screen = Screen.MENU;
                        sleep(200);
                    }
                    break;
            }

            telemetry.update();
        }
    }
}