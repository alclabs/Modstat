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
