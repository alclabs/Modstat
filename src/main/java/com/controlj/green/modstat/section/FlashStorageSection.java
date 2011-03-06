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

import com.controlj.green.modstat.FlashStorage;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlashStorageSection extends ModstatSection {

    //Flash storage size = 458752
    //  File storage size = 458752 (max = 458752), used = 6320, free = 452432
    
    //Flash storage size = 7061504
    //  Archive storage size = 7045120
    //  File storage size = 16384 (max = 7061504), used = 6376, free = 10008

    private static final Pattern firstLine = Pattern.compile("Flash storage size = (\\d+)");
    private static final Pattern secondLine = Pattern.compile("\\s+Archive storage size = (\\d+)");
    private static final Pattern thirdLine = Pattern.compile("\\s+File storage size = (\\d+) \\(max = (\\d+)\\), used = (\\d+), free = (\\d+)");



    public FlashStorageSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        parts = matchesStart(source.getCurrentLine(), firstLine.matcher(""));
        if (parts != null)
        {
            try {
                long flashSize = (Long.parseLong(parts[0]));
                source.nextLine();

                parts = matchesStart(source.getCurrentLine(), secondLine.matcher(""));
                long archiveSize = Long.MIN_VALUE;
                if (parts != null) {
                    archiveSize = Long.parseLong(parts[0]);
                    source.nextLine();
                }

                parts = matchesStart(source.getCurrentLine(), thirdLine.matcher(""));
                if (parts != null) {
                    modstat.setFlashStorage(new FlashStorage(flashSize,
                            archiveSize,
                            Long.parseLong(parts[0]),
                            Long.parseLong(parts[1]),
                            Long.parseLong(parts[2]),
                            Long.parseLong(parts[3])
                            ));
                    foundSection = true;
                    source.nextLine();
                }
            } catch (NumberFormatException e) { } // not set
            source.nextLine();
        }

        return foundSection;
    }
}