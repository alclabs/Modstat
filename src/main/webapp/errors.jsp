<%@ page import="com.controlj.green.modstat.checks.Report" %>
<%@ page import="com.controlj.green.modstat.checks.ReportLocation" %>
<%@ page import="java.util.List" %>
<%@ page import="com.controlj.green.modstat.checks.ReportRow" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  Report report = new Report();
  List<ReportLocation> locations = report.runReport(request);
%>

<p>Gathered <%= report.getCount() %> modstat(s).  Issues at <%= locations.size() %> locations.</p>
<table cellpadding="0" cellspacing="0">
  <%
      for (ReportLocation location : locations) {
  %>
  <tr><td colspan="2"><%= location.getDisplayPath() %></td> </tr>
  <%
      for (ReportRow row : location.getRows()) {
  %>
  <tr>
      <td class="spacer">&nbsp;</td>
      <td class="<%= row.getLevel() == ReportRow.Level.ERROR ? "error" : "warning"%>"><%= row.getMessage()%></td>
  </tr>
  <%
      }
  %>
  <%
      }
  %>
</table>
