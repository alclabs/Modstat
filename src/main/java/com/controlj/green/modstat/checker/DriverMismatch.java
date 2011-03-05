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

package com.controlj.green.modstat.checker;

import com.controlj.green.addonsupport.access.Location;
import com.controlj.green.addonsupport.access.NoSuchAspectException;
import com.controlj.green.addonsupport.access.SystemAccess;
import com.controlj.green.addonsupport.access.aspect.BACnetObjectSource;
import com.controlj.green.addonsupport.access.aspect.Device;
import com.controlj.green.addonsupport.access.aspect.Driver;
import com.controlj.green.addonsupport.access.bacnet.BACnetObject;
import com.controlj.green.modstat.FirmwareVersion;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverMismatch extends BaseChecker {
    static private Pattern dbPattern = Pattern.compile("(\\d+)\\-(\\d+)\\-(\\d+)");
    static private Pattern fieldPattern = Pattern.compile("(\\d+)\\.(\\d+):(\\d+)");

    public DriverMismatch(String id) {
        super(id);
        name = "Driver Mismatch";
        description = "Does the driver in the module match the one in the database";
    }


    @Override
    public List<ReportRow> check(Modstat modstat, SystemAccess access, Location location) {
        List<ReportRow> result = null;

        if (modstat.hasFirmwareVersions()) {
            List<FirmwareVersion> versions = modstat.getFirmwareVersions();
            FirmwareVersion version = getNonBootDriver(versions);

            if (version != null) {
                try {
                    Driver driver = location.getAspect(Driver.class);
                    String dbVersion = driver.getVersion();
                    // in form of 4-02-094
                    String fieldVersion = version.getVersion();
                    // in form of 4-02:094

                    int dba, dbb, dbc;
                    int fa, fb, fc;

                    Matcher dbMatcher = dbPattern.matcher(dbVersion);
                    Matcher fieldMatcher = fieldPattern.matcher(fieldVersion);
                    if (dbMatcher.matches() && fieldMatcher.matches()) {
                        dba = Integer.parseInt(dbMatcher.group(1));
                        dbb = Integer.parseInt(dbMatcher.group(2));
                        dbc = Integer.parseInt(dbMatcher.group(3));

                        fa = Integer.parseInt(fieldMatcher.group(1));
                        fb = Integer.parseInt(fieldMatcher.group(2));
                        fc = Integer.parseInt(fieldMatcher.group(3));

                        if ((dba != fa) || (dbb != fb) || (dbc != fc)) {
                            result = new ArrayList<ReportRow>();
                            result.add(ReportRow.error("Version of driver in database ("+dbVersion+") " +
                                    "is not the same as reported in Modstat ("+ fieldVersion+")"));

                        }
                    } else {
                        result = new ArrayList<ReportRow>();
                        result.add(ReportRow.error("Can't parse driver version.  db:"+dbVersion+" field:"+fieldVersion));
                    }
                } catch (NoSuchAspectException e) {
                    result = new ArrayList<ReportRow>();
                    result.add(ReportRow.error("Can't determine driver version from database for device at: "+location.getDisplayPath()));
                }
            }
        }
        return result;
    }

    public static @Nullable FirmwareVersion getNonBootDriver(List<FirmwareVersion> versions) {
        for (FirmwareVersion version : versions) {
            String driverName = version.getName();
            String firstFour = driverName.substring(0, 4);
            if (!firstFour.equalsIgnoreCase("boot")) {
                return version;
            }
        }
        return null;        
    }
}