package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetCountSection extends ModstatSection {
    //    41 Power failures
    private static final Matcher resetCountLine = Pattern.compile("\\s+(\\d+)\\s+(.+)\\s*").matcher("");


    public ResetCountSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];



        if (source.getCurrentLine().startsWith("Reset counters:")) {
            while ((parts = matchesStart(source.nextLine(), resetCountLine)) != null) {
                String name = parts[1];
                try {
                    long count = Long.parseLong(parts[0]);
                    modstat.getResetCounts().put(name, count);
                    foundSection = true;
                } catch (NumberFormatException e) { }
            }
        }

        return foundSection;
    }
}