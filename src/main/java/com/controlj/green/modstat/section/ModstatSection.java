package com.controlj.green.modstat.section;

import com.controlj.green.modstat.LineSource;
import com.controlj.green.modstat.Modstat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

public abstract class ModstatSection {
    protected static final SimpleDateFormat dtFormatyyyyss = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    protected static final SimpleDateFormat dtFormatyyss = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
    protected static final SimpleDateFormat dtFormatyy = new SimpleDateFormat("MM/dd/yy HH:mm");
    protected static final SimpleDateFormat dFormatyyyy = new SimpleDateFormat("MM/dd/yyyy");

    protected LineSource source;
    protected Modstat modstat;

    public ModstatSection(LineSource source, Modstat modstat) {
        this.source = source;
        this.modstat = modstat;
    }

    /**
     * See if the current line is starting this section.  If so, consume the section lines and update the modstat.
     *
     * @return true if this section was found
     */
    public abstract boolean lookForSection() throws StopParseException;

    protected String[] matchesStart(String line, Matcher m)
    {
        String[] result = null;
        if (line!=null && m.reset(line).lookingAt())
        {
            int groups = m.groupCount();
            result = new String[groups];
            for (int i=0; i<groups; i++)
            {
                result[i] = m.group(i+1);
            }
        }
        return result;
    }

    protected Date convertDate(String date, String time, DateFormat format) throws ParseException {
        return format.parse(date+" "+time);
    }



}
