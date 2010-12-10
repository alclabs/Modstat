package com.controlj.green.modstat.servlets;

import com.controlj.green.addonsupport.access.*;
import com.controlj.green.addonsupport.access.aspect.ModuleStatus;
import com.controlj.green.addonsupport.access.util.Acceptors;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ModstatZipServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/zip");
        resp.setHeader("Content-disposition", "attachment;filename=modstat.zip");
        final ZipOutputStream zout = new ZipOutputStream(resp.getOutputStream());
        try {
            SystemConnection connection = DirectAccess.getDirectAccess().getUserSystemConnection(req);


            connection.runReadAction(FieldAccessFactory.newFieldAccess(), new ReadAction() {

                public void execute(@NotNull SystemAccess access) throws Exception {
                    Location netRoot = access.getTree(SystemTree.Network).getRoot();

                    Collection<ModuleStatus> aspects = netRoot.find(ModuleStatus.class, Acceptors.acceptAll());
                    for (ModuleStatus aspect : aspects) {
                        Location location = aspect.getLocation();
                        String lus = location.getPersistentLookupString(false);
                        String path = lus.substring("ABSPATH:1:".length());

                        ZipEntry entry = new ZipEntry(path+".txt");
                        entry.setMethod(ZipEntry.DEFLATED);
                        zout.putNextEntry(entry);
                        IOUtils.copy(new StringReader(aspect.getReportText()), zout);
                        zout.closeEntry();
                    }

                }
            });
        } catch (Exception e) {
            throw new ServletException("Error getting zip", e);
        }
        finally
        {
            zout.close();
        }

    }
}
