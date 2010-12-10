package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Message;
import com.controlj.green.modstat.Modstat;

public class ErrorMessageSection extends ModstatSection {

    public ErrorMessageSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() {
        boolean foundSection = false;
        String parts[];

        if (source.getCurrentLine().startsWith("System error message history:")) {
            while (Message.isMessage(source.nextLine())) {
                modstat.getErrorMessages().add(new Message(source.getCurrentLine()));
                foundSection = true;
            }
        }

        return foundSection;
    }
}