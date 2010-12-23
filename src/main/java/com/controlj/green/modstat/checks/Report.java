/*
 * Copyright (c) 2010 Automated Logic Corporation
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
import com.controlj.green.addonsupport.access.aspect.ModuleStatus;
import com.controlj.green.addonsupport.access.util.Acceptors;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.ModstatParser;
import com.controlj.green.modstat.checker.*;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Report {
    private int modstatCounts = 0;

    public int getCount() {
        return modstatCounts;
    }

    public List<ReportLocation> runReport(final HttpServletRequest req) throws InvalidConnectionRequestException, SystemException, ActionExecutionException {
        SystemConnection connection = DirectAccess.getDirectAccess().getUserSystemConnection(req);

        final List<ReportLocation>locations = new ArrayList<ReportLocation>();

        connection.runReadAction(new ReadAction() {
            @Override

            public void execute(@NotNull SystemAccess access) throws Exception {
                String idParam = req.getParameter("id");
                Location start;
                if (idParam!=null && idParam.length()>0) {
                    start = access.getTree(SystemTree.Network).resolve(idParam);
                } else {
                    start = access.getNetRoot();
                }

                Collection<ModuleStatus> aspects = start.find(ModuleStatus.class, Acceptors.acceptAll());
                for (ModuleStatus aspect : aspects) {
                    Modstat modstat = ModstatParser.parse(new StringReader(aspect.getReportText()));
                    modstatCounts++;

                    ReportLocation location = runChecks(new ReportLocation(aspect.getLocation()), modstat);
                    if (location != null) {
                        locations.add(location);
                    }
                }
            }
        });

        return locations;
    }

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
    
    private static ReportLocation runChecks(ReportLocation location, Modstat modstat) {
        ReportLocation result = null;
        for (Checker checker : checkers) {
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
        return result;
    }


    private static final Checker[] checkers = new Checker[] {
        new NoModstat(),
        new WatchdogTimeouts(),
        new ErrorMessages(),
        new ErrorCount(),
        new ProgramsRunning(),
        new BACnetErrors(),
        new DBFree(),
        new HeapFree(),
        new ArcnetReconfig(),
        new Ethernet()
    };


}
