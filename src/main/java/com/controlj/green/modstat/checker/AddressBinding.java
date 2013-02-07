/*
 * Copyright (c) 2013 Automated Logic Corporation
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
import com.controlj.green.addonsupport.access.aspect.Device;
import com.controlj.green.addonsupport.access.aspect.Driver;
import com.controlj.green.modstat.Modstat;
import com.controlj.green.modstat.checks.ReportRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressBinding extends BaseChecker {
    public AddressBinding(String id) {
        super(id);
        name = "Address Binding";
        description = "Check if field device instance and address match SiteBuilder (requires WebCTRL 6+)";
        setEnabled(true);  // defaults to disabled
    }

    @Override
    public List<ReportRow> check(Modstat modstat, SystemAccess access, Location location) {
        List<ReportRow> result = null;

        if (modstat.hasAddressBinding()) {
            com.controlj.green.modstat.AddressBinding addressBinding = modstat.getAddressBinding();

            result = new ArrayList<ReportRow>();

            try {
                Device device = location.getAspect(Device.class);
                String dbAddress = device.getMacAddressString();
                String fieldAddress = addressBinding.getMac();
                if (fieldAddress.contains(":")) {
                    fieldAddress = fieldAddress.substring(0,fieldAddress.indexOf(":"));
                }
                if (!dbAddress.equals(fieldAddress)) {
                    result.add(ReportRow.error("Address in the field device ("+fieldAddress+") does not match the value in SiteBuilder ("+
                            dbAddress+")."));
                }
            } catch (NoSuchAspectException e) {
                result.add(ReportRow.error("Can't determine device address from database for device at: "+location.getDisplayPath() ));
            }

        }
        return result;
    }
}
