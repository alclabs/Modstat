package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FreeHeapSection extends ModstatSection {

    // one style is
    //Free heap space = 271500.
    // more common style is
    //Largest free heap space = 18432.

    private static final Matcher validLine = Pattern.compile(".* heap space = (\\d+).").matcher("");


    public FreeHeapSection(LineSource source, Modstat modstat) {
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
                modstat.setFreeHeap(Long.parseLong(parts[0]));
                foundSection = true;
            } catch (NumberFormatException e) { } // not set
            source.nextLine();
        }

        return foundSection;
    }
}