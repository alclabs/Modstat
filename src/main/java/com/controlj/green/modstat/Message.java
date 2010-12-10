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