package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseSection extends ModstatSection {

    //Database size = 368274 , used = 185994, free = 182280.

    private static final Matcher validLine = Pattern.compile("Database size = (\\d+) , used = (\\d+), free = (\\d+).").matcher("");


    public DatabaseSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        parts = matchesStart(source.getCurrentLine(), validLine);
        if (parts != null)
        {
            try {
                modstat.setDatabaseSize(Long.parseLong(parts[0]));
                modstat.setDatabaseUsed(Long.parseLong(parts[1]));
                modstat.setDatabaseFree(Long.parseLong(parts[2]));
                foundSection = true;
            } catch (NumberFormatException e) { } // not set
            source.nextLine();
        }

        return foundSection;
    }
}