package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;

public class ProgramsRunning extends BaseChecker {
    public ProgramsRunning() {
        name = "All Programs Running";
        description = "Checks that all loaded programs (GFBs) are running.";
    }

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.hasProgramsLoaded() && modstat.hasProgramsRunning()) {
            int loaded = modstat.getProgramsLoaded();
            int running = modstat.getProgramsRunning();

            if (loaded != running) {
                result = new ArrayList<ReportRow>();
                result.add(ReportRow.error("Only "+running+" out of "+loaded+" program(s) are running."));
            }
        }
        return result;
    }
}
