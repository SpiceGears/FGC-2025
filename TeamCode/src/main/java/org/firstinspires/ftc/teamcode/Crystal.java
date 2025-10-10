/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.CrystalSubsystems.Indexer;
import org.firstinspires.ftc.teamcode.CrystalSubsystems.Lance;
import org.firstinspires.ftc.teamcode.CrystalSubsystems.Intake;
import org.firstinspires.ftc.teamcode.CrystalSubsystems.Shooter;
import org.firstinspires.ftc.teamcode.CrystalSubsystems.VPAS;
import org.firstinspires.ftc.teamcode.CrystalSubsystems.VisionEx;
import org.firstinspires.ftc.teamcode.Subsystems.Climbing;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Vision;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Optional;

@TeleOp(name="Crystal", group="Linear OpMode")
public class Crystal extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    private final Drive drive = new Drive(this);
    private final Intake intake = new Intake(this);
    private final Shooter shooter = new Shooter(this);
    private final Climbing climb = new Climbing(this);
    private final Indexer indexer = new Indexer(this);

    private final Lance lance = new Lance(this);

    public IMU imu;

    private VisionEx vision;

    private VPAS vpas;

    @Override
    public void runOpMode() {


        imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters imuParams = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        new Orientation(
                                AxesReference.INTRINSIC,
                                AxesOrder.ZXY,
                                AngleUnit.DEGREES,
                                -90.0f,
                                0.0f,
                                90+23.0f,
                                0
                        )
                )
        );

        imu.initialize(imuParams);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        drive.init();
        intake.init();
        shooter.init();
        climb.init();
        indexer.init();
        lance.init();
        vision = new VisionEx(this);



        vpas = new VPAS(drive, vision, this, imu);

        waitForStart();
        runtime.reset();

        vpas.start();

        while (opModeIsActive()) {

            vpas.tick();

            double forward = gamepad1.left_stick_y;
            double turn  =  -gamepad1.right_stick_x;


            drive.drive(forward, turn);
            intake.handle(
                    (gamepad1.right_trigger > 0.5) || (gamepad2.right_trigger > 0.5),
                    (gamepad1.left_trigger > 0.5)  || (gamepad2.left_trigger > 0.5)
            );
            shooter.handleShooter((gamepad1.x || gamepad2.x), (gamepad1.a || gamepad2.a));
            shooter.handlePasser((gamepad1.right_bumper || gamepad2.right_bumper), (gamepad1.left_bumper || gamepad2.left_bumper));
            climb.handle((gamepad1.dpad_up || gamepad2.dpad_up), (gamepad1.dpad_down || gamepad2.dpad_down));
            indexer.handle(gamepad1.right_bumper || gamepad2.right_bumper);
            lance.handle(gamepad2.dpad_left || gamepad1.dpad_left, gamepad2.dpad_right || gamepad1.dpad_right);





            telemetry.addData("Status", "Run Time: " + runtime.toString());
            vpas.telemetry();
            telemetry.update();
        }

        vision.dispose();
    }
}
