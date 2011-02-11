<%@ page import="com.controlj.green.modstat.checks.Report" %>
<%@ page import="com.controlj.green.modstat.checks.ReportLocation" %>
<%@ page import="java.util.List" %>
<%@ page import="com.controlj.green.modstat.checks.ReportRow" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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

<%
  Report report = new Report();
  List<ReportLocation> locations = report.runReport(request);
%>
<style type="text/css">
    #report .error {
        background-image:url('error.png');
        background-repeat:no-repeat;
        background-position:10px 0px;
    }

    #report .warning {
        background-image:url('dot.png');
        background-repeat:no-repeat;
        background-position:10px 0px;
    }

    #report .msg {
        padding-left:30px;
        line-height:16px;
    }

    #report .path {
        font-weight:bold;
        padding-top:5px;
    }

    #legend {
        white-space:nowrap;
        float:right;
        vertical-align:bottom;
        padding-top:0px;
        margin-top:8px;
    }

</style>
<p id="legend"><img src="error.png" style="position:relative; top:4px;"/> = Error</p>
<p>Gathered <%= report.getCount() %> modstat(s).  Issues at <%= locations.size() %> locations.</p>
<div id="report">
  <%
      for (ReportLocation location : locations) {
  %>
  <a class="path" target="_blank" href="text.jsp?loc=<%= URLEncoder.encode(location.getLookup(),"UTF-8") %>">
      <%= location.getDisplayPath() %>
  </a>
  <%
      for (ReportRow row : location.getRows()) {
  %>
        <div class="msg <%= row.getLevel() == ReportRow.Level.ERROR ? "error" : "warning"%>"><%= row.getMessage()%></div>
  <%
      }
  }
  %>
</div>