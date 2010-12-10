package com.controlj.green.modstat;

import com.controlj.green.addonsupport.access.*;
import com.controlj.green.addonsupport.access.aspect.ModuleStatus;
import com.controlj.green.addonsupport.access.util.Acceptors;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final List<String> stats = new ArrayList<String>();
        try {
            SystemConnection connection = DirectAccess.getDirectAccess().getUserSystemConnection(req);


            connection.runReadAction(FieldAccessFactory.newFieldAccess(), new ReadAction() {

                public void execute(@NotNull SystemAccess access) throws Exception {
                    Location netRoot = access.getTree(SystemTree.Network).getRoot();
                    /*
                    Collection<AnalogTrendSource> aspects = access.getGeoRoot().find(AnalogTrendSource.class, Acceptors.acceptAll());
                    for (AnalogTrendSource aspect : aspects) {
                        stats.add("loc="+aspect.getLocation().getDisplayPath());
                    }
                    */
                    Collection<ModuleStatus> aspects = netRoot.find(ModuleStatus.class, Acceptors.acceptAll());
                    System.out.println("Found "+aspects.size()+" aspects");
                    long start = System.currentTimeMillis();
                    for (ModuleStatus aspect : aspects) {
                        stats.add("from "+aspect.getLocation().getDisplayPath()+"\n"+aspect.getReportText());
                    }
                    long end = System.currentTimeMillis();
                    System.out.println("Gathering modstats took "+(end - start)+" mSec");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        ServletOutputStream out = resp.getOutputStream();
        out.println("<html><body>");
        for (String stat : stats) {
            out.println("<pre>");
            out.print(stat);
            out.println("</pre>");
            out.println("<br>");
        }
        out.println("</body></html>");

    }
}
