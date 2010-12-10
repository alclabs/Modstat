package com.controlj.green.modstat.section;

import com.controlj.green.modstat.HardwareInfo;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreHardwareSection extends ModstatSection {
    //Core board hardware:
    //  Type=168, board=74, manufactured on 08/17/2009, S/N 21C951608N
    //  RAM: 1024 kBytes;    FLASH: 4096 kBytes, type = 6
    private static final Matcher firstLine = Pattern.compile("  Type=(\\d+), board=(\\d+), manufactured on (\\d\\d/\\d\\d/\\d\\d\\d\\d), S/N (.+)").matcher("");
    private static final Matcher secondLine = Pattern.compile("  RAM: (\\d+) kBytes;    FLASH: (\\d+) kBytes, type = (\\d+)").matcher("");


    public CoreHardwareSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];


        if (source.getCurrentLine().startsWith("Core board hardware:") ||
           (source.getCurrentLine().startsWith("Main board hardware:"))) {
            parts = matchesStart(source.nextLine(), firstLine);
            if (parts != null)
            {
                try {
                    HardwareInfo hwInfo = new HardwareInfo();
                    hwInfo.setType(Integer.parseInt(parts[0]));
                    hwInfo.setBoard(Integer.parseInt(parts[1]));
                    hwInfo.setManufactureDate(dFormatyyyy.parse(parts[2]));
                    hwInfo.setSerialNumber(parts[3]);

                    parts = matchesStart(source.nextLine(), secondLine);
                    if (parts != null) {
                        hwInfo.setKRam(Integer.parseInt(parts[0]));
                        hwInfo.setKFlash(Integer.parseInt(parts[1]));
                        hwInfo.setFlashType(Integer.parseInt(parts[2]));

                        modstat.setCoreHardwareInfo(hwInfo);
                        foundSection = true;
                    }
                } catch (ParseException e) { } // not set
                catch (NumberFormatException e) {} // not set
                source.nextLine();
            }

        }
        return foundSection;
    }
}