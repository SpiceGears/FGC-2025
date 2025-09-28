package org.firstinspires.ftc.teamcode.CrystalSubsystems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Indexer {

    HardwareMap hardware;
    CRServo leftServo, rightServo;

    public Indexer(OpMode opMode) {
        hardware = opMode.hardwareMap;
    }

    public void init() {
        leftServo = hardware.get(CRServo.class, "leftIndexerServo");
        rightServo = hardware.get(CRServo.class, "rightIndexerServo");

        leftServo.setDirection(Direction.FORWARD);
        rightServo.setDirection(Direction.REVERSE);
    }

    private void spinServos() {
        leftServo.setPower(1);
        rightServo.setPower(1);
    }

    private void stopServos() {
        leftServo.setPower(0);
        rightServo.setPower(0);
    }

    public void handle(boolean run) {
        if(run) spinServos();
        else stopServos();
    }

}
