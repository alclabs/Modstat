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

package com.controlj.green.modstat

import spock.lang.Specification
import com.controlj.green.modstat.servlets.SaveConfigServlet
import javax.servlet.http.HttpSession
import com.controlj.green.modstat.checks.Checker
import com.controlj.green.modstat.checks.Report
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.controlj.green.modstat.checker.ArcnetReconfig

class SaveConfigServletTest extends Specification {

    def getIndex() {
        setup:
        SaveConfigServlet servlet = new SaveConfigServlet()

        expect:
        servlet.getIndex("4:testing") == 4
        servlet.getIndex("42:test:stuff") == 42
        servlet.getIndex("03:leadingzero") == 3

        when:
        servlet.getIndex("fred:value")
        then:
        thrown(RuntimeException)

        when:
        servlet.getIndex(":42:value")
        then:
        //thrown(RuntimeException)
        RuntimeException e = thrown()
        e.message.startsWith("Modstat addon: Error parsing configuration parameters.")
    }

    def getFieldName() {
        setup:
        SaveConfigServlet servlet = new SaveConfigServlet()

        expect:
        servlet.getFieldName("4:testing") == "testing"
        servlet.getFieldName("03:test:stuff") == "test:stuff"

        when:
        servlet.getFieldName("43:")
        then:
        thrown(RuntimeException)

        when:
        servlet.getFieldName("")
        then:
        thrown(RuntimeException)
    }

    def doGet() {
        setup:
        SaveConfigServlet servlet = new SaveConfigServlet()
        Checker[] checkers = Report.newCheckers()
        HttpSession mSession = Mock()
        mSession.getAttribute(Report.ATTRIB_CHECKER) >> checkers
        HttpServletRequest req = Mock()
        HttpServletResponse resp = Mock()
        req.getSession() >> mSession

        when:
        req.getParameterMap() >> ["0:enable":(String[])["true"], "1:enable":(String[])["false"]]
        servlet.doGet(req, resp)

        then:
        checkers[0].enabled == true
        checkers[1].enabled == false        
    }

    def processParameters() {
        setup:
        SaveConfigServlet servlet = new SaveConfigServlet()
        Checker[] checkers

        // check default state
        when:
        checkers = Report.newCheckers()
        then:
        checkers[0].enabled == true
        checkers[1].enabled == true
        checkers[2].enabled == true
        checkers[3].enabled == true
        checkers[4].enabled == true

        // normal
        when:
        checkers = Report.newCheckers()
        servlet.processParameters( ["0:enable":(String[])["true"], "1:enable":(String[])["false"], "2:enable":(String[])["false"]], checkers)
        then:
        checkers[0].enabled == true
        checkers[1].enabled == false
        checkers[2].enabled == false

        // error in the middle, just skip
        when:
        checkers = Report.newCheckers()
        servlet.processParameters( ["0:enable":(String[])["true"], "1:enable":(String[])["fred"], "2:enable":(String[])["false"]], checkers)
        then:
        checkers[0].enabled == true
        checkers[1].enabled == true
        checkers[2].enabled == false

        // Int parameter
        when:
        checkers = Report.newCheckers()
        servlet.processParameters( ["0:enable":(String[])["true"], "1:enable":(String[])["false"],
                "2:enable":(String[])["false"], ("8:"+ArcnetReconfig.FIELD_WARN_LIMIT) :(String[])["42"]], checkers)
        then:
        checkers[8].name == "Arcnet Reconfigs"
        checkers[8].warningLimit == 42
    }
}
