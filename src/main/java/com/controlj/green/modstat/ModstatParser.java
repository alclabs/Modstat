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

import com.controlj.green.modstat.section.*;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/*
        Sections - in order?
ADDRESS BINDING Used: device instance 488001 is on network 0 mac 172.31.217.213:0xbac0
09/07/2010  21:46:11         CM: 3

Device Instance: 0300303

Downloaded by: WebCTRL Server 09/15/10 13:58 Mark Evans - 172.16.200.32:0xbac2

Application Software Version: PRG:in_heat_req

Flash Archive Status: Valid on 09/15/10 14:05:21

1 PRGs loaded.  1 PRGs running.
or
1 PRGs initialized.  1 PRGs running.


Module status:
Firmware sections validated in flash memory
or
Firmware sections in flash memory
============================================
  Boot8            - v4.01:001   Jun 19 2007
  S6104 DRIVER     - v2.24:013   Sep 03 2009

Reset counters:
    41 Power failures
    xx asdfadsfakj

System error message history:                                   Type   Specific
  EEPROM2: Read error at address 0x00       08/23/10 22:44:59 00014001 00000000
Warning message history:
  Main firmware not found at startup.       08/24/10 08:22:47
  Brownout protection activated.            --/--/-- --:--:--
Information message history:
  Clock changed from 08/24/10 08:25:18 to   08/24/10 08:24:36

ARC156 reconfigurations during the last hour (cleared upon reset):
   Total ....................... 14
   Initiated by this node ...... 0

Secondary ARC156 reconfigurations during the last hour (cleared upon reset):
   Total ....................... 0
   Initiated by this node ...... 0

BACnet comm errors in the last 7 days (cleared by format):    Incoming Outgoing
  ARC156 data link                          09/15/10 16:56:03        0        6

Main board hardware:
  Type=84, board=7, manufactured on 06/15/2000, S/N S40055120N
  RAM: 512 kBytes;    FLASH: 512 kBytes, type = 2

Core board hardware:
  Type=168, board=74, manufactured on 08/17/2009, S/N 21C951608N
  RAM: 1024 kBytes;    FLASH: 4096 kBytes, type = 6
Base board hardware:
  Type=168, board=48, manufactured on 08/17/2009, S/N AMR970030N

Largest free heap space = 19456.
or
Free heap space = 12903.

Database size = 368274 , used = 185994, free = 182280.

Raw physical switches: 0x3008000

Network Information:
  Ethernet MAC address  = 00-E0-C9-00-05-76
  Current IP Address    = 172.16.12.28
  Current Subnet Mask   = 255.255.0.0
  Current Gateway Addr  = 172.16.100.110
  Assigned IP Address   = 172.16.12.28
  Assigned Subnet Mask  = 255.255.0.0
  Assigned Gateway Addr = 172.16.100.110
Ethernet statistics:
  Total frames received    = 30952383
  Total frames transmitted = 282192
  Tx deferred A            = 0
  Tx deferred B            = 0
  Tx aborts: Out-of-window = 0
  Tx aborts: Jabber        = 0
  Tx aborts: 16 collisions = 147
  Tx aborts: Underrun      = 0
  Tx cleanups type A       = 0
  Tx cleanups type B       = 0
  Tx cleanups type D       = 0
  Tx cleanups type E       = 0
  Tx no chip memory A      = 0
  Tx no chip memory B      = 0
  Tx no chip memory C      = 0
  Tx chip memory now ready = 0
  Rx smaller than 64 bytes = 0
  Rx larger than 1518 bytes= 0
  Rx no buffer errors      = 0
  Rx alignment errors      = 0
  Rx CRC errors            = 0
  Rx dribble errors        = 0
  Receptions missed        = 2002
*/

public class ModstatParser {

    private static List<ModstatSection> initializeSections(LineSource source, Modstat modstat) {

        List<ModstatSection> sections = new LinkedList<ModstatSection>();

        sections.add(new AddressBindingSection(source, modstat));
        sections.add(new CMnetSection(source, modstat));
        sections.add(new ErrorSection(source, modstat));
        sections.add(new TimeSection(source, modstat));
        sections.add(new DeviceInstanceSection(source, modstat));
        sections.add(new DownloadedBySection(source, modstat));
        sections.add(new ApplicationsSection(source, modstat));
        sections.add(new FlashArchiveSection(source, modstat));
        sections.add(new ProgramsSection(source, modstat));
        sections.add(new FirmwareSection(source, modstat));
        sections.add(new ResetCountSection(source, modstat));
        sections.add(new ErrorMessageSection(source, modstat));
        sections.add(new WarningMessageSection(source, modstat));
        sections.add(new InfoMessageSection(source, modstat));
        sections.add(new ArcnetReconfigSection(source, modstat));
        sections.add(new Arcnet2ReconfigSection(source, modstat));
        sections.add(new BACnetErrorSection(source, modstat));
        sections.add(new RoutesSection(source, modstat));        
        sections.add(new CoreHardwareSection(source, modstat));
        sections.add(new BaseHardwareSection(source, modstat));
        sections.add(new FreeHeapSection(source, modstat));
        sections.add(new DatabaseSection(source, modstat));
        sections.add(new FlashStorageSection(source, modstat));
        sections.add(new SwitchesSection(source, modstat));
        sections.add(new BACnetPortSection(source, modstat));
        sections.add(new FlowSensorSection(source, modstat));
        sections.add(new NetworkInformationSection(source, modstat));
        sections.add(new EthernetSection(source, modstat));
        sections.add(new SecondaryArcnetStatSection(source, modstat));        

        return sections;
    }

    public static Modstat parse(Reader rdr) throws IOException {
        List<ModstatSection> sections;
        LineSource source = new LineSource(rdr);

        Modstat modstat = new Modstat();

        sections = initializeSections(source, modstat);

        try {
            while (!source.isEmpty()) {
                ListIterator<ModstatSection> it = sections.listIterator();

                while (it.hasNext()) {
                    ModstatSection nextSection = it.next();
                    if (nextSection.lookForSection()) {
                        it.remove();
                    }
                }
                if (!source.isEmpty()) {    // line wasn't handled by this pass of section parsers.  Advance to next line and try again
                    modstat.addUnparsedLine(source.getCurrentLine());
                    source.nextLine();
                }
            }
        } catch (StopParseException e) { } // just stop parsing

        return modstat;
    }


}
