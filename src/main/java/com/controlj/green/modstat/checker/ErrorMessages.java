package com.controlj.green.modstat.checker;

import com.controlj.green.modstat.Message;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;

public class ErrorMessages extends BaseChecker {
    public ErrorMessages() {
        name = "Error Messages";
        description = "Display any error messages";
    }

    @Override
    public List<ReportRow> check(Modstat modstat) {
        List<ReportRow> result = null;

        if (modstat.hasErrorMessages()) {
            List<Message> messages = modstat.getErrorMessages();
            result = new ArrayList<ReportRow>();

            for (Message message : messages) {
                result.add(ReportRow.error("Error message:'"+message+"'"));
            }
        }
        return result;
    }
}
