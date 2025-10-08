package org.firstinspires.ftc.teamcode.CrystalSubsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lance {
    HardwareMap hardware;
    OpMode opMode;
    Servo LanceServo;

    public static double closePosition = 0.2;
    public static double openPosition = -1;

    public static double closeRightPosition = 0.5;
    public Lance(OpMode opMode){
        this.opMode = opMode;
        hardware = opMode.hardwareMap;
    }

    public void init() {
        LanceServo = opMode.hardwareMap.get(Servo.class, "LanceServo");
    }

    private void openLance() {
        LanceServo.setPosition(openPosition);
    }

    private void closeLance() {
        LanceServo.setPosition(closePosition);
    }

    public void handle(boolean dpadLeft, boolean dpadRight) {
        if (dpadRight) {
            openLance();
        } else if (dpadLeft) {
            closeLance();
        }
    }
}
