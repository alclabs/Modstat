package com.controlj.green.modstat.section;

import com.controlj.green.modstat.HardwareInfo;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseHardwareSection extends ModstatSection {
    //Base board hardware:
    //  Type=93, board=3, manufactured on 09/28/2000, S/N U32090024N
    private static final Matcher firstLine = Pattern.compile("  Type=(\\d+), board=(\\d+), manufactured on (\\d\\d/\\d\\d/\\d\\d\\d\\d), S/N (.+)").matcher("");


    public BaseHardwareSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        if (source.getCurrentLine().startsWith("Base board hardware:")) {
            parts = matchesStart(source.nextLine(), firstLine);
            if (parts != null)
            {
                try {
                    HardwareInfo hwInfo = new HardwareInfo();
                    hwInfo.setType(Integer.parseInt(parts[0]));
                    hwInfo.setBoard(Integer.parseInt(parts[1]));
                    hwInfo.setManufactureDate(dFormatyyyy.parse(parts[2]));
                    hwInfo.setSerialNumber(parts[3]);

                    modstat.setBaseHardwareInfo(hwInfo);
                    foundSection = true;
                } catch (ParseException e) { } // not set
                catch (NumberFormatException e) {} // not set
                source.nextLine();
            }

        }
        return foundSection;
    }
}