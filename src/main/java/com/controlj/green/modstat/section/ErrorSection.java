package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorSection extends ModstatSection {
    private static final Matcher validLine = Pattern.compile("").matcher("");


    public ErrorSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    @Override
    public boolean lookForSection() throws StopParseException {

        if (source.getCurrentLine().startsWith("Error getting modstat")) {
            modstat.setErrorReading(true);
            throw new StopParseException(source.getCurrentLine());
        }

        return false;
    }
}