package com.controlj.green.modstat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineSource {
    private static Matcher blankLine = Pattern.compile("\\A\\s*\\z").matcher("");


    private BufferedReader in;
    private String currentLine;
    private boolean empty = false;
    boolean skippedBlanks = false;

    public LineSource(Reader rdr)
    {
        in = new BufferedReader(rdr);
        nextLine();
    }

    public String nextLine()
    {
        String result = null;

        try {
            while ((result = in.readLine()) != null)
            {
                if (blankLine.reset(result).matches()) {
                    skippedBlanks = true;
                } else {
                    currentLine = result;
                    return currentLine;
                }
            }
        } catch (IOException e) { } // ignore and return null

        empty = true;
        return null;
    }

    public String getCurrentLine() {
        return currentLine;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isSkippedBlanks() {
        return skippedBlanks;
    }

}
