/*
 * Copyright (c) 2013 Automated Logic Corporation
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

import com.controlj.green.modstat.AddressBinding;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class AddressBindingSection extends ModstatSection
{
    //  ADDRESS BINDING Used: device instance 488001 is on network 0 mac 172.31.217.213:0xbac0
    private static final Matcher matchLine = Pattern.compile("ADDRESS BINDING Used: device instance (\\d+) is on network (\\d+) mac ([^\\s]+)").matcher("");

    public AddressBindingSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];

        parts = matchesStart(source.getCurrentLine(), matchLine);
        if (parts != null && parts.length == 3)
        {
            try {
                int instance = Integer.parseInt(parts[0]);
                int network = Integer.parseInt(parts[1]);
                String mac = parts[2];

                modstat.setAddressBinding(new AddressBinding(instance, network, mac));
                foundSection = true;
            } catch (NumberFormatException e) {} // not set, found still false

            source.nextLine();
        }

        return foundSection;
    }

}
