package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Climbing;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Gate;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.utils.MatchState;

@TeleOp(name="Dziamdziak", group="Linear Opmode")
public class Dziamdziak extends LinearOpMode {
    private final Drive drivetrain = new Drive(this);
    private final Intake intake = new Intake(this);
    private final Gate gate = new Gate(this);
    private final Climbing climbing = new Climbing(this);
    private final ElapsedTime runtime = new ElapsedTime();
    private MatchState state = MatchState.IDLE;

    @Override
    public void runOpMode() {
        drivetrain.init();
        intake.init();
        gate.init();
        climbing.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //gamepad1.rumble(1, 1, Gamepad.RUMBLE_DURATION_CONTINUOUS);
        gamepad1.rumble(1500);

        waitForStart();

        telemetry.addData("Status", "Running");
        telemetry.update();

        while(opModeIsActive()) {
            double drive = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;

            drivetrain.drive(drive, turn);

            gate.handle(gamepad1.dpad_up, gamepad1.dpad_down);

            intake.handle(gamepad1.right_trigger > 0.5, gamepad1.left_trigger > 0.5);

            climbing.handle(gamepad1.right_bumper, gamepad1.left_bumper);

            if(gamepad1.ps && state == MatchState.IDLE) {
                state = MatchState.STARTED;
                runtime.reset();
                gamepad1.rumble(1.0, 1.0, 1000);
            }

            if(state == MatchState.STARTED && runtime.seconds() >= 120) {
                state = MatchState.ENDGAME;
                gamepad1.rumble(1, 1, 500);
            }

            if(state == MatchState.ENDGAME && runtime.seconds() >= 150) {
                state = MatchState.IDLE;
                gamepad1.rumble(1, 1, 2000);
            }


            telemetry.addData("Gamepad Input", "-----");
            telemetry.addData("Drive (Left Stick Y)", "%.2f", drive);
            telemetry.addData("Turn (Right Stick X)", "%.2f", turn);

            telemetry.addData("Motor Powers (Raw)", "---");
            telemetry.addData("Left Power", "%.2f", drivetrain.getLeftPower());
            telemetry.addData("Right Power", "%.2f", drivetrain.getRightPower());

            telemetry.addData("OpMode Status", "Active");
            telemetry.addData("Match State", state.toString());
            telemetry.addData("Match Time", runtime.seconds());

            telemetry.update();
        }

        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}