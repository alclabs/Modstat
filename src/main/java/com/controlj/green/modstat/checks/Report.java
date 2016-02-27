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

import com.controlj.green.addonsupport.AddOnInfo;
import com.controlj.green.addonsupport.InvalidConnectionRequestException;
import com.controlj.green.addonsupport.Version;
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
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Report {
    private static final String ATTRIB_CHECKER = "checkers";
    private int modstatCounts = 0;
    private Class[] checkerClasses;

    public Report(Version apiVersion) {
        checkerClasses = getCheckerClasses(apiVersion);
    }

    public int getCount() {
        return modstatCounts;
    }

    public List<ReportLocation> runReport(final HttpServletRequest req) throws InvalidConnectionRequestException, SystemException, ActionExecutionException {
        SystemConnection connection = DirectAccess.getDirectAccess().getUserSystemConnection(req);

        final List<ReportLocation>locations = new ArrayList<ReportLocation>();

        RunnableProgress work = (RunnableProgress) LongRunning.getWork(req);
        if (work.isAlive() || work.hasError()) {
            System.err.println("Modstat Addon- Report run with no modstats");
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
                        if (!nextEntry.isDirectory()) {
                            String path = textName.substring(0, textName.length()-4);   // remove the trailing ".txt"
                            Location loc = access.getNetRoot().getDescendant(path);

                            Modstat modstat = ModstatParser.parse(new InputStreamReader(zin));
                            modstatCounts++;

                            ReportLocation reportLocation = runChecks(checkers, new ReportLocation(loc), modstat, access, loc);
                            if (reportLocation != null) {
                                locations.add(reportLocation);
                            }
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
    
    private ReportLocation runChecks(Checker[] checkers, ReportLocation reportLocation, Modstat modstat, SystemAccess access, Location location) {
        ReportLocation result = null;
        for (Checker checker : checkers) {
            if (checker.isEnabled()) {
                List<ReportRow> rows = null;
                try {
                    rows = checker.check(modstat, access, location);
                } catch (Throwable th) {
                    result = reportLocation;
                    result.addRow(ReportRow.error("Error running check using "+checker.getClass().getName()+". "+th.getMessage()));
                }
                if (rows != null && rows.size() > 0) {
                    if (result == null) {
                        result =  reportLocation;
                    }
                    result.addRows(rows);
                }
            }
        }
        return result;
    }


    public Checker[] getCheckers(HttpServletRequest req) {
        Checker[] result = (Checker[]) req.getSession().getAttribute(ATTRIB_CHECKER);
        if (result == null) {
            result = newCheckers();
            req.getSession().setAttribute(ATTRIB_CHECKER, result);
        }
        return result;
    }

    private Checker[] newCheckers() {
        Checker[] result = new Checker[checkerClasses.length];
        for (int i=0; i<checkerClasses.length; i++) {
            try {
                Constructor ctor = checkerClasses[i].getConstructor(String.class);
                result[i] = (Checker) ctor.newInstance(Integer.toString(i));
            } catch (Exception e) {
                throw new RuntimeException("Can't create checker class", e);
            }
        }
        return result;
    }


    private Class[] getCheckerClasses(Version apiVersion) {
        List<Class> l = new ArrayList<Class>();
        l.add(NoModstat.class);
        l.add(NoDriver.class);
        l.add(DeviceIDMismatch.class);
        if (apiVersion.getMajorVersionNumber() >1 ||
           (apiVersion.getMajorVersionNumber() == 1 && apiVersion.getMinorVersionNumber() >= 3))
        {
            l.add(AddressBinding.class);
        }
        l.add(DriverMismatch.class);
        l.add(ArcnetReconfigCause.class);
        l.add(ArcnetReconfig.class);
        l.add(ErrorMessages.class);
        l.add(WarningMessages.class);
        l.add(ProgramsRunning.class);
        l.add(DBFree.class);
        l.add(HeapFree.class);
        l.add(FlashStorageFree.class);
        l.add(BACnetErrors.class);
        l.add(Ethernet.class);
        l.add(NoPrograms.class);
        l.add(ErrorCount.class);
        l.add(WatchdogTimeouts.class);
        l.add(ParsingError.class);
        l.add(MEBadCapSerial.class);

        Class[] classes = new Class[l.size()];
        classes = l.toArray(classes);

        return classes;
    }


    /*new Class[] {
        NoModstat.class,
        NoDriver.class,
        DeviceIDMismatch.class,

        AddressBinding.class,
        DriverMismatch.class,
        ArcnetReconfigCause.class,
        ArcnetReconfig.class,
        ErrorMessages.class,
        WarningMessages.class,
        ProgramsRunning.class,
        DBFree.class,
        HeapFree.class,
        FlashStorageFree.class,
        BACnetErrors.class,
        Ethernet.class,
        NoPrograms.class,
        ErrorCount.class,
        WatchdogTimeouts.class,
        ParsingError.class
    };
    */
}
