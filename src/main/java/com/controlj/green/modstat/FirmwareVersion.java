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

package com.controlj.green.modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirmwareVersion {
    //  Boot8            - v4.01:001   Jun 19 2007
    //  S6104 DRIVER     - v2.24:013   Sep 03 2009
    private static final Matcher fwSectionMatcher = Pattern.compile("\\s\\s(.{1,16}?)\\s+-\\s+v([^\\s]+)\\s+(.+)").matcher("");

    private String name;
    private String version;
    private String dateString;


    public static boolean isFirmwareSection(String line)
    {
        fwSectionMatcher.reset(line);
        return fwSectionMatcher.lookingAt();
    }

    public FirmwareVersion(String line)
    {
        if (!isFirmwareSection(line))
        {
            throw new IllegalArgumentException("Not a firmware section");
        }

        name = fwSectionMatcher.group(1);
        version = fwSectionMatcher.group(2);
        dateString = fwSectionMatcher.group(3);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDateString() {
        return dateString;
    }
}
