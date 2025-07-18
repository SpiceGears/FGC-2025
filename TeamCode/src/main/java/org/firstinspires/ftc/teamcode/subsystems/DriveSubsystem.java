package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveSubsystem {

    private final LinearOpMode opMode;

    // Four drive motors
    private DcMotor rearLeft = null;
    private DcMotor rearRight = null;
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;

    // 0 = all motors, 1 = front only, 2 = rear only
    private int driveMode = 0;

    public DriveSubsystem(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    // Initialize motors from hardware map
    public void init() {
        try {
            rearLeft = opMode.hardwareMap.get(DcMotor.class, "leftBackDrive");
            rearRight = opMode.hardwareMap.get(DcMotor.class, "rightBackDrive");
            frontLeft = opMode.hardwareMap.get(DcMotor.class, "leftFrontDrive");
            frontRight = opMode.hardwareMap.get(DcMotor.class, "rightFrontDrive");

            // Set motor directions so the robot drives forward properly
            rearLeft.setDirection(DcMotor.Direction.FORWARD);
            rearRight.setDirection(DcMotor.Direction.REVERSE);
            frontLeft.setDirection(DcMotor.Direction.FORWARD);
            frontRight.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception e) {
            // Don't crash if one or more motors are missing
        }
    }

    // Set drive mode (0 = all, 1 = front, 2 = rear)
    public void setDriveMode(int mode) {
        if (mode < 0) mode = 2;
        if (mode > 2) mode = 0;
        this.driveMode = mode;
    }

    // Get current drive mode
    public int getDriveMode() {
        return driveMode;
    }

    // Check if all motors are working
    public boolean checkMotors() {
        return isOk(rearLeft) && isOk(rearRight) && isOk(frontLeft) && isOk(frontRight);
    }

    // Return text status of each motor
    public String getMotorStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("rearLeft: ").append(isOk(rearLeft) ? "OK" : "MISSING").append("\n");
        sb.append("rearRight: ").append(isOk(rearRight) ? "OK" : "MISSING").append("\n");
        sb.append("frontLeft: ").append(isOk(frontLeft) ? "OK" : "MISSING").append("\n");
        sb.append("frontRight: ").append(isOk(frontRight) ? "OK" : "MISSING");

        return sb.toString();
    }

    // Return true if a motor exists and doesn't throw errors
    private boolean isOk(DcMotor motor) {
        try {
            if (motor == null) return false;
            motor.setPower(0); // test command
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Drive control logic (arcade drive)
     * @param drive - forward/backward input (e.g. left stick Y)
     * @param turn - turning input (e.g. right stick X)
     * @param fullSpeed - true = 100% power, false = 50%
     */
    public void drive(double drive, double turn, boolean fullSpeed) {
        // Calculate motor power
        double leftPower = drive + turn;
        double rightPower = drive - turn;

        // Normalize if over 1.0
        double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (max > 1.0) {
            leftPower /= max;
            rightPower /= max;
        }

        // Scale down if not full speed
        double limit = fullSpeed ? 1.0 : 0.5;
        leftPower *= limit;
        rightPower *= limit;

        // Return if any motor is missing
        if (rearLeft == null || rearRight == null || frontLeft == null || frontRight == null) return;

        // Apply power depending on drive mode
        switch (driveMode) {
            case 0: // all motors
                rearLeft.setPower(leftPower);
                rearRight.setPower(rightPower);
                frontLeft.setPower(leftPower);
                frontRight.setPower(rightPower);
                break;
            case 1: // front only
                frontLeft.setPower(leftPower);
                frontRight.setPower(rightPower);
                rearLeft.setPower(0);
                rearRight.setPower(0);
                break;
            case 2: // rear only
                rearLeft.setPower(leftPower);
                rearRight.setPower(rightPower);
                frontLeft.setPower(0);
                frontRight.setPower(0);
                break;
        }
    }
}