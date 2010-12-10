package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArcnetReconfig extends BaseChecker {
    public ArcnetReconfig() {
        name        = "Arcnet Reconfigs";
        description = "Checks for too many Arcnet reconfigurations in the last hour.";
    }

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.hasArcnetReconfigs()) {

            Map<String, Long> reconfigs = modstat.getArcnetReconfigs();

            if (reconfigs.containsKey(Modstat.ArcnetReconfigs.THIS_NODE) &&
                    reconfigs.containsKey(Modstat.ArcnetReconfigs.TOTAL)) {
                long rc_this = reconfigs.get(Modstat.ArcnetReconfigs.THIS_NODE);
                long rc_total = reconfigs.get(Modstat.ArcnetReconfigs.TOTAL);
                if (rc_total > 5) {
                    result = new ArrayList<ReportRow>();
                    result.add(ReportRow.error( rc_total +" arcnet reconfigs in the last hour. "+
                            rc_this+" were from this node."));
                }
            }
        }

        if (modstat.hasSecondaryArcnetReconfigs()) {

            Map<String, Long> reconfigs = modstat.getSecondaryArcnetReconfigs();

            if (reconfigs.containsKey(Modstat.ArcnetReconfigs.THIS_NODE) &&
                    reconfigs.containsKey(Modstat.ArcnetReconfigs.TOTAL)) {
                long rc_this = reconfigs.get(Modstat.ArcnetReconfigs.THIS_NODE);
                long rc_total = reconfigs.get(Modstat.ArcnetReconfigs.TOTAL);
                if (rc_total > 5) {
                    if (result == null) {
                        result = new ArrayList<ReportRow>();
                    }
                    result.add(ReportRow.error( rc_total +" secondary arcnet reconfigs in the last hour. "+
                            rc_this+" were from this node."));
                }
            }
        }
        return result;
    }

}
