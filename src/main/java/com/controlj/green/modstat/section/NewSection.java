package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewSection extends ModstatSection {
    private static final Matcher validLine = Pattern.compile("").matcher("");


    public NewSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];

        parts = matchesStart(source.getCurrentLine(), validLine);
        if (parts != null)
        {
            //modstat.set
            foundSection = true;
            source.nextLine();
        }

        return foundSection;
    }
}