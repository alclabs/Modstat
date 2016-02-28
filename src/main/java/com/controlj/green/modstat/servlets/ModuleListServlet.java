package com.controlj.green.modstat.servlets;

import com.controlj.green.addonsupport.InvalidConnectionRequestException;
import com.controlj.green.addonsupport.access.*;
import com.controlj.green.addonsupport.access.aspect.Device;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by sappling on 2/27/2016.
 */
public class ModuleListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SystemConnection connection = null;
        try {
            connection = DirectAccess.getDirectAccess().getUserSystemConnection(req);
            final Collection<String> deviceNames = new HashSet<String>();
            try {
                connection.runReadAction(FieldAccessFactory.newDisabledFieldAccess(), new ReadAction() {

                    @Override
                    public void execute(@NotNull SystemAccess access) throws Exception {
                        Collection<Device> devices = access.find(access.getNetRoot(), Device.class, new AspectAcceptor<Device>() {
                            @Override
                            public boolean accept(@NotNull Device device) {
                                return !device.isOutOfService();
                            }
                        });

                        for (Device device : devices) {
                            deviceNames.add(device.getModelName());
                        }
                    }
                });
            } catch (ActionExecutionException e) {
                throw new ServletException("Error finding devices",e);
            } catch (SystemException e) {
                throw new ServletException("System Error finding devices",e);
            }

            ArrayList<String> deviceList = new ArrayList<String>(deviceNames);
            Collections.sort(deviceList);

            JSONArray arrayData = new JSONArray();
            for (String deviceName : deviceList) {
                arrayData.put(deviceName);
            }
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");
            arrayData.write(writer);
            writer.close();
        } catch (InvalidConnectionRequestException e) {
            throw new ServletException("Error getting connection:"+e.getMessage());
        } catch (JSONException e) {
            throw new ServletException("Error encoding JSON",e);
        }
    }
}
