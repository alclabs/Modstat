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

package com.controlj.green.modstat.work;

import com.controlj.green.addonsupport.access.*;
import com.controlj.green.addonsupport.access.aspect.Device;
import com.controlj.green.addonsupport.access.aspect.ModuleStatus;
import com.controlj.green.addonsupport.access.util.Acceptors;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ModstatWork extends Thread implements RunnableProgress {
    private int progress = 0;
    private int progressLimit = Integer.MAX_VALUE;
    private Exception error = null;
    private final SystemConnection connection;
    private final String rootPath;
    File resultFile = null;
    private byte[] cache = null;

    public ModstatWork(SystemConnection connection, String rootPath) {
        this.connection = connection;
        this.rootPath = rootPath;
    }

    @Override
    synchronized public int getProgress() {
        return progress * 100 / progressLimit;
    }

    synchronized public boolean hasError() {
        return error != null;
    }

    synchronized public Exception getError() {
        return error;
    }

    @Override
    public InputStream getCache() {
        return new ByteArrayInputStream(cache);
    }

    @Override
    public void run() {
        final ZipOutputStream zout;
        ByteArrayOutputStream out = new ByteArrayOutputStream(10000);
        zout = new ZipOutputStream(out);

        try {

            connection.runReadAction(FieldAccessFactory.newFieldAccess(), new ReadAction() {

                public void execute(@NotNull SystemAccess access) throws Exception {
                    Location root = access.getTree(SystemTree.Network).resolve(rootPath);

                    Collection<ModuleStatus> aspects = root.find(ModuleStatus.class, Acceptors.acceptAll());

                    synchronized (this) {
                        progressLimit = aspects.size();
                    }

                    for (ModuleStatus aspect : aspects) {
                        Location location = aspect.getLocation();
                        try {
                            if (!location.getAspect(Device.class).isOutOfService()) {
                                String path = getReferencePath(location);

                                //System.out.println("Gathering modstat from "+path);

                                ZipEntry entry = new ZipEntry(path+".txt");
                                entry.setMethod(ZipEntry.DEFLATED);
                                zout.putNextEntry(entry);
                                IOUtils.copy(new StringReader(aspect.getReportText()), zout);
                                zout.closeEntry();
        
                                synchronized (this) {
                                    progress++;
                                    if (isInterrupted()) {
                                        error = new Exception("Gathering Modstats interrupted");
                                        return;
                                    }
                                }
                            }
                        } catch (NoSuchAspectException e) { // skip and go to the next one
                        }
                    }

                }
            });
        } catch (Exception e) {
            synchronized (this) {
                error = e;
            }
            return;
        }
        finally
        {
            try {
                zout.close();
            } catch (IOException e) {} // we tried our best
        }
        if (!hasError()) {
            cache = out.toByteArray();
        }
    }

    public static String getReferencePath(Location loc) {
        try {
            if (loc.hasParent() && (loc.getParent().getType() != LocationType.System) ) {
                    return getReferencePath(loc.getParent()) + "/" + loc.getReferenceName();
            } else {
                return loc.getReferenceName();
            }
        } catch (UnresolvableException e) {
            throw new RuntimeException("Couldn't resolve parent when it should exist.", e);
        }
    }
}