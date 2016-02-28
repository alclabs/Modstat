/*
 * Copyright (c) 2011 Automated Logic Corporation
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

import com.controlj.green.addonsupport.access.Location;
import com.controlj.green.addonsupport.access.SystemAccess;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.controlj.green.modstat.Modstat.EthernetStats.*;

public class Ethernet extends BaseChecker {
    public static final String FIELD_WARN_LIMIT = "warn";
    public static final String FIELD_ERROR_LIMIT = "error";

    // These are percentages
    private float warnLimit = 0.02f;
    private float errorLimit = 0.3f;

    public Ethernet(String id) {
        super(id);
        name = "Ethernet Statistics";
        description = "Too many ethernet communications errors since last download or zap";
        fieldNames.addAll(Arrays.asList(FIELD_WARN_LIMIT, FIELD_ERROR_LIMIT));
    }

    @Override @NotNull
    public String getFieldValue(String fieldName) throws InvalidFieldNameException {
        if (FIELD_WARN_LIMIT.equals(fieldName)) {
            return Float.toString(warnLimit);
        } else if (FIELD_ERROR_LIMIT.equals(fieldName)) {
            return Float.toString(errorLimit);
        }
        return super.getFieldValue(fieldName);
    }


    @Override
    public void setFieldValue(String fieldName, String value) throws InvalidFieldValueException, InvalidFieldNameException {
        if (FIELD_WARN_LIMIT.equals(fieldName)) {
            warnLimit = floatValueFromString(value);
        } else if (FIELD_ERROR_LIMIT.equals(fieldName)) {
            errorLimit = floatValueFromString(value);
        } else {
            super.setFieldValue(fieldName, value);
        }
    }

    @NotNull
    @Override
    public String getConfigHTML() {
        return "Warning if more than " + getTextInputHTML(FIELD_WARN_LIMIT, "size=\"4\"")+
                "% of packets with errors.<br/>" +
                "Error if more than " + getTextInputHTML(FIELD_ERROR_LIMIT, "size=\"4\"")+
                "% of packets with errors. (counts cleared on download or reset)";
    }



    @Override
    public List<ReportRow> check(Modstat modstat, SystemAccess access, Location location) {
        List<ReportRow> result = null;

        if (modstat.hasEthernetStats()) {
            Map<String, Long> stats = modstat.getEthernetStats();
            if (stats.containsKey(TOTAL_RECEIVED) &&
                    stats.containsKey(TOTAL_TRANSMITTED))
            {
                long totalReceived = stats.get(TOTAL_RECEIVED);
                long totalTransmitted = stats.get(TOTAL_TRANSMITTED);

                long totalRxProblems = 0;
                long totalTxProblems = 0;

                if (stats.containsKey(TX_ABORTS_JABBER)) {
                    long value = stats.get(TX_ABORTS_JABBER);
                    totalTxProblems += value;
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet transmits aborted due to jabbering = " +
                                countFormat.format(value) + " out of a total of "+ countFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                totalTxProblems += getCounts(TX_ABORTS_OUT_OF_WIN, stats);
                totalTxProblems += getCounts(TX_ABORTS_16_COLLISIONS, stats);
                totalTxProblems += getCounts(TX_ABORTS_UNDERRUN, stats);
                totalTxProblems += getCounts(TX_CLEANUPS_A, stats);
                totalTxProblems += getCounts(TX_CLEANUPS_B, stats);
                totalTxProblems += getCounts(TX_CLEANUPS_D, stats);
                totalTxProblems += getCounts(TX_CLEANUPS_E, stats);

                float txPercentProblems = 0f;
                if (totalTransmitted > 0) {
                    txPercentProblems = (((float)totalTxProblems) / totalTransmitted);
                }
                if ( (txPercentProblems * 100 > warnLimit) || (txPercentProblems * 100 > errorLimit)) {
                    String message = "Too many ethernet transmission problems. " +
                            "There were " + countFormat.format(totalTxProblems) +
                            " out of a total of " + countFormat.format(totalTransmitted) + " packets transmitted. (" +
                            percentFormat.format(txPercentProblems) + ").";
                    if (txPercentProblems * 100 > errorLimit) {
                        result = addRow(result, ReportRow.error(message));
                    } else {
                        result = addRow(result, ReportRow.warning(message));
                    }
                }


                totalRxProblems += getCounts(RX_TOO_SMALL, stats);
                totalRxProblems += getCounts(RX_TOO_BIG, stats);
                totalRxProblems += getCounts(RX_NO_BUFFER, stats);
                totalRxProblems += getCounts(RX_ALIGNMENT, stats);
                totalRxProblems += getCounts(RX_CRC_ERRORS, stats);
                totalRxProblems += getCounts(RX_DRIBBLE_ERRORS, stats);
                totalRxProblems += getCounts(RX_RECEPTION_MISSED, stats);

                float rxPercentProblems = 0f;
                if (totalReceived > 0) {
                    rxPercentProblems = (((float)totalRxProblems) / totalReceived);
                }
                if ( (rxPercentProblems * 100 > warnLimit) || (rxPercentProblems * 100 > errorLimit)) {
                    String message = "Too many ethernet reception problems. " +
                            "There were " + countFormat.format(totalRxProblems) +
                            " out of a total of " + countFormat.format(totalReceived) + " packets received. (" +
                            percentFormat.format(rxPercentProblems) + ").";
                    if (rxPercentProblems * 100 > errorLimit) {
                        result = addRow(result, ReportRow.error(message));
                    } else {
                        result = addRow(result, ReportRow.warning(message));
                    }
                }
            }
        }
        return result;
    }

    long getCounts(String key, Map<String, Long> stats) {
        if (stats.containsKey(key)) {
            return stats.get(key);
        }
        return 0;
    }


    private static List<ReportRow> addRow(List<ReportRow> rows, ReportRow newRow) {
        if (rows == null) {
            rows = new ArrayList<ReportRow>();
        }
        rows.add(newRow);
        return rows;
    }
}
