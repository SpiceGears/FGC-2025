package org.firstinspires.ftc.teamcode.Subsystems;

import com.arcrobotics.ftclib.drivebase.DifferentialDrive;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;

public class UniversalDiffDrive {
    private DifferentialDrive drive;
    private MotorGroup leftGroup;
    private MotorGroup rightGroup;

    public UniversalDiffDrive(MotorGroup left, MotorGroup right)
    {
        leftGroup = left;
        rightGroup = right;

        drive = new DifferentialDrive(leftGroup, rightGroup);
        drive.setRightSideInverted(true);
    }

    public void drive(double forward, double turn)
    {
        drive.arcadeDrive(forward, turn);
    }
}
