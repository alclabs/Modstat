package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class HeapFree extends BaseChecker{
    public HeapFree() {
        name = "Memory Free";
        description = "Checks for low free memory (heap space)";
    }

    private static final NumberFormat numberFormat = new DecimalFormat("#,###");

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.hasFreeHeap()) {
            long free = modstat.getFreeHeap();

            if (free < 5000L) {
                result = new ArrayList<ReportRow>();
                String totalMem = "?";
                if (modstat.hasCoreHardwareInfo()) {
                    totalMem = modstat.getCoreHardwareInfo().getKRam() +"k";
                }
                result.add(ReportRow.error("Only "+ numberFormat.format(free)+" bytes out of "+
                        numberFormat.format(totalMem)+" bytes of heap space are available."));
            }
        }
        return result;
    }
}
