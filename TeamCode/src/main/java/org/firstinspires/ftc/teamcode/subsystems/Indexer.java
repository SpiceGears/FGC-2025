package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Config;

import pl.spicegears.fgc.lib.StatusCode;
import pl.spicegears.fgc.lib.Subsystem;

public class Indexer extends Subsystem {
    CRServo leftServo, rightServo;
    HardwareMap hardwareMap;
    public Indexer(HardwareMap hardwareMap) {
        super("Indexer");
        this.hardwareMap = hardwareMap;
    }

    public void init() {
        try {
            leftServo = hardwareMap.get(CRServo.class, Config.INDEXER_LEFT_SERVO);
            rightServo = hardwareMap.get(CRServo.class, Config.INDEXER_RIGHT_SERVO);

            leftServo.setDirection(Config.INDEXER_LEFT_REVERSE ? Direction.REVERSE : Direction.FORWARD);
            rightServo.setDirection(Config.INDEXER_RIGHT_REVERSE ? Direction.REVERSE : Direction.FORWARD);

            setStatus(StatusCode.INITIATED);
        } catch (Exception e) {
            setStatus(StatusCode.HARDWARE_NOT_FOUND);
        }
    }

    private void spinServos() {
        leftServo.setPower(1);
        rightServo.setPower(1);
    }

    private void stopServos() {
        leftServo.setPower(0);
        rightServo.setPower(0);
    }

    public void handle(boolean spin) {
        if(getStatusCode() < 10) return;
        if(spin) spinServos();
        else stopServos();
    }
}
