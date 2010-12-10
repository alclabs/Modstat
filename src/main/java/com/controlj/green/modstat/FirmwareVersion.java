package com.controlj.green.modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirmwareVersion {
    //  Boot8            - v4.01:001   Jun 19 2007
    //  S6104 DRIVER     - v2.24:013   Sep 03 2009
    private static final Matcher fwSectionMatcher = Pattern.compile("\\s\\s(.{1,16}?)\\s+-\\s+v([^\\s]+)\\s+(.+)").matcher("");

    private String name;
    private String version;
    private String dateString;


    public static boolean isFirmwareSection(String line)
    {
        fwSectionMatcher.reset(line);
        return fwSectionMatcher.lookingAt();
    }

    public FirmwareVersion(String line)
    {
        if (!isFirmwareSection(line))
        {
            throw new IllegalArgumentException("Not a firmware section");
        }

        name = fwSectionMatcher.group(1);
        version = fwSectionMatcher.group(2);
        dateString = fwSectionMatcher.group(3);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDateString() {
        return dateString;
    }
}
