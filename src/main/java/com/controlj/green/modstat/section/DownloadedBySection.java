package com.controlj.green.modstat.section;

import com.controlj.green.modstat.DownloadInfo;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadedBySection extends ModstatSection {
    //Downloaded by: WebCTRL Server 09/15/10 13:58 Mark Evans - 172.16.200.32:0xbac2
    // groups
    //    product
    //    date
    //    time
    //    operator
    //    address
    private static final Matcher dlLine = Pattern.compile("Downloaded by:\\s+(.+)\\s+(\\d\\d/\\d\\d/\\d\\d)\\s+(\\d\\d:\\d\\d)\\s+(.+) - (.+)").matcher("");


    public DownloadedBySection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        parts = matchesStart(source.getCurrentLine(), dlLine);
        if (parts != null)
        {
            try {
                Date time = convertDate(parts[1], parts[2], dtFormatyy);
                modstat.setDownloadInfo(new DownloadInfo(parts[0], time, parts[3], parts[4]));
                foundSection = true;
            } catch (ParseException e) { } // not set
            source.nextLine();
        }

        return foundSection;
    }
}