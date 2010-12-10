package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeSection extends ModstatSection {
    //09/07/2010  21:46:11         CM: 1
    private static final Matcher timeLine = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d\\d\\d)\\s+(\\d\\d:\\d\\d:\\d\\d)").matcher("");


    public TimeSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        //todo - parse CM part at the end
        boolean foundSection = false;
        String parts[];

        parts = matchesStart(source.getCurrentLine(), timeLine);
        if (parts != null)
        {
            try {
                modstat.setCaptureTime(convertDate(parts[0], parts[1], dtFormatyyyyss));
                foundSection = true;
            } catch (ParseException e) {
                // time not set.  Ignore - time not set in modstat
            }
            source.nextLine();
        }

        return foundSection;
    }
}
