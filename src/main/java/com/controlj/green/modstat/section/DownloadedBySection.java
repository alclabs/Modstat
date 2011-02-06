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

import com.controlj.green.modstat.DownloadInfo;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadedBySection extends ModstatSection {
    //Downloaded by: WebCTRL Server 09/15/10 13:58 Mark Evans - 172.16.200.32:0xbac2
    // groups
    //    product
    //    date
    //    time
    //    operator
    //    address
    private static final Matcher dlLine = Pattern.compile("Downloaded by:\\s+(.+)\\s+(\\d\\d/\\d\\d/\\d\\d)\\s+(\\d\\d:\\d\\d)\\s+(.+) - (.+)").matcher("");


    public DownloadedBySection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        parts = matchesStart(source.getCurrentLine(), dlLine);
        if (parts != null)
        {
            try {
                Date time = convertDate(parts[1], parts[2], dtFormatyy);
                modstat.setDownloadInfo(new DownloadInfo(parts[0], time, parts[3], parts[4]));
                foundSection = true;
            } catch (ParseException e) { } // not set
            source.nextLine();
        }

        return foundSection;
    }
}