package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ErrorCount extends BaseChecker {
    public ErrorCount() {
        name = "Error Count";
        description = "Check if any errors have occurred since the error counts were reset";
    }

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.hasResetCounts()) {
            Map<String, Long> counts = modstat.getResetCounts();
            if (counts.containsKey(Modstat.ResetCount.ERRORS)) {
                Long errors = counts.get(Modstat.ResetCount.ERRORS);
                if (errors > 0) {
                    if (result == null) { result = new ArrayList<ReportRow>(); }
                    result.add(ReportRow.warning(numberFormat.format(errors) + " error(s) in reset count."));
                }
            }
        }
        return result;
    }
}
