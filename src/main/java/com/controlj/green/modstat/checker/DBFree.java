package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;

public class DBFree extends BaseChecker {
    public DBFree() {
        name = "Database Free Space";
        description = "Check for small amounts of database space free.";
    }

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.hasDatabaseFree() && modstat.hasDatabaseSize() && modstat.hasDatabaseUsed()) {
            long free = modstat.getDatabaseFree();

            if (free < 2000L) {
                result = new ArrayList<ReportRow>();
                result.add(ReportRow.warning("Only "+ numberFormat.format(free)+" bytes out of "+
                        numberFormat.format(modstat.getDatabaseSize()) + " bytes of database space are available."));
            }
        }
        return result;
    }
}
