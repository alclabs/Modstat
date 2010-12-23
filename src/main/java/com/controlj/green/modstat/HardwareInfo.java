/*
 * Copyright (c) 2010 Automated Logic Corporation
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


/*
Core board hardware:
  Type=168, board=74, manufactured on 08/17/2009, S/N 21C951608N
  RAM: 1024 kBytes;    FLASH: 4096 kBytes, type = 6
Base board hardware:
  Type=168, board=48, manufactured on 08/17/2009, S/N AMR970030N
 */
public class HardwareInfo {
    private int type = -1;
    private int board = -1;
    private Date manufactureDate;
    private String serialNumber;
    private int kRam = -1;
    private int kFlash = -1;
    private int flashType = -1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean hasType() {
        return type != -1;
    }

    public int getBoard() {
        return board;
    }

    public void setBoard(int board) {
        this.board = board;
    }

    public boolean hasBoard() {
        return board != -1;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public boolean hasManufactureDate() {
        return manufactureDate != null;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean hasSerialNumber() {
        return serialNumber != null;
    }

    public int getKRam() {
        return kRam;
    }

    public void setKRam(int kRam) {
        this.kRam = kRam;
    }

    public boolean hasKRam() {
        return kRam != -1;
    }

    public int getKFlash() {
        return kFlash;
    }

    public void setKFlash(int kFlash) {
        this.kFlash = kFlash;
    }

    public boolean hasKFlash() {
        return kFlash != -1;
    }

    public int getFlashType() {
        return flashType;
    }

    public void setFlashType(int flashType) {
        this.flashType = flashType;
    }

    public boolean hasFlashType() {
        return flashType != -1;
    }
}
