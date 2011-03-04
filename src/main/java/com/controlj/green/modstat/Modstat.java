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

package com.controlj.green.modstat;

import java.util.*;

public class Modstat {
    private List<String> unparsedLines = new ArrayList<String>();
    private boolean errorReading = false;
    private boolean cmNet = false;
    private Date captureTime;
    private Date flashArchiveTime;
    private String flashArchiveStatus;
    private int deviceInstance = -1;
    private int programsLoaded = -1;
    private int programsRunning = -1;
    private long freeHeap = -1;
    private long databaseSize = -1;
    private long databaseUsed = -1;
    private long databaseFree = -1;
    private String applicationSoftwareVersion;
    boolean switchesSet = false;
    private int switches;
    private DownloadInfo downloadInfo;
    private List<FirmwareVersion> firmwareVersions = new ArrayList<FirmwareVersion>();
    private Map<String, Long>  resetCounts = new HashMap<String,Long>();
    private Map<String, Long>  arcnetReconfigs = new HashMap<String,Long>();
    private Map<String, Long>  secondaryArcnetReconfigs = new HashMap<String,Long>();
    private List<BACnetError> bacnetErrors = new ArrayList<BACnetError>();
    private Map<String,String> networkInfo = new HashMap<String,String>();
    private Map<String,Long> ethernetStats = new HashMap<String,Long>();
    private List<Message> errorMessages = new ArrayList<Message>();
    private List<Message> warningMessages = new ArrayList<Message>();
    private List<Message> infoMessages = new ArrayList<Message>();
    private HardwareInfo coreHardwareInfo;  // also main board
    private HardwareInfo baseHardwareInfo;
    private FlashStorage flashStorage;

    public interface ResetCount {
        public static final String POWER_FAILURES = "Power failures";
        public static final String BROWNOUTS = "Brownouts";
        public static final String WARM_BOOTS = "Commanded warm boots";
        public static final String COLD_BOOTS = "Commanded cold boots";
        public static final String ERRORS = "System errors";
        public static final String WATCHDOG_TIMEOUTS = "Watchdog timeouts";
    }

    public interface ArcnetReconfigs {
        public static final String TOTAL = "Total";
        public static final String THIS_NODE = "Initiated by this node";        
    }

    public interface EthernetStats {
        public static final String TOTAL_RECEIVED           = "Total frames received";
        public static final String TOTAL_TRANSMITTED        = "Total frames transmitted";
        public static final String TX_DEFERRED_A            = "Tx deferred A";
        public static final String TX_DEFERRED_B            = "Tx deferred B";
        public static final String TX_ABORTS_OUT_OF_WIN     = "Tx aborts: Out-of-window";
        public static final String TX_ABORTS_JABBER         = "Tx aborts: Jabber";
        public static final String TX_ABORTS_16_COLLISIONS  = "Tx aborts: 16 collisions";
        public static final String TX_ABORTS_UNDERRUN       = "Tx aborts: Underrun";
        public static final String TX_CLEANUPS_A            = "Tx cleanups type A";
        public static final String TX_CLEANUPS_B            = "Tx cleanups type B";
        public static final String TX_CLEANUPS_D            = "Tx cleanups type D";
        public static final String TX_CLEANUPS_E            = "Tx cleanups type E";
        public static final String TX_NO_CHIP_MEM_A         = "Tx no chip memory A";
        public static final String TX_NO_CHIP_MEM_B         = "Tx no chip memory B";
        public static final String TX_NO_CHIP_MEM_C         = "Tx no chip memory C";
        public static final String TX_CHIP_MEM_READY        = "Tx chip memory now ready";
        public static final String RX_TOO_SMALL             = "Rx smaller than 64 bytes";
        public static final String RX_TOO_BIG               = "Rx larger than 1518 bytes";
        public static final String RX_NO_BUFFER             = "Rx no buffer errors";
        public static final String RX_ALIGNMENT             = "Rx alignment errors";
        public static final String RX_CRC_ERRORS            = "Rx CRC errors";
        public static final String RX_DRIBBLE_ERRORS        = "Rx dribble errors";
        public static final String RX_RECEPTION_MISSED      = "Receptions missed";        
    }

    public void addUnparsedLine(String line) {
        unparsedLines.add(line);
    }

    public List<String> getUnparsedLines() {
        return unparsedLines;
    }

    public Date getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Date captureTime) {
        this.captureTime = captureTime;
    }

    public boolean hasCaptureTime() {
        return captureTime != null;
    }

    public int getDeviceInstance() {
        return deviceInstance;
    }

    public void setDeviceInstance(int deviceInstance) {
        this.deviceInstance = deviceInstance;
    }

    public boolean hasDeviceID() {
        return deviceInstance != -1;
    }

    public Date getFlashArchiveTime() {
        return flashArchiveTime;
    }

    public void setFlashArchiveTime(Date flashArchiveTime) {
        this.flashArchiveTime = flashArchiveTime;
    }

    public boolean hasFlashArchiveTime() {
        return flashArchiveTime != null;
    }

    public String getFlashArchiveStatus() {
        return flashArchiveStatus;
    }

    public void setFlashArchiveStatus(String flashArchiveStatus) {
        this.flashArchiveStatus = flashArchiveStatus;
    }

    public boolean hasFlashArchiveStatus() {
        return flashArchiveStatus != null;
    }

    public int getProgramsLoaded() {
        return programsLoaded;
    }

    public void setProgramsLoaded(int programsLoaded) {
        this.programsLoaded = programsLoaded;
    }

    public boolean hasProgramsLoaded() {
        return programsLoaded != -1;
    }

    public int getProgramsRunning() {
        return programsRunning;
    }

    public void setProgramsRunning(int programsRunning) {
        this.programsRunning = programsRunning;
    }

    public boolean hasProgramsRunning() {
        return programsRunning != -1;
    }

    public List<FirmwareVersion> getFirmwareVersions() {
        return firmwareVersions;
    }

    public void addFirmwareVersion(FirmwareVersion newVersion)
    {
        firmwareVersions.add(newVersion);
    }

    public boolean hasFirmwareVersion() {
        return !firmwareVersions.isEmpty();
    }

    public Map<String,Long> getResetCounts()
    {
        return resetCounts;
    }

    public boolean hasResetCounts() {
        return !resetCounts.isEmpty();
    }

    public List<Message> getErrorMessages() {
        return errorMessages;
    }

    public boolean hasErrorMessages() {
        return !errorMessages.isEmpty();
    }

    public List<Message> getWarningMessages() {
        return warningMessages;
    }

    public boolean hasWarningMessages() {
        return !warningMessages.isEmpty();
    }

    public List<Message> getInfoMessages() {
        return infoMessages;
    }

    public boolean hasInfoMessages() {
        return !infoMessages.isEmpty();
    }

    public Map<String, Long> getArcnetReconfigs() {
        return arcnetReconfigs;
    }

    public boolean hasArcnetReconfigs() {
        return !arcnetReconfigs.isEmpty();
    }

    public Map<String, Long> getSecondaryArcnetReconfigs() {
        return secondaryArcnetReconfigs;
    }

    public boolean hasSecondaryArcnetReconfigs() {
        return !secondaryArcnetReconfigs.isEmpty();
    }

    public List<BACnetError> getBacnetErrors() {
        return bacnetErrors;
    }

    public boolean hasBacnetErrors() {
        return !bacnetErrors.isEmpty();
    }

    public long getFreeHeap() {
        return freeHeap;
    }

    public void setFreeHeap(long freeHeap) {
        this.freeHeap = freeHeap;
    }

    public boolean hasFreeHeap() {
        return freeHeap != -1L;
    }

    public long getDatabaseSize() {
        return databaseSize;
    }

    public void setDatabaseSize(long databaseSize) {
        this.databaseSize = databaseSize;
    }

    public boolean hasDatabaseSize() {
        return databaseSize != -1L;
    }

    public long getDatabaseUsed() {
        return databaseUsed;
    }

    public void setDatabaseUsed(long databaseUsed) {
        this.databaseUsed = databaseUsed;
    }

    public boolean hasDatabaseUsed() {
        return databaseUsed != -1L;
    }

    public long getDatabaseFree() {
        return databaseFree;
    }

    public void setDatabaseFree(long databaseFree) {
        this.databaseFree = databaseFree;
    }

    public boolean hasDatabaseFree() {
        return databaseFree != -1L;
    }

    public Map<String, String> getNetworkInfo() {
        return networkInfo;
    }

    public boolean hasNetworkInfo() {
        return !networkInfo.isEmpty();
    }

    public Map<String, Long> getEthernetStats() {
        return ethernetStats;
    }

    public boolean hasEthernetStats() {
        return !ethernetStats.isEmpty();
    }

    public int getSwitches() {
        return switches;
    }

    public void setSwitches(int switches) {
        this.switches = switches;
        switchesSet = true;
    }

    public boolean hasSwitches() {
        return switchesSet;
    }

    public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public boolean hasDownloadInfo() {
        return downloadInfo != null;
    }

    public String getApplicationSoftwareVersion() {
        return applicationSoftwareVersion;
    }

    public void setApplicationSoftwareVersion(String applicationSoftwareVersion) {
        this.applicationSoftwareVersion = applicationSoftwareVersion;
    }

    public boolean hasApplicationSofwareVersion() {
        return applicationSoftwareVersion != null;
    }

    public HardwareInfo getCoreHardwareInfo() {
        return coreHardwareInfo;
    }

    public void setCoreHardwareInfo(HardwareInfo coreHardwareInfo) {
        this.coreHardwareInfo = coreHardwareInfo;
    }

    public boolean hasCoreHardwareInfo() {
        return coreHardwareInfo != null;
    }

    public HardwareInfo getBaseHardwareInfo() {
        return baseHardwareInfo;
    }

    public void setBaseHardwareInfo(HardwareInfo baseHardwareInfo) {
        this.baseHardwareInfo = baseHardwareInfo;
    }

    public boolean hasBaseHardwareInfo() {
        return baseHardwareInfo != null;
    }

    public boolean isErrorReading() {
        return errorReading;
    }

    public void setErrorReading(boolean errorReading) {
        this.errorReading = errorReading;
    }

    public FlashStorage getFlashStorage() {
        return flashStorage;
    }

    public void setFlashStorage(FlashStorage flashStorage) {
        this.flashStorage = flashStorage;
    }

    public boolean hasFlashStorage() {
        return flashStorage != null;
    }

    public boolean isCmNet() {
        return cmNet;
    }

    public void setCmNet(boolean cmNet) {
        this.cmNet = cmNet;
    }
}
