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

package com.controlj.green.modstat.checks;

import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checker.InvalidFieldNameException;
import com.controlj.green.modstat.checker.InvalidFieldValueException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Checker {
    static final String FIELD_ENABLE = "enable";


    List<ReportRow> check(Modstat modstat);
    @NotNull String getId();
    @NotNull String getFieldId(String fieldName) throws InvalidFieldNameException;
    void setFieldValue(String fieldName, String value) throws InvalidFieldValueException, InvalidFieldNameException;
    @NotNull String getFieldValue(String fieldName) throws InvalidFieldNameException;
    @NotNull String getName();
    @NotNull String getDescription();
    @NotNull String getConfigHTML();
    void setEnabled(boolean enabled);
    boolean isEnabled();
}
