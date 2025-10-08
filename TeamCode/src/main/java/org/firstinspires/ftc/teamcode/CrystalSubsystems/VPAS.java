package org.firstinspires.ftc.teamcode.CrystalSubsystems;


import androidx.core.math.MathUtils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;

import java.util.Arrays;
import java.util.List;

/// # Vision-powered Assist System
public class VPAS {
    private boolean DriveTrainLock = false;

    private LinearOpMode opMode;

    private boolean start = false;

    private final double DISTANCE_CM = 100;
    private final double WHEEL_DIAMETER_CM = 9.0;

    private final double ROBOT_WIDTH_CM = 48.0;

    /// ## Ticks per revolution, 28 t/rev for Ultraplanetary
    private final double TICKS_PER_REVOLUTION = 28;

    /// ## Gearbox ratio
    private final double DRIVETRAIN_GEARBOX = 15.0/1.0;

    // In centimeters
    private final double cameraOffsetX = 15.0;  // Right of center
    private final double cameraOffsetY = -42.0;  // Behind center

    private Drive drive;
    private VisionEx vision;

    private IMU imu;

    
    
    /// Apriltag we locked in
    private AprilTagDetection detection;

    private AprilTagPoseFtc tagpose;
    private double tagPoseX;
    private double tagPoseY;

    
    
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

                        tagpose = detect.ftcPose;
                        tagPoseX = tagpose.x;
                        tagPoseY = tagpose.y;




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
                tagpose = null;

                DcMotor[] motors = drive.getMotors();
                DcMotor leftMotor = motors[0];
                DcMotor rightMotor = motors[1];
                leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }

            {
                DriveTrainLock = true;
                driveToPos();



                DriveTrainLock = false;

            }


        }



    }



    public void telemetry()
    {
        if (detectionLock) {
            opMode.telemetry.addLine("# APRIL TAG LOCKED");
            opMode.telemetry.addData("Tag X: ", tagPoseX);
            opMode.telemetry.addData("Tag Y: ", tagPoseY);
        }
        else if (foundtags) opMode.telemetry.addLine("# April Tags found");
        else opMode.telemetry.addLine("# No target");


    }






    private void driveToPos()
    {
        tagPoseX = tagPoseX + cameraOffsetX;
        tagPoseY = tagPoseY + cameraOffsetY;

        double wheelCircumference = Math.PI * WHEEL_DIAMETER_CM;
        double deltaX = Math.abs(tagPoseX) - DISTANCE_CM;
        double deltaY = tagPoseY;

        double moveDistance = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
        double moveHeading = Math.atan2(tagPoseX, tagPoseY);

        moveHeading = MathUtils.clamp(moveHeading, -Math.PI, Math.PI);

        /// Calc ticks per rev. when gearbox ratio is n
        double ticksPerRevViaRatio = TICKS_PER_REVOLUTION * DRIVETRAIN_GEARBOX;

        double wheelArc = moveHeading * (ROBOT_WIDTH_CM / 2.0);
        double wheelRevsRotate = wheelArc / wheelCircumference;
        int ticksRotate = (int) Math.round(wheelRevsRotate * (ticksPerRevViaRatio/2));

        double wheelRevsForward = moveDistance / wheelCircumference;
        int ticksForward = (int) Math.round(wheelRevsForward * (ticksPerRevViaRatio/2));

        DcMotor[] motors = drive.getMotors();
        DcMotor leftMotor = motors[0];
        DcMotor rightMotor = motors[1];

//        int currentTicks_Left = leftMotor.getCurrentPosition();
//        int currentTicks_Right = rightMotor.getCurrentPosition();

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setTargetPosition(-ticksRotate);
        rightMotor.setTargetPosition(ticksRotate);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(0.3);
        rightMotor.setPower(0.3);

        while (leftMotor.isBusy() || rightMotor.isBusy())
        {
         opMode.telemetry.addLine("ACTIVE ROTATION ASSIST");
         opMode.telemetry.addData("Current Position:", leftMotor.getCurrentPosition());
         opMode.telemetry.addData("Target: ", leftMotor.getTargetPosition());
         opMode.telemetry.update();
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//        currentTicks_Left = leftMotor.getCurrentPosition();
//        currentTicks_Right = rightMotor.getCurrentPosition();

        leftMotor.setTargetPosition(ticksForward);
        rightMotor.setTargetPosition(ticksForward);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(0.3);
        rightMotor.setPower(0.3);

        while (leftMotor.isBusy() || rightMotor.isBusy())
        {
            opMode.telemetry.addLine("ACTIVE DISTANCE ASSIST");
            double distance_to_target = Math.abs(leftMotor.getTargetPosition() - leftMotor.getCurrentPosition());
            opMode.telemetry.addData("Distance: ", distance_to_target);
            opMode.telemetry.addData("Current Position:", leftMotor.getCurrentPosition());
            opMode.telemetry.addData("Target: ", leftMotor.getTargetPosition());
            opMode.telemetry.update();
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
