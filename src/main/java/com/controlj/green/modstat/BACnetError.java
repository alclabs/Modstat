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
