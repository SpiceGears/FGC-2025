package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.Config;
import org.firstinspires.ftc.teamcode.utils.Status;
import org.firstinspires.ftc.teamcode.utils.StatusCode;

public class Drivetrain {

    DcMotor leftDriveMotor;
    DcMotor rightDriveMotor;

    Status subsystemStatus;

    public Drivetrain(OpMode opMode) {
        HardwareMap hardware = opMode.hardwareMap;
        try {
            leftDriveMotor = hardware.get(DcMotor.class, Config.DRIVE_LEFT_MOTOR);
            rightDriveMotor = hardware.get(DcMotor.class, Config.DRIVE_RIGHT_MOTOR);

            leftDriveMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);
            rightDriveMotor.setMode(RunMode.RUN_WITHOUT_ENCODER);

            leftDriveMotor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);
            rightDriveMotor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);

            leftDriveMotor.setDirection(Config.DRIVE_LEFT_REVERSE ? Direction.REVERSE : Direction.FORWARD);
            rightDriveMotor.setDirection(Config.DRIVE_RIGHT_REVERSE ? Direction.REVERSE : Direction.FORWARD);

            subsystemStatus = new Status(StatusCode.INITIATED, "System Drivetrain dziala poprawnie.");

        } catch (Exception e) {
               subsystemStatus = new Status(StatusCode.HARDWARE_NOT_FOUND, "Nie znaleziono sprzetu systemu Drivetrain.");
        }
    }

    double leftPower;
    double rightPower;

    public void drive(double drive, double turn) {

        if(subsystemStatus.getStatusCode().getCode() < 10) return;

        leftPower = Range.clip(drive + turn, -1, 1);
        rightPower = Range.clip(drive - turn, -1, 1);

        leftDriveMotor.setPower(leftPower);
        rightDriveMotor.setPower(rightPower);
    }

    public double getLeftPower() {
        if(subsystemStatus.getStatusCode().getCode() < 10) return 0;
        return leftPower;
    }

    public double getRightPower() {
        if(subsystemStatus.getStatusCode().getCode() < 10) return 0;
        return rightPower;
    }
}
