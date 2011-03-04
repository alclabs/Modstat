/*
 * Copyright (c) 2011 Automated Logic Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
    private StringBuilder text = new StringBuilder();
    int lineNumber = 0;

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
                lineNumber++;
                text.append(in + "\n");
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

    public String getText() {
        return text.toString();
    }

    public int getCurrentLineNumber() {
        return lineNumber;
    }
}
