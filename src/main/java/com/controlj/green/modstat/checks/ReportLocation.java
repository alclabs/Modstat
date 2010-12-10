package com.controlj.green.modstat.checks;

import com.controlj.green.addonsupport.access.Location;

import java.util.ArrayList;
import java.util.List;

public class ReportLocation {
    String displayPath;
    String lookup;
    String name;
    List<ReportRow> rows = new ArrayList<ReportRow>();

    public ReportLocation(Location loc) {
        displayPath = loc.getDisplayPath();
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
}
