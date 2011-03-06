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

import java.text.ParseException;
import java.util.regex.Pattern;

public class FlowSensorSection extends ModstatSection {
    //Integrated flow sensor calibration:
    //00:-2067 10:5273
    //ZASF status: offline

    private static final Pattern calibrationPattern = Pattern.compile("\\s*(\\-?\\d+):(\\-?\\d+) (\\-?\\d+):(\\-?\\d+)");
    private static final String validSectionPrefix = "Integrated flow sensor calibration:";
    private static final String statusPrefix = "ZASF status: ";


    public FlowSensorSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        String parts[];

        boolean foundSection = source.getCurrentLine().startsWith(validSectionPrefix);
        if (foundSection) {
            source.nextLine();

            if (calibrationPattern.matcher(source.getCurrentLine()).matches()) {
                modstat.setFlowSensorCalibration(source.getCurrentLine());
                source.nextLine();
            }
            if (source.getCurrentLine().startsWith(statusPrefix)) {
                String status = source.getCurrentLine().substring(statusPrefix.length());
                modstat.setZasfStatus(status);
                source.nextLine();
            }
            
            foundSection = true;
        }

        return foundSection;
    }
}