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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    //  Main firmware not found at startup.       08/24/10 08:22:47
    //  Brownout protection activated.            --/--/-- --:--:--
    private static final Matcher matcher =
            Pattern.compile("\\s\\s(.{1,41})\\s+([-\\d]{2}/[-\\d]{2}/[-\\d]{2})\\s([-\\d]{2}:[-\\d]{2}:[-\\d]{2})").matcher("");

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

    private String message;
    Date date;

    public static boolean isMessage(String line)
    {
        matcher.reset(line);
        return matcher.lookingAt();
    }

    public Message(String line)
    {
        if (!isMessage(line))
        {
            throw new IllegalArgumentException("Not a message");
        }

        message = matcher.group(1).trim();
        try {
            date = dateTimeFormat.parse(matcher.group(2) + " " + matcher.group(3));
        } catch (ParseException e) { } // leave null
    }

    public @NotNull String getMessage() {
        return message;
    }

    public @Nullable Date getDate() {
        return date;
    }

    public String toString() {
        return dateTimeFormat.format(date) + " - " + message;
    }

}