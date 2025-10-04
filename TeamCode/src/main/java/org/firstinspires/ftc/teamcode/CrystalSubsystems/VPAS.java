package org.firstinspires.ftc.teamcode.CrystalSubsystems;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Arrays;
import java.util.List;

/// # Vision-powered Assist System
public class VPAS {
    private boolean DriveTrainLock = false;

    private LinearOpMode opMode;

    private boolean start = false;

    private Drive drive;
    private VisionEx vision;

    private IMU imu;

    
    
    /// Apriltag we locked in
    private AprilTagDetection detection;

    
    
    /// whether or not we have locked onto an apriltag
    private boolean detectionLock;

    private boolean foundtags;

    /// ## Returns true if VPAS wants to use the drivetrain
    public boolean isDriveTrainLock() {
        return DriveTrainLock;
    }



    public VPAS(Drive drivetrain, VisionEx visionex, LinearOpMode op, IMU imusub)
    {
        this.drive = drivetrain;
        this.vision = visionex;
        this.opMode = op;
        this.imu = imusub;
    }

    public void start() {start = true;}

    public void tick()
    {
        if (!detectionLock) {
            List<AprilTagDetection> detections = vision.getDetections();
            if (!detections.isEmpty()) foundtags = true; else foundtags = false;
            for (AprilTagDetection detect : detections) {
                if (Arrays.stream(vision.TARGET_APRILTAGS_ID).anyMatch(d -> d == detect.id)) {

                    opMode.gamepad2.rumble(50);

                    if (opMode.gamepad2.a) {
                        detectionLock = true;
                        detection = detect;



                        break;
                    }
                }
            }
        }
        else 
        {
            if (opMode.gamepad2.a) {
                detectionLock = false;
                detection = null;
            }

            {
                DriveTrainLock = true;
                if (detection == null) {
                    drive.stop();
                    DriveTrainLock = false;
                } else {
                    if (-(detection.ftcPose.z) >= 80) {
                        drive.drive(-0.3, 0);
                    } else if (-(detection.ftcPose.z) < 70) {
                        drive.drive(0.3, 0);
                    } else {
                        drive.stop();


                    }
                }
                DriveTrainLock = false;
                
            }


        }



    }



    public void telemetry()
    {
        if (detectionLock) {
            opMode.telemetry.addLine("# APRIL TAG LOCKED");
            opMode.telemetry.addData("Robot pose: ", detection.ftcPose);
        }
        else if (foundtags) opMode.telemetry.addLine("# April Tags found");
        else opMode.telemetry.addLine("# No target");


    }
}
