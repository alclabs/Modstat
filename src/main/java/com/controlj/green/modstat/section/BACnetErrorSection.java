package com.controlj.green.modstat.section;

import com.controlj.green.modstat.BACnetError;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BACnetErrorSection extends ModstatSection {
    //  ARC156 data link                          09/15/10 16:56:03        0        6
    private static final Matcher validLine = Pattern.compile("  (.+)?\\s+(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d)\\s+(\\d+)\\s+(\\d+)").matcher("");


    public BACnetErrorSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];

        if (source.getCurrentLine().startsWith("BACnet comm errors in the last")) {
            while ((parts = matchesStart(source.nextLine(), validLine)) != null) {
                try {
                    modstat.getBacnetErrors().add(
                            new BACnetError(parts[0].trim(),
                                    convertDate(parts[1], parts[2],dtFormatyyss),
                                    Long.parseLong(parts[3]),
                                    Long.parseLong(parts[4])));
                } catch (ParseException e) { }  // don't add
                foundSection = true;
            }
        }

        return foundSection;
    }
}