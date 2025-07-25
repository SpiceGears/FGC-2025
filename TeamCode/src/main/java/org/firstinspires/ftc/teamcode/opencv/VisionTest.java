/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.opencv; // Ensure this package matches your file structure

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.opencv.AprilTagDetectionPipeline; // Ensure this import is correct
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@TeleOp
public class VisionTest extends LinearOpMode {
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int ID_TAG_OF_INTEREST = 18; // Tag ID 18 from the 36h11 family

    // This variable will store the last seen tag of interest
    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()
        );
        camera = OpenCvCameraFactory.getInstance().createWebcam(
                hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId
        );
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(
                tagsize, fx, fy, cx, cy
        );

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Camera Error", errorCode);
                telemetry.update();
            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * We'll keep a simplified version here to show camera status
         * and potentially an initial detection if desired.
         */
        while (!isStarted() && !isStopRequested()) {
            ArrayList<AprilTagDetection> currentDetections =
                    aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0) {
                boolean tagFoundInInit = false; // Use a local variable for init loop
                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == ID_TAG_OF_INTEREST) {
                        tagOfInterest = tag; // Store it for later use if we start
                        tagFoundInInit = true;
                        break;
                    }
                }
                if (tagFoundInInit) {
                    telemetry.addLine("Tag of interest is IN SIGHT! Ready to start.");
                    tagToTelemetry(tagOfInterest);
                } else {
                    telemetry.addLine("Tag of interest NOT in sight in init.");
                    if (tagOfInterest == null) {
                        telemetry.addLine("(Tag never seen yet)");
                    } else {
                        telemetry.addLine("Last seen in init (if any):");
                        tagToTelemetry(tagOfInterest);
                    }
                }
            } else {
                telemetry.addLine("No tags detected in init loop.");
                if (tagOfInterest == null) {
                    telemetry.addLine("(Tag never seen yet)");
                } else {
                    telemetry.addLine("Last seen in init (if any):");
                    tagToTelemetry(tagOfInterest);
                }
            }
            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in:
         * Now, we enter the main execution loop (opModeIsActive()).
         */

        // After start, we might want to log the last known tag (if found in init)
        // or just say we're starting.
        if (tagOfInterest != null) {
            telemetry.addLine("OpMode started. Initial tag snapshot:");
            tagToTelemetry(tagOfInterest);
        } else {
            telemetry.addLine("OpMode started. No tag was sighted during init.");
        }
        telemetry.update();
        sleep(100); // Give telemetry a moment to update

        /*
         * Main OpMode loop:
         * This is where you put your continuous actions, including
         * constantly checking for AprilTag detections.
         */
        while (opModeIsActive()) {
            // Get the latest detections from the pipeline
            ArrayList<AprilTagDetection> currentDetections =
                    aprilTagDetectionPipeline.getLatestDetections();

            boolean tagCurrentlyDetected = false;
            if (currentDetections.size() != 0) {
                // Look for our tag of interest among the current detections
                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == ID_TAG_OF_INTEREST) {
                        tagOfInterest = tag; // Update tagOfInterest with the latest data
                        tagCurrentlyDetected = true;
                        break; // Found our tag, no need to check others
                    }
                }
            }

            // Update telemetry based on current detection status
            if (tagCurrentlyDetected) {
                telemetry.addLine("Tag of interest IS currently in sight!");
                tagToTelemetry(tagOfInterest); // Show details of the currently detected tag
                // **** Autonomous/TeleOp Action based on tagOfInterest here ****
                // Example: If the tag is seen, make decisions
                // if (tagOfInterest.pose.x > 0.5) {
                //     // Robot is too far right of the tag
                //     // robot.turnLeft(0.1);
                // } else if (tagOfInterest.pose.x < -0.5) {
                //     // Robot is too far left of the tag
                //     // robot.turnRight(0.1);
                // }
                // ... and so on for y, z, rotations
            } else {
                telemetry.addLine("Tag of interest NOT currently in sight.");
                if (tagOfInterest != null) {
                    telemetry.addLine("\nLast seen (snapshot from previous detection):");
                    tagToTelemetry(tagOfInterest); // Show the last known data
                } else {
                    telemetry.addLine("(Tag has never been seen)");
                }
            }

            // You would typically add your robot control logic here as well
            // e.g., drive commands, arm control, etc.
            // robot.handleDrive();
            // robot.handleArm();

            telemetry.update();
            sleep(20); // Keep loop frequency reasonable
        }

        // After the opModeIsActive() loop ends (e.g., STOP button pressed), stop camera
        camera.stopStreaming();
        camera.closeCameraDevice();
        telemetry.addLine("OpMode ended. Camera stopped.");
        telemetry.update();
    }

    void tagToTelemetry(AprilTagDetection detection) {
        Orientation rot = Orientation.getOrientation(
                detection.pose.R,
                AxesReference.INTRINSIC,
                AxesOrder.YXZ,
                AngleUnit.DEGREES
        );

        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(
                String.format("Translation X: %.2f feet", detection.pose.x * FEET_PER_METER)
        );
        telemetry.addLine(
                String.format("Translation Y: %.2f feet", detection.pose.y * FEET_PER_METER)
        );
        telemetry.addLine(
                String.format("Translation Z: %.2f feet", detection.pose.z * FEET_PER_METER)
        );
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", rot.firstAngle));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", rot.secondAngle));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", rot.thirdAngle));
    }
}