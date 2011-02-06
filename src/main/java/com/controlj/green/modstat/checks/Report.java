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

package com.controlj.green.modstat.checks;

import com.controlj.green.addonsupport.InvalidConnectionRequestException;
import com.controlj.green.addonsupport.access.*;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.ModstatParser;
import com.controlj.green.modstat.checker.*;
import com.controlj.green.modstat.servlets.LongRunning;
import com.controlj.green.modstat.work.RunnableProgress;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Report {
    private static final String ATTRIB_CHECKER = "checkers";
    private int modstatCounts = 0;

    public int getCount() {
        return modstatCounts;
    }

    public List<ReportLocation> runReport(final HttpServletRequest req) throws InvalidConnectionRequestException, SystemException, ActionExecutionException {
        SystemConnection connection = DirectAccess.getDirectAccess().getUserSystemConnection(req);

        final List<ReportLocation>locations = new ArrayList<ReportLocation>();

        RunnableProgress work = (RunnableProgress) LongRunning.getWork(req);
        if (work.isAlive() || work.hasError()) {
            System.err.println("Report run with no modstats");
        } else {

            final InputStream in = work.getCache();

            connection.runReadAction(new ReadAction() {
                @Override

                public void execute(@NotNull SystemAccess access) throws Exception {

                    Checker checkers[] = getCheckers(req);

                    ZipInputStream zin = new ZipInputStream(in);
                    ZipEntry nextEntry = null;
                    while((nextEntry = zin.getNextEntry()) != null) {
                        String textName = nextEntry.getName();
                        String path = textName.substring(0, textName.length()-4);   // remove the trailing ".txt"
                        Location loc = access.getNetRoot().getDescendant(path);

                        Modstat modstat = ModstatParser.parse(new InputStreamReader(zin));
                        modstatCounts++;

                        ReportLocation reportLocation = runChecks(checkers, new ReportLocation(loc), modstat);
                        if (reportLocation != null) {
                            locations.add(reportLocation);
                        }
                    }
                }
            });
        }

        return locations;
    }

    /*
    public static List<ReportLocation> testReport() {
        List<ReportLocation>locations = new ArrayList<ReportLocation>();

        File sampleDir = new File("webserver/webapps/modstat/WEB-INF/samples");
        File[] files = sampleDir.listFiles();
        for (File file : files) {
            ReportLocation loc = new ReportLocation(file.getName(), file.getPath(), file.getPath());

            try {
                Modstat modstat = ModstatParser.parse(new FileReader(file));
                ReportLocation location = runChecks(loc, modstat);
                if (location != null) {
                    locations.add(location);
                }
            } catch (IOException e) { }  // intentionally ignore
        }
        return locations;
    }
    */
    
    private static ReportLocation runChecks(Checker[] checkers, ReportLocation location, Modstat modstat) {
        ReportLocation result = null;
        for (Checker checker : checkers) {
            if (checker.isEnabled()) {
                List<ReportRow> rows = null;
                try {
                    rows = checker.check(modstat);
                } catch (Throwable th) {
                    result = location;
                    result.addRow(ReportRow.error("Error running check using "+checker.getClass().getName()+". "+th.getMessage()));
                }
                if (rows != null) {
                    if (result == null) {
                        result =  location;
                    }
                    result.addRows(rows);
                }
            }
        }
        return result;
    }


    public static Checker[] getCheckers(HttpServletRequest req) {
        Checker[] result = (Checker[]) req.getSession().getAttribute(ATTRIB_CHECKER);
        if (result == null) {
            result = newCheckers();
            req.getSession().setAttribute(ATTRIB_CHECKER, result);
        }
        return result;
    }

    private static Checker[] newCheckers() {
        Class classes[] = getCheckerClasses();
        Checker[] result = new Checker[classes.length];
        for (int i=0; i<classes.length; i++) {
            try {
                Constructor ctor = classes[i].getConstructor(String.class);
                result[i] = (Checker) ctor.newInstance(Integer.toString(i));
            } catch (Exception e) {
                throw new RuntimeException("Can't create checker class", e);
            }
        }
        return result;
    }


    private static Class[] getCheckerClasses() {
        return checkerClasses;
    }

    private static Class[] checkerClasses = new Class[] {
        NoModstat.class,
        WatchdogTimeouts.class,
        ErrorMessages.class,
        ErrorCount.class,
        ProgramsRunning.class,
        BACnetErrors.class,
        DBFree.class,
        HeapFree.class,
        ArcnetReconfig.class,
        Ethernet.class
    };
}
