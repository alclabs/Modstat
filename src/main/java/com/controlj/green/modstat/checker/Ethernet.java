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

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.controlj.green.modstat.Modstat.EthernetStats.*;

public class Ethernet extends BaseChecker {
    public Ethernet(String id) {
        super(id);
        name = "Ethernet Statistics";
        description = "Check for ethernet communications errors";
    }

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.hasEthernetStats()) {
            Map<String, Long> stats = modstat.getEthernetStats();
            if (stats.containsKey(TOTAL_RECEIVED) &&
                    stats.containsKey(TOTAL_TRANSMITTED))
            {
                long totalReceived = stats.get(TOTAL_RECEIVED);
                long totalTransmitted = stats.get(TOTAL_TRANSMITTED);

                if (stats.containsKey(TX_ABORTS_OUT_OF_WIN)) {
                    long value = stats.get(TX_ABORTS_OUT_OF_WIN);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet transmits aborted due to out of window error = " +
                                countFormat.format(value) +
                                " out of a total of "+ countFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_ABORTS_JABBER)) {
                    long value = stats.get(TX_ABORTS_JABBER);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet transmits aborted due to jabbering = " +
                                countFormat.format(value) + " out of a total of "+ countFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_ABORTS_16_COLLISIONS)) {
                    long value = stats.get(TX_ABORTS_16_COLLISIONS);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet transmits aborted due excessive collisions = " +
                                countFormat.format(value) +
                                " out of a total of "+ countFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_ABORTS_UNDERRUN)) {
                    long value = stats.get(TX_ABORTS_UNDERRUN);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet transmits aborted due underruns = " +
                                countFormat.format(value) +
                                " out of a total of "+ countFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(RX_TOO_SMALL)) {
                    long value = stats.get(RX_TOO_SMALL);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet receives too small = " +
                                countFormat.format(value) +
                                " out of a total of "+ countFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_TOO_BIG)) {
                    long value = stats.get(RX_TOO_BIG);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet receives too big = " +
                                countFormat.format(value) +
                                " out of a total of "+ countFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(TX_NO_CHIP_MEM_A)) {
                    long value = stats.get(TX_NO_CHIP_MEM_A);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet transmits out of chip memory A = " +
                                countFormat.format(value) +
                                " out of a total of "+ countFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_NO_CHIP_MEM_B)) {
                    long value = stats.get(TX_NO_CHIP_MEM_B);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet transmits out of chip memory B = " +
                                countFormat.format(value) +
                                " out of a total of "+ countFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_NO_CHIP_MEM_C)) {
                    long value = stats.get(TX_NO_CHIP_MEM_C);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet transmits out of chip memory C = " +
                                countFormat.format(value) +
                                " out of a total of "+ countFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(RX_NO_BUFFER)) {
                    long value = stats.get(RX_NO_BUFFER);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet receive out of buffers " +
                                countFormat.format(value) +
                                " time(s) out of a total of "+ countFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_ALIGNMENT)) {
                    long value = stats.get(RX_ALIGNMENT);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet receive failed with alignment problemd " +
                                countFormat.format(value) +
                                " time(s) out of a total of "+ countFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_CRC_ERRORS)) {
                    long value = stats.get(RX_CRC_ERRORS);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet receive failed with CRC error " +
                                countFormat.format(value) +
                                " time(s) out of a total of "+ countFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_DRIBBLE_ERRORS)) {
                    long value = stats.get(RX_DRIBBLE_ERRORS);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet receive failed with dribble error " +
                                countFormat.format(value) +
                                " time(s) out of a total of "+ countFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_RECEPTION_MISSED)) {
                    long value = stats.get(RX_RECEPTION_MISSED);
                    long percentMissed = value * 100 / totalReceived;
                    if (percentMissed > 0) {
                        result = addRow(result, ReportRow.error("Over 1% of ethernet packets missed. " +
                                countFormat.format(value) + " out of a total of "+ countFormat.format(totalReceived) + " received."));
                    } else if (value > 0) {
                        result = addRow(result, ReportRow.warning(countFormat.format(value) +
                                " ethernet receptions missed out of a total of " + countFormat.format(totalReceived)));
                    }
                }
            }
        }
        return result;
    }

    private static List<ReportRow> addRow(List<ReportRow> rows, ReportRow newRow) {
        if (rows == null) {
            rows = new ArrayList<ReportRow>();
        }
        rows.add(newRow);
        return rows;
    }
}
