package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EthernetSection extends ModstatSection {
    //  Total frames received    = 2010682
    private static final Matcher validLine = Pattern.compile("\\s+(.+)\\s*=\\s*\\s+(\\d+)\\s*").matcher("");


    public EthernetSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];

        if (source.getCurrentLine().startsWith("Ethernet statistics")) {
            while ((parts = matchesStart(source.nextLine(), validLine)) != null) {
                String name = parts[0].trim();
                try {
                    long count = Long.parseLong(parts[1]);
                    modstat.getEthernetStats().put(name, count);
                    foundSection = true;
                } catch (NumberFormatException e) { }
            }
        }

        return foundSection;
    }
}