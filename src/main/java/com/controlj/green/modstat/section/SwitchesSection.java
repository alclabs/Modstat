package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwitchesSection extends ModstatSection {

    //Raw physical switches: 0x3008000
    private static final Matcher validLine = Pattern.compile("Raw physical switches: 0x(\\d+)").matcher("");


    public SwitchesSection(LineSource source, Modstat modstat) {
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
                modstat.setSwitches(Integer.parseInt(parts[0], 16));
                foundSection = true;
            } catch (NumberFormatException e) { } // not set
            source.nextLine();
        }

        return foundSection;
    }
}