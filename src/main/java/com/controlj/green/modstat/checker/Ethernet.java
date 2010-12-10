package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.controlj.green.modstat.Modstat.EthernetStats.*;

public class Ethernet extends BaseChecker {
    public Ethernet() {
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
                                numberFormat.format(value) +
                                " out of a total of "+ numberFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_ABORTS_JABBER)) {
                    long value = stats.get(TX_ABORTS_JABBER);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet transmits aborted due to jabbering = " +
                                numberFormat.format(value) + " out of a total of "+ numberFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_ABORTS_16_COLLISIONS)) {
                    long value = stats.get(TX_ABORTS_16_COLLISIONS);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet transmits aborted due excessive collisions = " +
                                numberFormat.format(value) +
                                " out of a total of "+ numberFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_ABORTS_UNDERRUN)) {
                    long value = stats.get(TX_ABORTS_UNDERRUN);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet transmits aborted due underruns = " +
                                numberFormat.format(value) +
                                " out of a total of "+ numberFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(RX_TOO_SMALL)) {
                    long value = stats.get(RX_TOO_SMALL);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet receives too small = " +
                                numberFormat.format(value) +
                                " out of a total of "+ numberFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_TOO_BIG)) {
                    long value = stats.get(RX_TOO_BIG);
                    if (value > 0) {
                        result = addRow(result, ReportRow.warning("Ethernet receives too big = " +
                                numberFormat.format(value) +
                                " out of a total of "+ numberFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(TX_NO_CHIP_MEM_A)) {
                    long value = stats.get(TX_NO_CHIP_MEM_A);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet transmits out of chip memory A = " +
                                numberFormat.format(value) +
                                " out of a total of "+ numberFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_NO_CHIP_MEM_B)) {
                    long value = stats.get(TX_NO_CHIP_MEM_B);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet transmits out of chip memory B = " +
                                numberFormat.format(value) +
                                " out of a total of "+ numberFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(TX_NO_CHIP_MEM_C)) {
                    long value = stats.get(TX_NO_CHIP_MEM_C);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet transmits out of chip memory C = " +
                                numberFormat.format(value) +
                                " out of a total of "+ numberFormat.format(totalTransmitted) + " transmitted."));
                    }
                }

                if (stats.containsKey(RX_NO_BUFFER)) {
                    long value = stats.get(RX_NO_BUFFER);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet receive out of buffers " +
                                numberFormat.format(value) +
                                " time(s) out of a total of "+ numberFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_ALIGNMENT)) {
                    long value = stats.get(RX_ALIGNMENT);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet receive failed with alignment problemd " +
                                numberFormat.format(value) +
                                " time(s) out of a total of "+ numberFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_CRC_ERRORS)) {
                    long value = stats.get(RX_CRC_ERRORS);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet receive failed with CRC error " +
                                numberFormat.format(value) +
                                " time(s) out of a total of "+ numberFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_DRIBBLE_ERRORS)) {
                    long value = stats.get(RX_DRIBBLE_ERRORS);
                    if (value > 0) {
                        result = addRow(result, ReportRow.error("Ethernet receive failed with dribble error " +
                                numberFormat.format(value) +
                                " time(s) out of a total of "+ numberFormat.format(totalReceived) + " received."));
                    }
                }

                if (stats.containsKey(RX_RECEPTION_MISSED)) {
                    long value = stats.get(RX_RECEPTION_MISSED);
                    long percentMissed = value * 100 / totalReceived;
                    if (percentMissed > 0) {
                        result = addRow(result, ReportRow.error("Over 1% of ethernet packets missed. " +
                                numberFormat.format(value) + " out of a total of "+ numberFormat.format(totalReceived) + " received."));
                    } else if (value > 0) {
                        result = addRow(result, ReportRow.warning(numberFormat.format(value) +
                                " ethernet receptions missed out of a total of " + numberFormat.format(totalReceived)));
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
