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

package com.controlj.green.modstat.servlets;

import com.controlj.green.addonsupport.AddOnInfo;
import com.controlj.green.modstat.checker.InvalidFieldNameException;
import com.controlj.green.modstat.checker.InvalidFieldValueException;
import com.controlj.green.modstat.checks.Checker;
import com.controlj.green.modstat.checks.Report;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class SaveConfigServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Report report = new Report(AddOnInfo.getAddOnInfo().getApiVersion());
        execute(req, resp, report);
    }

    private void execute(HttpServletRequest req, HttpServletResponse resp, Report report) throws ServletException, IOException {
        Checker[] checkers = report.getCheckers(req);
        Map<String, String[]> map = req.getParameterMap();
        processParameters(map, checkers);
    }

    private void processParameters(Map<String,String[]> params, Checker[] checkers) {
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            int index = getIndex(entry.getKey());
            String fieldName = getFieldName(entry.getKey());
            try {
                checkers[index].setFieldValue(fieldName, entry.getValue()[0]);
            } catch (InvalidFieldValueException e) {
                System.err.println("Error in modstat addon: SaveConfigServlet.doGet request has invalid field value:"+e.getMessage());
            } catch (InvalidFieldNameException e) {
                System.err.println("Error in modstat addon: SaveConfigServlet.doGet request has invalid field name:"+e.getMessage());
            }
        }
    }

    private int getIndex(String param) {
        int index = param.indexOf(":");
        if (index > 0) {
            String intString = param.substring(0, index);
            try {
                return Integer.parseInt(intString);
            } catch (NumberFormatException e) {} // ignore and handle error below
        }
        System.err.println("Modstat addon: Error parsing configuration parameters.  Index not a number for:"+param);
        throw new RuntimeException("Modstat addon: Error parsing configuration parameters.  Index not a number for:"+param);
    }

    private String getFieldName(String param) {
        int index = param.indexOf(":");
        if (index > 0 && (index < param.length()-1)) {
            return param.substring(index+1);
        }
        System.err.println("Modstat addon: Error parsing configuration parameters.  missing field name in:"+param);
        throw new RuntimeException("Modstat addon: Error parsing configuration parameters.  missing field name in:"+param);
    }
}
