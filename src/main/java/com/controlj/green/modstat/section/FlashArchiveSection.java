package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlashArchiveSection extends ModstatSection {
    //Flash Archive Status: Valid on 09/15/10 14:05:21
    private static final Matcher validLine = Pattern.compile("Flash Archive Status: Valid on (\\d\\d/\\d\\d/\\d\\d)\\s+(\\d\\d:\\d\\d:\\d\\d)").matcher("");


    public FlashArchiveSection(LineSource source, Modstat modstat) {
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
                modstat.setFlashArchiveTime( convertDate(parts[0], parts[1], dtFormatyyss));
                foundSection = true;
            } catch (ParseException e) { } // not set
            source.nextLine();
        }

        return foundSection;
    }
}