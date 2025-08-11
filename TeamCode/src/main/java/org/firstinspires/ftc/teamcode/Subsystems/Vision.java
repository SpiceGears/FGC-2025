package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.opencv.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.ArrayList;

public class Vision {

    private OpenCvCamera camera;
    private AprilTagDetectionPipeline pipeline;

    // Camera calibration values (replace with your own)
    private final double fx = 578.272;
    private final double fy = 578.272;
    private final double cx = 402.145;
    private final double cy = 221.506;

    // Tag size in meters
    private final double tagsize = 0.166;

    private AprilTagDetection lastDetection = null;

    public Vision(HardwareMap hardwareMap) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        FtcDashboard.getInstance().startCameraStream(camera, 0);


        pipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
        camera.setPipeline(pipeline);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                // Handle camera error
            }
        });
    }

    /** Updates the latest detection — call this every loop */
    public void update() {
        ArrayList<AprilTagDetection> detections = pipeline.getLatestDetections();
        if (!detections.isEmpty()) {
            lastDetection = detections.get(0); // Take the first detected tag
        } else {
            lastDetection = null; // No tag detected right now
        }
    }

    /** Returns the ID of the last detected tag, or -1 if none */
    public int getLastDetectedTagId() {
        return (lastDetection != null) ? lastDetection.id : -1;
    }

    /** Returns the full AprilTagDetection object */
    public AprilTagDetection getLastDetection() {
        return lastDetection;
    }

    public double getTagBearingDegrees() {
        if (lastDetection != null) {
            double x = lastDetection.pose.x; // m
            double z = lastDetection.pose.z; // m
            double angleRad = Math.atan2(x, z);
            return -Math.toDegrees(angleRad);
        }
        return 0;
    }

    /** Stops the camera stream */
    public void stop() {
        camera.stopStreaming();
    }

    public void turnTowardTag(Drive drive) {
        if (lastDetection != null) {
            double bearingError = getTagBearingDegrees();
            double kP = 0.03;
            double tolerance = 1.0;

            if (Math.abs(bearingError) > tolerance) {
                double turnPower = kP * bearingError;
                drive.drive(0, turnPower);
            } else {
                drive.stop();
            }
        } else {
            drive.stop();
        }
    }
}