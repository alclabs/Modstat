package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Message;
import com.controlj.green.modstat.Modstat;

public class WarningMessageSection extends ModstatSection {

    public WarningMessageSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];

        if (source.getCurrentLine().startsWith("Warning message history:")) {
            while (Message.isMessage(source.nextLine())) {
                modstat.getWarningMessages().add(new Message(source.getCurrentLine()));
                foundSection = true;
            }
        }

        return foundSection;
    }
}