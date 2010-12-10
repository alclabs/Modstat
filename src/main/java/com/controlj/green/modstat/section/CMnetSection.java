package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CMnetSection extends ModstatSection {
    private static final Matcher validLine = Pattern.compile("").matcher("");


    public CMnetSection(LineSource source, Modstat modstat) {
        super(source, modstat);
    }

    /*
    This MUST be the first section in the parser's list.  It only checks that
    the source had leading blank lines, sets a flag in the modstat, and stops parsing
     */
    @Override
    public boolean lookForSection() throws StopParseException {
        if (source.isSkippedBlanks()) {
            modstat.setCmNet(true);
            throw new StopParseException("CMNet not supported");
        }
        
        return true;
    }
}