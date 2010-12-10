package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProgramsSection extends ModstatSection {

    // older style is
    //3 PRGs loaded.  3 PRGs running.
    // newer style is
    //3 PRGs initialized.  3 PRGs running.
    private static final Matcher progLine = Pattern.compile("(\\d+) PRGs .+.\\s+(\\d+) PRGs running\\.").matcher("");


    public ProgramsSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        parts = matchesStart(source.getCurrentLine(), progLine);
        if (parts != null)
        {
            try {
                int loaded = Integer.parseInt(parts[0]);
                int running = Integer.parseInt(parts[1]);
                modstat.setProgramsLoaded(loaded);
                modstat.setProgramsRunning(running);
                foundSection = true;
            } catch (NumberFormatException e) { } // programs not set
            source.nextLine();
        }

        return foundSection;
    }
}