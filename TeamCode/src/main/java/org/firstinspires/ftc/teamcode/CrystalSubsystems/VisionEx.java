package org.firstinspires.ftc.teamcode.CrystalSubsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

import global.first.EcoEquilibriumGameDatabase;


/// # Vision subsystem
public class VisionEx {
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    public final int[] TARGET_APRILTAGS_ID = {100, 102, 104};

    public VisionEx(LinearOpMode opMode)
    {
        initAprilTag(opMode);
    }

    public List<AprilTagDetection> getDetections()
    {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        return currentDetections;
    }








    /// Call the method to cleanup and save CPU and memory.
    /// Closes the vision portal video stream.
    public void dispose()
    {
        visionPortal.close();
    }



    private void initAprilTag(LinearOpMode opMode) {

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
        builder.setCamera(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"));

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }   // end method initAprilTag()

}
