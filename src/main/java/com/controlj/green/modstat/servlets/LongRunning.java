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
import com.controlj.green.addonsupport.InvalidConnectionRequestException;
import com.controlj.green.addonsupport.access.DirectAccess;
import com.controlj.green.addonsupport.access.SystemConnection;
import com.controlj.green.modstat.work.ModstatWork;
import com.controlj.green.modstat.work.RunnableProgress;
import com.controlj.green.modstat.work.TestWork;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LongRunning extends HttpServlet {
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_PROCESS = "process";
    private static final String PARAM_ID = "id";
    private static final String PARAM_DEVICES = "devices";

    private static final String ACTION_START = "start";
    private static final String ACTION_STATUS = "status";
    private static final String PROCESS_TIMER = "timer";
    private static final String PROCESS_MODSTAT = "modstat";

    private boolean test = false;

    private static final String ATTRIB_WORK = "work";

    private static ServletContext sc;
    private static final String TEST_SOURCE = "WEB-INF/modstat.zip";


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        sc = config.getServletContext();
        if (test) {
            test = getFileInWebApp(TEST_SOURCE).exists();
            if (!test) {
                System.err.println("Couldn't find test modstat source");
            }
        }
    }

    private File getFileInWebApp(String path) {
        return new File(sc.getRealPath(path));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String actionString = req.getParameter(PARAM_ACTION);
            if (actionString == null || actionString.isEmpty()) {
                throw new ServletException("Missing parameter: action");
            }
            if (actionString.equals(ACTION_START)) {
                String processString = req.getParameter(PARAM_PROCESS);
                if (processString == null || processString.isEmpty()) {
                    throw new ServletException("Missing parameter: process");
                }

                RunnableProgress oldWork = (RunnableProgress) req.getSession().getAttribute(ATTRIB_WORK);
                if (oldWork != null) {
                    oldWork.interrupt();
                }
                /*
                if (processString.equals(PROCESS_TIMER)) {

                    TestWork work = new TestWork();
                    req.getSession().setAttribute(ATTRIB_WORK, work);
                    work.start();
                } else
                */
                if (processString.equals(PROCESS_MODSTAT)) {
                    String idString = req.getParameter(PARAM_ID);
                    if (idString==null || idString.isEmpty()) {
                        throw new ServletException("Missing parameter: id");
                    }

                    String jsonDevices = req.getParameter(PARAM_DEVICES);
                    List<String> deviceNames = new ArrayList<String>();

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(jsonDevices);
                        for (int i=0; i<jsonArray.length(); i++) {
                            deviceNames.add(jsonArray.getString(i));
                        }
                    } catch (JSONException e) {
                        throw new ServletException("Missing parameter: devices");
                    }

                    SystemConnection connection = null;
                    try {
                        connection = DirectAccess.getDirectAccess().getUserSystemConnection(req);
                    } catch (InvalidConnectionRequestException e) {
                        throw new ServletException("Error getting connection:"+e.getMessage());
                    }
                    RunnableProgress work;
                    if (test) {
                        work = new TestWork(getFileInWebApp(TEST_SOURCE));
                    } else {
                        work = new ModstatWork(connection, idString, deviceNames);
                    }
                    req.getSession().setAttribute(ATTRIB_WORK, work);
                    work.start();
                } else {
                    throw new ServletException("Unknown process:"+processString);
                }
            } else if (actionString.equals(ACTION_STATUS)) {
                RunnableProgress work = (RunnableProgress) req.getSession().getAttribute("work");
                JSONObject result = new JSONObject();
                resp.setContentType("application/json");
                String errorMessage = null;
                int percent = 0;
                boolean stopped = false;

                if (work == null) {
                    throw new ServletException("Can't get status, nothing started");
                } else if (work.hasError()) {
                    throw new ServletException(work.getError().getMessage());
                } else {
                    if (work.isAlive()) {
                        percent = work.getProgress();
                    } else {
                        percent = 100;
                        stopped = true;
                    }
                }
                try {
                    result.put("percent", percent);
                    result.put("stopped", stopped);
                    if (errorMessage != null) {
                        result.put("error", errorMessage);
                    }

                    result.write(resp.getWriter());
                } catch (JSONException e) {
                    throw new ServletException("Error writing result:"+e.getMessage());
                }
            }
        } catch (ServletException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            System.err.println("Add-On Error in "+ AddOnInfo.getAddOnInfo().getName()+":"+e.getMessage());
            e.printStackTrace(new PrintWriter(System.err));
        }
    }

    public static Object getWork(HttpServletRequest req) {
        return req.getSession().getAttribute(ATTRIB_WORK);
    }

    private void startTimer() {

    }
}
