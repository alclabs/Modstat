package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WatchdogTimeouts extends BaseChecker {
    public WatchdogTimeouts() {
        name = "Watchdog Timeout";
        description = "Checks for any watchdog timeouts since last module format.";
    }

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.hasResetCounts()) {
            Map<String,Long> counts = modstat.getResetCounts();
            if (counts.containsKey("Watchdog timeouts")) {
                long timeouts = counts.get("Watchdog timeouts");
                if (timeouts > 0) {
                    result = new ArrayList<ReportRow>();
                    result.add(ReportRow.warning(timeouts +" watchdog timeouts in reset count."));
                }
            }
        }
        return result;
    }
}
