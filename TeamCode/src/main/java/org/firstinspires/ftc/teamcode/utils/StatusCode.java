package org.firstinspires.ftc.teamcode.utils;

public enum StatusCode {
    NOT_INITIATED(0),
    HARDWARE_NOT_FOUND(4),
    INITIATED(10);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
