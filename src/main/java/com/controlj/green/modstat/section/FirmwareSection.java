package com.controlj.green.modstat.section;

import com.controlj.green.modstat.FirmwareVersion;
import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

public class FirmwareSection extends ModstatSection {

    public FirmwareSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];

        if (source.getCurrentLine().startsWith("Module status:") &&
            source.nextLine().startsWith("Firmware sections") &&
            source.nextLine().startsWith("==========================")) {
            while (FirmwareVersion.isFirmwareSection(source.nextLine())) {
                modstat.addFirmwareVersion(new FirmwareVersion(source.getCurrentLine()));
                foundSection = true;
            }
        }

        return foundSection;
    }
}