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

import com.controlj.green.modstat.HardwareInfo;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseHardwareSection extends ModstatSection {
    //Base board hardware:
    //  Type=93, board=3, manufactured on 09/28/2000, S/N U32090024N
    private static final Matcher firstLine = Pattern.compile("  Type=(\\d+), board=(\\d+), manufactured on (\\d\\d/\\d\\d/\\d\\d\\d\\d), S/N (.+)").matcher("");


    public BaseHardwareSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        if (source.getCurrentLine().startsWith("Base board hardware:")) {
            parts = matchesStart(source.nextLine(), firstLine);
            if (parts != null)
            {
                try {
                    HardwareInfo hwInfo = new HardwareInfo();
                    hwInfo.setType(Integer.parseInt(parts[0]));
                    hwInfo.setBoard(Integer.parseInt(parts[1]));
                    hwInfo.setManufactureDate(dFormatyyyy.parse(parts[2]));
                    hwInfo.setSerialNumber(parts[3]);

                    modstat.setBaseHardwareInfo(hwInfo);
                    foundSection = true;
                } catch (ParseException e) { } // not set
                catch (NumberFormatException e) {} // not set
                source.nextLine();
            }

        }
        return foundSection;
    }
}