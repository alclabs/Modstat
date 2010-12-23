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
