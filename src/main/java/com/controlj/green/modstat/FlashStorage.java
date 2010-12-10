package com.controlj.green.modstat;

public class FlashStorage {
    private long flashSize;
    private long totalFileSize;
    private long maxFileSize;
    private long usedFileSize;
    private long freeFileSize;

    public FlashStorage(long flashSize, long totalFileSize, long maxFileSize, long usedFileSize, long freeFileSize) {
        this.flashSize = flashSize;
        this.totalFileSize = totalFileSize;
        this.maxFileSize = maxFileSize;
        this.usedFileSize = usedFileSize;
        this.freeFileSize = freeFileSize;
    }

    public long getFlashSize() {
        return flashSize;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public long getUsedFileSize() {
        return usedFileSize;
    }

    public long getFreeFileSize() {
        return freeFileSize;
    }
}
