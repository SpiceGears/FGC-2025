package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class Climbing {
    private final LinearOpMode opMode;
    private DcMotor leftClimb;
    private DcMotor rightClimb;
    private CRServo leftServo;
    private CRServo rightServo;

    private double leftPower;
    private double rightPower;

    public enum ServoState {
        STOPPED,
        EXTENDED_IDLE,
        RETRACTED_IDLE,
        EXTENDING,
        RETRACTING
    }
    private ServoState currentServoState = ServoState.STOPPED;
    private ElapsedTime servoTimer = new ElapsedTime();

    public Climbing(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void init() {
        leftClimb = opMode.hardwareMap.get(DcMotor.class, "leftClimb");
        rightClimb = opMode.hardwareMap.get(DcMotor.class, "rightClimb");
        leftServo = opMode.hardwareMap.get(CRServo.class, "leftServo");
        rightServo = opMode.hardwareMap.get(CRServo.class, "rightServo");

        leftClimb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightClimb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftClimb.setDirection(DcMotorSimple.Direction.REVERSE);
        rightClimb.setDirection(DcMotorSimple.Direction.FORWARD);
        leftServo.setDirection(DcMotorSimple.Direction.FORWARD);
        rightServo.setDirection(DcMotorSimple.Direction.REVERSE);

        leftClimb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightClimb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        currentServoState = ServoState.RETRACTED_IDLE;
    }

    public ServoState getCurrentServoState() {
        return currentServoState;
    }

    public void startExtending() {
        if (currentServoState != ServoState.EXTENDING && currentServoState != ServoState.RETRACTING) {
            leftServo.setPower(-1);
            rightServo.setPower(-1);
            servoTimer.reset();
            currentServoState = ServoState.EXTENDING;
            opMode.telemetry.addData("Servo Action", "Starting EXTENDING");
        }
    }

    public void startRetracting() {
        if (currentServoState != ServoState.EXTENDING && currentServoState != ServoState.RETRACTING) {
            leftServo.setPower(1);
            rightServo.setPower(1);
            servoTimer.reset();
            currentServoState = ServoState.RETRACTING;
            opMode.telemetry.addData("Servo Action", "Starting RETRACTING");
        }
    }

    public void updateServoMechanism() {
        if (currentServoState == ServoState.EXTENDING) {
            if (servoTimer.seconds() >= 6.5) {
                leftServo.setPower(0);
                rightServo.setPower(0);
                currentServoState = ServoState.EXTENDED_IDLE; // Now in the extended state
                opMode.telemetry.addData("Servo State", "EXTENDED & IDLE");
            }
        } else if (currentServoState == ServoState.RETRACTING) {
            if (servoTimer.seconds() >= 6.5) {
                leftServo.setPower(0);
                rightServo.setPower(0);
                currentServoState = ServoState.RETRACTED_IDLE; // Now in the retracted state
                opMode.telemetry.addData("Servo State", "RETRACTED & IDLE");
            }
        }
        if (currentServoState == ServoState.EXTENDED_IDLE || currentServoState == ServoState.RETRACTED_IDLE || currentServoState == ServoState.STOPPED) {
            leftServo.setPower(0);
            rightServo.setPower(0);
        }

        opMode.telemetry.addData("Current Servo Time", servoTimer.seconds());
        opMode.telemetry.addData("Current Servo State", currentServoState.toString());
    }

    public void stopServos() {
        leftServo.setPower(0);
        rightServo.setPower(0);
    }

    public void drive() {
        leftClimb.setPower(1);
        rightClimb.setPower(1);
    }

    public void reverse() {
        leftClimb.setPower(-1);
        rightClimb.setPower(-1);
    }

    public void stop() {
        rightClimb.setPower(0);
        leftClimb.setPower(0);
    }
}