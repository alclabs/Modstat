package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationsSection extends ModstatSection {

    //Application Software Version: PRG:in_heat_req
    private static final Matcher asvLine = Pattern.compile("Application Software Version: (.+)").matcher("");


    public ApplicationsSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        parts = matchesStart(source.getCurrentLine(), asvLine);
        if (parts != null)
        {
            modstat.setApplicationSoftwareVersion(parts[0]);
            foundSection = true;
            source.nextLine();
        }

        return foundSection;
    }
}