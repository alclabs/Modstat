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

import com.controlj.green.addonsupport.access.Location;
import com.controlj.green.addonsupport.access.UnresolvableException;

import java.util.ArrayList;
import java.util.List;

public class ReportLocation {
    String displayPath;
    String lookup;
    String name;
    List<ReportRow> rows = new ArrayList<ReportRow>();

    public ReportLocation(Location loc) {
        displayPath = getFullDisplayPath(loc);
        lookup = loc.getPersistentLookupString(false);
        name = loc.getDisplayName();
    }

    public ReportLocation(String name, String displayPath, String lookup) {
        this.name = name;
        this.displayPath = displayPath;
        this.lookup = lookup;
    }

    public String getDisplayPath() {
        return displayPath;
    }

    public String getLookup() {
        return lookup;
    }

    public String getName() {
        return name;
    }

    public void addRow(ReportRow newRow) {
        rows.add(newRow);
    }

    public void addRows(List<ReportRow> newRows) {
        rows.addAll(newRows);
    }

    public List<ReportRow> getRows() {
        return rows;
    }

    public void setRows(List<ReportRow> rows) {
        this.rows = rows;
    }

    private String getFullDisplayPath(Location loc) {
        if (!loc.hasParent()) {
            return "";
        } else {
            try {
                return getFullDisplayPath(loc.getParent()) + " / " + loc.getDisplayName();
            } catch (UnresolvableException e) {
                throw new RuntimeException("can't resolve parent even though hasParent is true", e);
            }
        }
    }
}
