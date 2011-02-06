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

import com.controlj.green.modstat.checks.Checker;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseChecker implements Checker, Serializable {
    protected static final NumberFormat countFormat = new DecimalFormat("#,###");
    protected static final NumberFormat percentFormat = new DecimalFormat("#,###.##%");
    
    protected Set<String> fieldNames = new HashSet<String>(Arrays.asList(FIELD_ENABLE ));
    protected String name ="";
    protected String description = "";
    private   boolean enabled = true;
    protected String id;

    public BaseChecker(String id) {
        this.id = id;
        if (id == null) {
            throw new RuntimeException("ID of a checker may not be null");
        }
        if (id.contains(":")) {
            throw new RuntimeException("Bad ID provided:'"+ id +"'.  ID can not contain a ':'.");
        }
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @NotNull
    @Override
    public String getFieldId(String fieldName) throws InvalidFieldNameException {
        confirmFieldName(fieldName);
        return getId()+":"+fieldName;
    }

    @Override
    public void setFieldValue(String fieldName, String value) throws InvalidFieldValueException, InvalidFieldNameException {

        if (FIELD_ENABLE.equals(fieldName)) {
            setEnabled(booleanValueFromString(value));
        } else {
            throw new InvalidFieldNameException("Unknown field:"+fieldName);
        }
        // Default implementation only handles enable.  Override for Checkers that have more fields
    }

    public @NotNull String getFieldValue(String fieldName) throws InvalidFieldNameException {
        // Default implementation only handles enable.  Override for Checkers that have more fields
        if (FIELD_ENABLE.equals(fieldName)) {
            return Boolean.toString(isEnabled());
        } else {
            throw new InvalidFieldNameException("Unknown field:"+fieldName);
        }
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @NotNull
    @Override
    public String getConfigHTML() {
        return "&nbsp;";
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    protected void confirmFieldName(String fieldName) throws InvalidFieldNameException {
        if (!fieldNames.contains(fieldName)) {
            throw new InvalidFieldNameException("Unknown field:"+fieldName);
        }
    }

    protected boolean booleanValueFromString(String value) throws InvalidFieldValueException {
        if ("true".equals(value)) {
            return true;
        } else if ("false".equals(value)) {
            return false;
        }
        throw new InvalidFieldValueException(("'"+value+"' is not a valid boolean value.  Must be 'true' or 'false'."));
    }

    protected int intValueFromString(String value) throws InvalidFieldValueException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new InvalidFieldValueException("'"+value+"' is not a valid interger value.", e);
        }
    }

    protected String getNumberInputHTML(String fieldName, String extra) {
        try {
            return "<input type=\"text\" name=\""+getFieldId(fieldName)+"\" value=\"" +
                    getFieldValue(fieldName)+"\" "+ extra+ "/>";
        } catch (InvalidFieldNameException e) {
            throw new RuntimeException("Unexpected Field Name", e);
        }
    }
}

