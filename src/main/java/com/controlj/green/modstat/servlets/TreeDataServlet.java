package com.controlj.green.modstat.servlets;

import com.controlj.green.addonsupport.InvalidConnectionRequestException;
import com.controlj.green.addonsupport.access.*;
import com.controlj.green.addonsupport.access.util.LocationSort;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

public class TreeDataServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        final String id = req.getParameter("id");
        String treeString = req.getParameter("type");
        final SystemTree tree;
        if (treeString == null || treeString.equals("geo")) {
            tree = SystemTree.Geographic;
        } else if (treeString.equals("net")) {
            tree = SystemTree.Network;
        } else {
            tree = SystemTree.Geographic;
        }

        final PrintWriter writer = resp.getWriter();

        try {
            SystemConnection connection = DirectAccess.getDirectAccess().getUserSystemConnection(req);

            connection.runReadAction(new ReadAction() {
                @Override
                public void execute(@NotNull SystemAccess access) throws Exception {
                    JSONArray arrayData = new JSONArray();
                    Collection<Location> children;
                    if (id == null) {
                        children = new ArrayList<Location>(1);
                        children.add(access.getTree(tree).getRoot());
                    }
                    else {
                        children = access.getTree(tree).resolve(id).getChildren(LocationSort.PRESENTATION);
                    }


                    for (Location child : children) {
                        JSONObject next = new JSONObject();
                        next.put("title", child.getDisplayName());
                        next.put("key", child.getTransientLookupString());
                        next.put("path", child.getDisplayPath());

                        if (!child.getChildren().isEmpty()) {
                            next.put("isLazy", true);
                        }

                        next.put("icon", getIconForType(child.getType()));
                        arrayData.put(next);
                    }
                    arrayData.write(writer);
                }
            });
        } catch (InvalidConnectionRequestException e) {
            //todo handle
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ActionExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private String getIconForType(LocationType type) {
        String urlBase = "../../../_common/lvl5/skin/graphics/type/";
        String image;

        switch (type) {
            case System:
                image = "system.gif";
                break;
            
            case Area:
                image = "area.gif";
                break;

            case Site:
                image = "site.gif";
                break;

            case Network:
                image = "network.gif";
                break;

            case Device:
                image = "hardware.gif";
                break;

            case Driver:
                image = "dir.gif";
                break;

            case Equipment:
                image = "equipment.gif";
                break;

            case Microblock:
                image = "io_point.gif";
                break;

            case MicroblockComponent:
                image = "io_point.gif";
                break;

            default:
                image = "unknown.gif";
                break;
        }

        return urlBase + image;
    }
}
