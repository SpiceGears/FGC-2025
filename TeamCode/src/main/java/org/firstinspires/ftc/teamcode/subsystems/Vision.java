package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.utils.Config;
import org.firstinspires.ftc.teamcode.utils.Constants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.Arrays;
import java.util.List;

import global.first.EcoEquilibriumGameDatabase;
import pl.spicegears.fgc.lib.Logger;
import pl.spicegears.fgc.lib.StatusCode;
import pl.spicegears.fgc.lib.Subsystem;

public class Vision extends Subsystem {
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private final Drivetrain drive;


    private Gamepad copilot_gamepad;

    public final int[] TARGET_APRILTAGS_ID = {100, 102, 104};

    public int lockedAprilTag = 0;
    HardwareMap hardwareMap;
    public Vision(Drivetrain drivetrain) {
        super("Vision");
        this.drive = drivetrain;
    }

    private Logger log;


    ///  assigns gamepad and logger to vision subsystem
    public void assignGamepadLogger(Gamepad gamepad, Logger logger)
    {
        this.copilot_gamepad = gamepad;
        this.log = logger;
    }

    public void init(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        if (drive == null)
        {
            setStatus(StatusCode.NOT_INITIATED, "Drivetrain subsystem is null");
            return;
        }
        if (drive.getStatusCode() != StatusCode.INITIATED.getCode())
        {
            setStatus(StatusCode.NOT_INITIATED, "Drivetrain subsystem was not initiated on Vision.init()");
            return;
        }

        try
        {
            initAprilTag();
            setStatus(StatusCode.INITIATED);
        }
        catch (Exception e)
        {
            setStatus(StatusCode.HARDWARE_NOT_FOUND);
        }
    }

    public List<AprilTagDetection> getDetections()
    {
        return aprilTag.getDetections();
    }
    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()
                .setTagLibrary(EcoEquilibriumGameDatabase.getEcoEquilibriumTagLibrary())
                .setCameraPose(new Position(DistanceUnit.CM,0,0,0,0), new YawPitchRollAngles(AngleUnit.DEGREES, 0,0, 0, 0))
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)

                // ... these parameters are fx, fy, cx, cy.

                .build();
        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();
        // Set the webcam (name is assumed to be "Webcam 1")
        builder.setCamera(hardwareMap.get(WebcamName.class, Config.WEB_CAMERA_NAME));
        // Set and enable the processor.
        builder.addProcessor(aprilTag);
        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();
    }   // end method initAprilTag()





    private boolean detectionLock = false;

    private boolean aligned = true;

    private double tagPoseX = 0;
    private double tagPoseY = 0;

    public void handle(boolean lockButton, double forward, double turn)
    {
        if (this.getStatusCode() == StatusCode.NOT_INITIATED.getCode()) return;

        boolean is_pilot_steering = false;
        if (Math.abs(forward) >= 0.01 || Math.abs(turn) >= 0.01) {is_pilot_steering = true;}

        if (!detectionLock) {
            List<AprilTagDetection> detections = getDetections();
            for (AprilTagDetection detect : detections) {
                if (Arrays.stream(TARGET_APRILTAGS_ID).anyMatch(d -> d == detect.id)) {
                    log.addLine("Vision:", "AprilTags detected");
                    if (copilot_gamepad != null) copilot_gamepad.rumble(50);

                    if (lockButton) {
                        detectionLock = true;
                        aligned = false;

                        lockedAprilTag = detect.id;

                        AprilTagPoseFtc tagpose = detect.ftcPose;
                        tagPoseX = tagpose.x;
                        tagPoseY = tagpose.y;
                        //tagpose.
                        break;
                    }
                }
            }
        }
        else
        {
            log.addLine("VISION LOCKED ONTO: ", lockedAprilTag);
            if (lockButton) {detectionLock = false; lockedAprilTag = 0; return;}
            if (is_pilot_steering) { return; }

            List<AprilTagDetection> detections = getDetections();
            for (AprilTagDetection detect : detections) {
                if ((lockedAprilTag == detect.id)) {
                    log.addLine("Vision: AprilTag still visible", "");
                    aligned = false;
                        AprilTagPoseFtc tagpose = detect.ftcPose;
                        tagPoseX = tagpose.x;
                        tagPoseY = tagpose.y;
                        //tagpose.
                        break;
                }
            }
            autoAlign();
        }

            //detectionLock = false;
    }



    private void autoAlign()
    {
        if (aligned) return;
        //add camera & ecosystem offsets
        tagPoseX = tagPoseX + Constants.CAMERA_OFFSET_X + Constants.ECOSYSTEM_OFFSET_X;
        tagPoseY = tagPoseY - Constants.CAMERA_OFFSET_Y - Constants.ECOSYSTEM_OFFSET_Y;

        double angleToTag_rad = Math.atan2(tagPoseY, tagPoseX);
        double angleToTag_deg = Math.toDegrees(angleToTag_rad);
        angleToTag_deg -= 90;
        angleToTag_deg += Constants.CAMERA_ANGLE_DEG;
        if (Math.abs(angleToTag_deg) <= Constants.CAMERA_ALIGNMENT_DEADZONE_DEG ) {aligned = true; return;}
        log.addLine("Vision: Aligning robot by degrees: ", angleToTag_deg);
        int[] targetTicks = ticksForTurnDegrees(angleToTag_deg); //calculate target encoder positions, index 0 - left, index 1 - right


        int[] currentPos = drive.getCurrentPosition();

        //we do inverse-offsets, since our camera is facing backward, not forward
        currentPos[1] += targetTicks[1];
        currentPos[0] += targetTicks[0];

        //use the drivetrain to rotate the robot to proper position
        drive.driveToPos(currentPos[0], currentPos[1], 0.9);

        aligned = true;
        //we finished our task - let go of the lock
        //detectionLock = false;


    }

    public void telemetry()
    {

    }



    public static int[] ticksForTurnDegrees(double turnDegrees) {
        // constants definition
        double W = Constants.ROBOT_WIDTH_CM;         // cm
        double D = Constants.WHEEL_DIAMETER_CM / 2;     // cm
        double gear = Constants.DRIVETRAIN_GEAR_RATIO; // motor:wheel (15 : 1)
        double ticksPerMotorRev = Constants.TICKS_PER_REVOLUTION; //ticks per rev

        // arc length for each wheel (cm)
        double s = Math.PI * W * Math.abs(turnDegrees) / 360.0;

        // wheel revolutions
        double wheelRevs = s / (Math.PI * D);

        // motor revolutions
        double motorRevs = wheelRevs * gear;

        // encoder ticks (float -> round to nearest long)
        int ticks = Math.toIntExact(Math.round(motorRevs * ticksPerMotorRev));

        // sign assignment: positive turnDegrees = left turn
        if (turnDegrees > 0) {
            // left backwards, right forwards
            return new int[] { -ticks, +ticks };
        } else if (turnDegrees < 0) {
            // left forwards, right backwards
            return new int[] { +ticks, -ticks };
        } else {
            return new int[] { 0, 0 };
        }
    }

}
