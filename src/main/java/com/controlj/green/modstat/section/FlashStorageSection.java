package com.controlj.green.modstat.section;

import com.controlj.green.modstat.FlashStorage;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlashStorageSection extends ModstatSection {

    //Flash storage size = 458752
    //  File storage size = 458752 (max = 458752), used = 6320, free = 452432

    private static final Matcher firstLine = Pattern.compile("Flash storage size = (\\d+)").matcher("");
    private static final Matcher secondLine = Pattern.compile("  File storage size = (\\d+) \\(max = (\\d+)\\), used = (\\d+), free = (\\d+)").matcher("");



    public FlashStorageSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        parts = matchesStart(source.getCurrentLine(), firstLine);
        if (parts != null)
        {
            try {
                long flashSize = (Long.parseLong(parts[0]));

                parts = matchesStart(source.nextLine(), secondLine);
                if (parts != null) {
                    modstat.setFlashStorage(new FlashStorage(flashSize,
                            Long.parseLong(parts[0]),
                            Long.parseLong(parts[1]),
                            Long.parseLong(parts[2]),
                            Long.parseLong(parts[3])
                            ));
                    foundSection = true;
                    source.nextLine();
                }
            } catch (NumberFormatException e) { } // not set
            source.nextLine();
        }

        return foundSection;
    }
}