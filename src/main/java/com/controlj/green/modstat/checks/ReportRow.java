package com.controlj.green.modstat.checks;

import org.jetbrains.annotations.NotNull;

public class ReportRow {
    public enum Level { ERROR, WARNING, INFO }

    private Level level;
    String message;

    public ReportRow(@NotNull Level level, @NotNull String message) {
        this.level = level;
        this.message = message;
    }

    @NotNull
    public static ReportRow error(String message) {
        return new ReportRow(Level.ERROR, message);
    }

    @NotNull
    public static ReportRow warning(String message) {
        return new ReportRow(Level.WARNING, message);
    }

    @NotNull
    public static ReportRow info(String message) {
        return new ReportRow(Level.INFO, message);
    }

    @NotNull
    public Level getLevel() {
        return level;
    }

    @NotNull
    public String getMessage() {
        return message;
    }
}
