package com.controlj.green.modstat.section;

/**
 * Stop parsing after this.
 */
public class StopParseException extends Exception {
    StopParseException(String msg) {
        super(msg);
    }

    StopParseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
