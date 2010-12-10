package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.BACnetError;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BACnetErrors extends BaseChecker {
    private static final SimpleDateFormat dtFormatyyss = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
    private static final NumberFormat numberFormat = new DecimalFormat("#,###");

    public BACnetErrors() {
        name = "BACnet Comm Erorrs";
        description = "Checks for too many BACnet communications errors over the last 7 days.";
    }

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.hasBacnetErrors()) {
            List<BACnetError> errorList = modstat.getBacnetErrors();

            for (BACnetError bacnetError : errorList) {
                long incoming = bacnetError.getIncomingCount();
                long outgoing = bacnetError.getOutgoingCount();

                long warnLimit = 25;
                long errorLimit = 100;

                if (incoming > errorLimit) {
                    if(result == null) { result = new ArrayList<ReportRow>(); }

                    result.add(ReportRow.error(numberFormat.format(incoming)+" incoming BACnet comm errors over the last 7 days."+
                    " The most recent was at "+ dtFormatyyss.format(bacnetError.getMostRecentTime()) +
                    " on the "+ bacnetError.getDataLinkName()));
                } else if (incoming > warnLimit) {
                    if(result == null) { result = new ArrayList<ReportRow>(); }
                    result.add(ReportRow.warning(numberFormat.format(incoming)+" incoming BACnet comm errors over the last 7 days."+
                    " The most recent was at "+ dtFormatyyss.format(bacnetError.getMostRecentTime()) +
                    " on the "+ bacnetError.getDataLinkName()));
                }

                if (outgoing > errorLimit) {
                    if(result == null) { result = new ArrayList<ReportRow>(); }

                    result.add(ReportRow.error(numberFormat.format(outgoing)+" outgoing BACnet comm errors over the last 7 days."+
                    " The most recent was at "+ dtFormatyyss.format(bacnetError.getMostRecentTime()) +
                    " on the "+ bacnetError.getDataLinkName()));
                } else if (outgoing > warnLimit) {
                    if(result == null) { result = new ArrayList<ReportRow>(); }

                    result.add(ReportRow.warning(numberFormat.format(outgoing)+" outgoing BACnet comm errors over the last 7 days."+
                    " The most recent was at "+ dtFormatyyss.format(bacnetError.getMostRecentTime()) +
                    " on the "+ bacnetError.getDataLinkName()));
                }
            }
        }
        return result;
    }
}
