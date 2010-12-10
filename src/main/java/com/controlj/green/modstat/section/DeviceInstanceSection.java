package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceInstanceSection extends ModstatSection {
    private static final Matcher devInstanceLine = Pattern.compile("Device Instance:\\s+(\\d+)").matcher("");


    public DeviceInstanceSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];

        parts = matchesStart(source.getCurrentLine(), devInstanceLine);
        if (parts != null)
        {
            try {
                modstat.setDeviceID(Integer.parseInt(parts[0]));
                foundSection = true;
            } catch (NumberFormatException e) { }   // ignore and mark not found
            source.nextLine();
        }

        return foundSection;
    }
}