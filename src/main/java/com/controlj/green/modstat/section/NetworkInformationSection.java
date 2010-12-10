package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkInformationSection extends ModstatSection {
    //  Ethernet MAC address  = 00-E0-C9-00-05-76
    private static final Matcher validLine = Pattern.compile("\\s+(.+)?\\s+=\\s+(.+)\\s*").matcher("");


    public NetworkInformationSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];

        if (source.getCurrentLine().startsWith("Network Information:")) {
            while ((parts = matchesStart(source.nextLine(), validLine)) != null) {
                modstat.getNetworkInfo().put(parts[0].trim(), parts[1].trim());
                foundSection = true;
            }
        }

        return foundSection;
    }
}