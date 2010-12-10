package com.controlj.green.modstat.checks;

import com.controlj.green.modstat.Modstat;

import java.util.List;

public interface Checker {
    List<ReportRow> check(Modstat modstat);
    String getName();
    String getDescription();
    void setEnabled(boolean enabled);
    boolean isEnabled();
}
