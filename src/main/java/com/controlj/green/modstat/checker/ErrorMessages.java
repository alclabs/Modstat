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
import com.controlj.green.modstat.Message;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorMessages extends BaseChecker {
    public static final String FIELD_ERROR_TEXT = "error_text";

    String error_text = "";

    public ErrorMessages(String id) {
        super(id);
        name = "Error Messages";
        description = "Search for error messages";
        fieldNames.addAll(Arrays.asList(FIELD_ERROR_TEXT));
    }

    @Override @NotNull
    public String getFieldValue(String fieldName) throws InvalidFieldNameException {
        if (FIELD_ERROR_TEXT.equals(fieldName)) {
            return error_text;
        }
        return super.getFieldValue(fieldName);
    }


    @Override
    public void setFieldValue(String fieldName, String value) throws InvalidFieldValueException, InvalidFieldNameException {
        if (FIELD_ERROR_TEXT.equals(fieldName)) {
            error_text = value;
        } else {
            super.setFieldValue(fieldName, value.trim());
        }
    }

    @NotNull
    @Override
    public String getConfigHTML() {
        return "Messages containing:" + getTextInputHTML(FIELD_ERROR_TEXT, "size=\"80\"");
    }

    @Override
    public List<ReportRow> check(Modstat modstat, SystemAccess access, Location location) {
        List<ReportRow> result = null;

        if (modstat.hasErrorMessages()) {
            List<Message> messages = modstat.getErrorMessages();
            result = new ArrayList<ReportRow>();

            for (Message message : messages) {
                if (error_text.isEmpty() || message.getMessage().contains(error_text)) {
                    result.add(ReportRow.error("Error message:'" + message + "'"));
                }
            }
        }
        return result;
    }
}
