package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArcnetReconfigSection extends ModstatSection {
    //    Total ....................... 14
    private static final Matcher validLine = Pattern.compile("\\s+([^\\.]+)\\s+\\.+\\s*(\\d+)\\s*").matcher("");


    public ArcnetReconfigSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];



        if (source.getCurrentLine().startsWith("ARC156 reconfigurations during the last hour")) {
            while ((parts = matchesStart(source.nextLine(), validLine)) != null) {
                String name = parts[0];
                try {
                    long count = Long.parseLong(parts[1]);
                    modstat.getArcnetReconfigs().put(name, count);
                    foundSection = true;
                } catch (NumberFormatException e) { }
            }
        }

        return foundSection;
    }
}