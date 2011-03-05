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
import com.controlj.green.addonsupport.access.bacnet.BACnetObject;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;

public class DeviceIDMismatch extends BaseChecker {
    public DeviceIDMismatch(String id) {
        super(id);
        name = "Device ID Mismatch";
        description = "Check if the module's device ID matches the one in the database";
    }


    @Override
    public List<ReportRow> check(Modstat modstat, SystemAccess access, Location location) {
        List<ReportRow> result = null;

        if (modstat.hasDeviceID()) {
            int fieldInstance = modstat.getDeviceInstance();
            try {
                BACnetObjectSource objectSource = location.getAspect(BACnetObjectSource.class);
                List<BACnetObject> objects = objectSource.getObjects();
                BACnetObject deviceObject = null;
                for (BACnetObject object : objects) {
                    if (object.getObjectTypeNumber() == 8) { // if device object
                        deviceObject = object;
                        break;
                    }
                }
                if (deviceObject == null) {
                    result = new ArrayList<ReportRow>();
                    result.add(ReportRow.error("Can't find device ID for device at: "+location.getDisplayPath()));
                } else if (fieldInstance != deviceObject.getObjectInstance()) {
                    result = new ArrayList<ReportRow>();
                    result.add(ReportRow.error("Device Instance in modstat ("+fieldInstance+
                    ") does not match the device instance in the database ("+ deviceObject.getObjectInstance() +")"));
                }
            } catch (NoSuchAspectException e) {
                result = new ArrayList<ReportRow>();
                result.add(ReportRow.error("Can't determine device ID for device at: "+location.getDisplayPath()));
            }
        }
        return result;
    }
}