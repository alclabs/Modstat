<%@ page import="org.jetbrains.annotations.NotNull" %>
<%@ page import="com.controlj.green.addonsupport.access.*" %>
<%@ page import="com.controlj.green.modstat.work.ModstatWork" %>
<%@ page import="com.controlj.green.modstat.servlets.LongRunning" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.zip.ZipInputStream" %>
<%@ page import="java.util.zip.ZipEntry" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="com.controlj.green.modstat.work.RunnableProgress" %>
<%--
~ Copyright (c) 2011 Automated Logic Corporation
~
~ Permission is hereby granted, free of charge, to any person obtaining a copy
~ of this software and associated documentation files (the "Software"), to deal
~ in the Software without restriction, including without limitation the rights
~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
~ copies of the Software, and to permit persons to whom the Software is
~ furnished to do so, subject to the following conditions:
~
~ The above copyright notice and this permission notice shall be included in
~ all copies or substantial portions of the Software.
~
~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
~ THE SOFTWARE.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    final String locationString = request.getParameter("loc");
    SystemConnection connection = DirectAccess.getDirectAccess().getUserSystemConnection(request);
    final HttpServletRequest req = request;
    String text = connection.runReadAction(new ReadActionResult<String>() {
        @Override
        public String execute(@NotNull SystemAccess systemAccess) throws Exception {
            try {
                if (locationString != null) {
                    Location loc = systemAccess.getTree(SystemTree.Network).resolve(locationString);
                    String path = ModstatWork.getReferencePath(loc)+".txt";

                    RunnableProgress work = (RunnableProgress) LongRunning.getWork(req);
                    if (work==null || work.isAlive() || work.hasError()) {
                        return "Modstats not gathered yet";
                    }

                    InputStream in = work.getCache();
                    ZipInputStream zin = new ZipInputStream(in);
                    ZipEntry next;
                    while ((next = zin.getNextEntry()) != null) {
                        if (next.getName().equals(path))  {
                            StringWriter result = new StringWriter();
                            IOUtils.copy(new InputStreamReader(zin, "ISO-8859-1"), result);
                            return result.toString();
                        }
                    }

                    return "Could not find location";
                } else {
                    return "Error: missing location parameter";
                }
            } catch (UnresolvableException e) {
                return "Error: Unknown Location";
            }
        }
    });
%>
<html>
  <head><title>Modstat</title></head>
  <body>
  <pre><%= text%></pre>
  </body>
</html>