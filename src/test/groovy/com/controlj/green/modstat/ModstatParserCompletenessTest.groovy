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

package com.controlj.green.modstat

import spock.lang.Specification
import java.util.zip.ZipInputStream
import java.util.zip.ZipEntry

/**
 * 
 */
class ModstatParserCompletenessTest extends Specification {

    private ZipInputStream zin = new ZipInputStream(getClass().getResourceAsStream("/com/controlj/green/modstat/samples/modstat.zip"));

    /*
    def testComplete() {
        setup:
        def problems = [:]

        ZipEntry entry;
        while ((entry = zin.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                String path = entry.getName();
                Modstat modstat = ModstatParser.parse(new InputStreamReader(zin))
                def lines = modstat.getUnparsedLines()
                if (lines.size() > 0) {
                    problems[path] = lines
                }
            }
        }

        if (problems.size() > 0) {
            problems.each { key, value ->
                System.err.println(key)
                value.each {
                    System.err.println "   $it"
                }
            }
        }
    }
    */

}
