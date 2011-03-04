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

public class ArcnetReconfigCause extends BaseChecker {
    public static final String FIELD_ERROR_LIMIT = "error";
    public static final String FIELD_WARN_LIMIT = "warn";

    private int errorLimit = 3;
    private int warningLimit = 1;


    public ArcnetReconfigCause(String id) {
        super(id);
        name        = "Cause of Arcnet Reconfigs";
        description = "Checks for devices causing Arcnet reconfigurations in the last hour.";
        fieldNames.addAll(Arrays.asList(FIELD_ERROR_LIMIT, FIELD_WARN_LIMIT));
    }

    @Override @NotNull public String getFieldValue(String fieldName) throws InvalidFieldNameException {
        if (FIELD_ERROR_LIMIT.equals(fieldName)) {
            return Integer.toString(errorLimit);
        } else if (FIELD_WARN_LIMIT.equals(fieldName)) {
            return Integer.toString(warningLimit);
        }
        return super.getFieldValue(fieldName);
    }


    @Override
    public void setFieldValue(String fieldName, String value) throws InvalidFieldValueException, InvalidFieldNameException {
        if (FIELD_ERROR_LIMIT.equals(fieldName)) {
            errorLimit = intValueFromString(value);
        } else if (FIELD_WARN_LIMIT.equals(fieldName)) {
            warningLimit = intValueFromString(value);
        }else {
            super.setFieldValue(fieldName, value);
        }
    }

    @NotNull
    @Override
    public String getConfigHTML() {
        return "Warning if more than " + getNumberInputHTML(FIELD_WARN_LIMIT, "size=\"4\"")+
                " total Arcnet reconfigs in the last hour. (from this node)<br/>"+
                "Error if more than " + getNumberInputHTML(FIELD_ERROR_LIMIT, "size=\"4\"")+
                " total Arcnet reconfigs in the last hour. (from this node)";
    }

    @Override
    public List<ReportRow> check(Modstat modstat, SystemAccess access, Location location) {
        List<ReportRow> result = null;

        if (modstat.hasArcnetReconfigs()) {

            Map<String, Long> reconfigs = modstat.getArcnetReconfigs();

            if (reconfigs.containsKey(Modstat.ArcnetReconfigs.THIS_NODE) &&
                    reconfigs.containsKey(Modstat.ArcnetReconfigs.TOTAL)) {
                long rc_this = reconfigs.get(Modstat.ArcnetReconfigs.THIS_NODE);
                if (rc_this > errorLimit || rc_this > warningLimit) {
                    String msg = countFormat.format(rc_this) +" arcnet reconfigs in the last hour from this device.";
                    result = new ArrayList<ReportRow>();
                    result.add((rc_this > errorLimit) ? ReportRow.error(msg) : ReportRow.warning(msg));
                }
            }
        }

        if (modstat.hasSecondaryArcnetReconfigs()) {

            Map<String, Long> reconfigs = modstat.getSecondaryArcnetReconfigs();

            if (reconfigs.containsKey(Modstat.ArcnetReconfigs.THIS_NODE) &&
                    reconfigs.containsKey(Modstat.ArcnetReconfigs.TOTAL)) {
                long rc_this = reconfigs.get(Modstat.ArcnetReconfigs.THIS_NODE);
                if (rc_this > errorLimit || rc_this > warningLimit) {
                    if (result == null) {
                        result = new ArrayList<ReportRow>();
                    }
                    String msg = countFormat.format(rc_this) +" arcnet reconfigs in the last hour from this device.";
                    result.add((rc_this > errorLimit) ? ReportRow.error(msg) : ReportRow.warning(msg));
                }
            }
        }
        return result;
    }

}