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

package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

public abstract class ModstatSection {
    protected static final SimpleDateFormat dtFormatyyyyss = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    protected static final SimpleDateFormat dtFormatyyss = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
    protected static final SimpleDateFormat dtFormatyy = new SimpleDateFormat("MM/dd/yy HH:mm");
    protected static final SimpleDateFormat dFormatyyyy = new SimpleDateFormat("MM/dd/yyyy");

    protected LineSource source;
    protected Modstat modstat;

    public ModstatSection(LineSource source, Modstat modstat) {
        this.source = source;
        this.modstat = modstat;
    }

    /**
     * See if the current line is starting this section.  If so, consume the section lines and update the modstat.
     *
     * @return true if this section was found
     */
    public abstract boolean lookForSection() throws StopParseException;

    protected String[] matchesStart(String line, Matcher m)
    {
        String[] result = null;
        if (line!=null && m.reset(line).lookingAt())
        {
            int groups = m.groupCount();
            result = new String[groups];
            for (int i=0; i<groups; i++)
            {
                result[i] = m.group(i+1);
            }
        }
        return result;
    }

    protected Date convertDate(String date, String time, DateFormat format) throws ParseException {
        return format.parse(date+" "+time);
    }



}
