package org.firstinspires.ftc.teamcode.CrystalSubsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lock {
    HardwareMap hardware;
    OpMode opMode;
    Servo leftServo, rightServo;
    public static double openPosition = 0.5;
    public static double closePosition = 1;
    public Lock(OpMode opMode){
        this.opMode = opMode;
        hardware = opMode.hardwareMap;
    }

    public void init() {

        leftServo = opMode.hardwareMap.get(Servo.class, "leftLock");
        rightServo = opMode.hardwareMap.get(Servo.class, "rightLock");
    }

    private void openLance() {

        leftServo.setPosition(0.5);
        rightServo.setPosition(0.5);
    }

    private void closeLance() {

        leftServo.setPosition(1);
        rightServo.setPosition(1);
    }

    public void handle(boolean dpadLeft, boolean dpadRight) {
        if (dpadRight) {
            openLance();
        } else if (dpadLeft) {
            closeLance();
        }
    }
}
