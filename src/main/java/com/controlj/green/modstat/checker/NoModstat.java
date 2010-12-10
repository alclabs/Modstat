package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;

public class NoModstat extends BaseChecker {
    public NoModstat() {
        name = "No Communications";
        description = "Lists modules that would not respond to modstat request.";
    }

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.isErrorReading()) {
            result = new ArrayList<ReportRow>();
            result.add(ReportRow.error("Could not read modstat!"));
        }
        return result;
    }
}
