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

package com.controlj.green.modstat.checker

import spock.lang.Specification
import com.controlj.green.addonsupport.access.SystemAccess
import com.controlj.green.addonsupport.access.Location
import com.controlj.green.modstat.Modstat
import com.controlj.green.addonsupport.access.aspect.Device
import com.controlj.green.modstat.ModstatParser

/**
 * 
 */
class AddressBindingTest extends Specification {
    ModstatParser parser = new ModstatParser()
    SystemAccess access = Mock()
    Location location = Mock()
    com.controlj.green.modstat.checker.AddressBinding bindingCheck = new com.controlj.green.modstat.checker.AddressBinding("1");
    Device deviceAspect = Mock()

    def setup() {
        0 * access._
        location.getAspect(Device.class) >> deviceAspect
    }


    def "IP, Default Port, and matches"() {
        setup:
        deviceAspect.getMacAddressString() >> "172.31.217.213"

        when:
        Modstat m = parser.parse(new StringReader("ADDRESS BINDING Used: device instance 488001 is on network 48803 mac 172.31.217.213:0xbac0\n"))
        def result = bindingCheck.check(m, access, location);

        then:
        result.size() == 0
    }

    def "IP, Default Port, and wrong"() {
        setup:
        0 * access._
        location.getAspect(Device.class) >> deviceAspect
        deviceAspect.getMacAddressString() >>  "172.31.212.213"

        when:
        Modstat m = parser.parse(new StringReader("ADDRESS BINDING Used: device instance 488001 is on network 48803 mac 172.31.217.213:0xbac0\n"))
        def result = bindingCheck.check(m, access, location);


        then:
        result.size() == 1
        result[0].message == "Address in the field device (172.31.217.213) does not match the value in SiteBuilder (172.31.212.213)."
    }

    def "IP, Different Port, and matches"() {
        setup:
        deviceAspect.getMacAddressString() >> "172.31.222.35:47810"

        when:
        Modstat m = parser.parse(new StringReader("ADDRESS BINDING Used: device instance 488001 is on network 48803 mac 0xac1fde23bac2\n"))
        def result = bindingCheck.check(m, access, location);

        then:
        result.size() == 0
    }

    def "IP, Different Port, and wrong"() {
        setup:
        0 * access._
        location.getAspect(Device.class) >> deviceAspect
        deviceAspect.getMacAddressString() >>  "172.31.222.35:47810"

        when:
        Modstat m = parser.parse(new StringReader("ADDRESS BINDING Used: device instance 488001 is on network 48803 mac 0xac1fde23bac3\n"))
        def result = bindingCheck.check(m, access, location);


        then:
        result.size() == 1
        result[0].message == "Address in the field device (172.31.222.35:47811) does not match the value in SiteBuilder (172.31.222.35:47810)."
    }

    def "Arcnet and matches"() {
        setup:
        deviceAspect.getMacAddressString() >> "42"

        when:
        Modstat m = parser.parse(new StringReader("ADDRESS BINDING Used: device instance 488001 is on network 48803 mac 42\n"))
        def result = bindingCheck.check(m, access, location);

        then:
        result.size() == 0
    }

    def "Arcnet and wrong"() {
        setup:
        deviceAspect.getMacAddressString() >> "43"

        when:
        Modstat m = parser.parse(new StringReader("ADDRESS BINDING Used: device instance 488001 is on network 48803 mac 42\n"))
        def result = bindingCheck.check(m, access, location);

        then:
        result.size() == 1
        result[0].message == "Address in the field device (42) does not match the value in SiteBuilder (43)."
    }
}
