/*
 * Copyright (c) 2010 Automated Logic Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
