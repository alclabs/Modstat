<%@ page import="com.controlj.green.modstat.checks.Report" %>
<%@ page import="com.controlj.green.modstat.checks.Checker" %>
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
    Checker[] checkers = Report.getCheckers(request);
%>
<style type="text/css">
    .enable { width: 2em; }
    .name   {
        font-weight: bold;
        width: 50px;
        white-space:nowrap;
        padding-right:10px;
    }
    .edit   { }
    .config { width: 100% }
    .odd    { background: #eee; }
    .config td { vertical-align:baseline; }
</style>
<script type="text/javascript">
    function serializeAll(els) {
        var result = ""
        els.each(function(index, element) {
            var el = $(element)
            if (el.attr("type") == "checkbox") {
                result+=(escape(el.attr('name'))+"="+(el.attr('checked') ? 'true' : 'false') )
            } else {
                result+=el.serialize()
            }
            if(index < els.length-1) {
                result += "&"
            }
        })
        return result
    }
    function initConfig() {
        $(".config input").bind("change", function() {
            //alert("clicked")
            $.get('servlets/saveconfig?' + serializeAll($(".config input")))
            /*
            var el = $(this);
            if (el.attr("type") == "checkbox") {
                alert(el.attr('name')+" = "+el.attr('checked'))
            } else {
                alert(el.attr('name')+"= " +el.attr('value'))
            }
            */
        })

    }
</script>

<table class="config" cellpadding="0" cellspacing="0">
<%
    int i=0;
    for (Checker checker : checkers) {
%>
    <tr class="<%= i%2 == 0 ? "odd":""%>">
        <td class="enable"><input type="checkbox" <%= checker.getFieldValue(Checker.FIELD_ENABLE).equals("true")?"checked=\"true\" ":""%> name="<%= checker.getFieldId(Checker.FIELD_ENABLE)%>"/></td>
        <td class="name" title="<%= checker.getDescription()%>"><%= checker.getName() %></td>
        <td class="edit"><%= checker.getConfigHTML()%></td>
    </tr>
<%
        i++;
    }
%>
</table>