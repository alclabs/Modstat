package com.controlj.green.modstat;

import java.util.Date;

public class BACnetError {
    private String dataLinkName;
    private Date mostRecentTime;
    private long incomingCount;
    private long outgoingCount;

    public BACnetError(String dataLinkName, Date mostRecentTime, long incomingCount, long outgoingCount) {
        this.dataLinkName = dataLinkName;
        this.mostRecentTime = mostRecentTime;
        this.incomingCount = incomingCount;
        this.outgoingCount = outgoingCount;
    }

    public String getDataLinkName() {
        return dataLinkName;
    }

    public Date getMostRecentTime() {
        return mostRecentTime;
    }

    public long getIncomingCount() {
        return incomingCount;
    }

    public long getOutgoingCount() {
        return outgoingCount;
    }
}
