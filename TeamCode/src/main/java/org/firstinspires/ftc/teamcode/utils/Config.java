package org.firstinspires.ftc.teamcode.utils;
@com.acmerobotics.dashboard.config.Config
public class Config {
    //DRIVE
    public static String DRIVE_LEFT_MOTOR = "leftDrive";
    public static boolean DRIVE_LEFT_REVERSE = true;
    public static String DRIVE_RIGHT_MOTOR = "rightDrive";
    public static boolean DRIVE_RIGHT_REVERSE = false;

    //INTAKE
    public static String INTAKE_MOTOR = "intake";
    public static boolean INTAKE_REVERSE = true;

    //INDEXER
    public static String INDEXER_LEFT_SERVO = "leftIndexerServo";
    public static boolean INDEXER_LEFT_REVERSE = false;
    public static String INDEXER_RIGHT_SERVO = "rightIndexerServo";
    public static boolean INDEXER_RIGHT_REVERSE = true;

    //SHOOTER
    public static String SHOOTER_LEFT_MOTOR = "leftShooter";
    public static boolean SHOOTER_LEFT_REVERSE = false;
    public static String SHOOTER_RIGHT_MOTOR = "rightShooter";
    public static boolean SHOOTER_RIGHT_REVERSE = true;
    public static String PASS_MOTOR = "passShooter";
    public static boolean PASS_REVERSE = false;

    //CLIMBER
    public static String CLIMBER_LEFT_MOTOR = "climbLeft";
    public static boolean CLIMBER_LEFT_REVERSE = true;
    public static String CLIMBER_RIGHT_MOTOR = "climbRight";
    public static boolean CLIMBER_RIGHT_REVERSE = false;

    public static String LOCK_LEFT_SERVO = "leftLock";
    public static String LOCK_RIGHT_SERVO = "rightLock";
    public static double UNLOCK_POS = 0.5;
    public static double LOCK_POS = 1.0;
    public static String WEB_CAMERA_NAME = "Webcam 1";


    public static boolean DEBUG = true;
    public static double TRIGGER_THRESHOLD = 0.5;
}
