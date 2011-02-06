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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TestWork extends Thread implements RunnableProgress {
    private int progress = 0;
    private int delaySeconds = 3;
    private File testFile;

    public TestWork(File file) {
        testFile = file;
    }


    @Override
    public int getProgress() {
        return progress * 10 / delaySeconds;
    }

    @Override
    public boolean hasError() {
        return false;
    }

    @Override
    public Exception getError() {
        return null;  
    }

    @Override
    public InputStream getCache() {
        try {
            return new FileInputStream(testFile);
        } catch (FileNotFoundException e) {
            System.err.println("Modstat Addon: error getting test file:"+ e.getMessage());
            return null;
        }
    }

    @Override
    public void run() {
        try {
            while (progress < (delaySeconds * 10)) {
                Thread.sleep(100);
                progress++;
            }
        } catch (InterruptedException e) { } // shutting down, this is OK
    }
}
