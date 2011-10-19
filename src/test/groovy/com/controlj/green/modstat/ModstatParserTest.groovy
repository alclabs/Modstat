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

class ModstatParserTest extends Specification {

    InputStreamReader oldStyleIS = new InputStreamReader(getClass().getResourceAsStream("/com/controlj/green/modstat/samples/oldstyle.txt"))
    InputStreamReader newStyleIS = new InputStreamReader(getClass().getResourceAsStream("/com/controlj/green/modstat/samples/newstyle.txt"))

    def cmnet() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:   // normal
        Modstat m = parser.parse(new StringReader('''
MXM:  CM 15 ($0f)      Time: 09:50:01 Thursday Sep-16-2010
'''))

        then:
        m.isCmNet()

    }

    def errors() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:   // normal
        Modstat m = parser.parse(new StringReader("Error getting modstat:No response to WHO-IS. --- Modify <BACnet/IP Connection Comm Timeout (in millis)> tuning parameter. ---"))

        then:
        m.isErrorReading()
    }

    def captureTime() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:   // normal
        Modstat m = parser.parse(new StringReader("09/07/2010  21:46:11         CM: 1\n"))
        Date time = m.getCaptureTime()

        then:
        m.hasCaptureTime()
        time == new Date(110, 8, 7, 21,46,11)

        when:   // garbage before
        m = parser.parse(new StringReader("something at the start\n09/07/2010  21:46:11         CM: 1\n"))
        time = m.getCaptureTime()

        then:
        m.hasCaptureTime()
        time == new Date(110, 8, 7, 21,46,11)

        when:   // garbage after
        m = parser.parse(new StringReader("09/07/2010  21:46:11         CM: 1\nsomething at the end\n"))
        time = m.getCaptureTime()

        then:
        m.hasCaptureTime()
        time == new Date(110, 8, 7, 21,46,11)

        when: // using real old style file
        m = parser.parse(oldStyleIS)
        time = m.getCaptureTime()

        then:
        m.hasCaptureTime()
        time == new Date(110, 8, 7, 21, 37, 58)

        when:
        m = parser.parse(new StringReader("This is\na bunch\n of garbage"))

        then:
        !m.hasCaptureTime()

    }

    def devInstance() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(oldStyleIS)

        then:
        m.hasDeviceID()
        m.getDeviceInstance() == 300201

        when:
        m = parser.parse(new StringReader("This is\na bunch\n of garbage"))

        then:
        !m.hasDeviceID()
    }

    def downloadInfo() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(oldStyleIS)

        then:
        !m.hasDownloadInfo()

        when:
        m = parser.parse(newStyleIS)
        DownloadInfo dl = m.getDownloadInfo()

        then:
        m.hasDownloadInfo()
        dl.product == 'WebCTRL Server'
        dl.operator == 'Fred Jones'
        dl.time == new Date(110, 8, 15, 13, 58, 0)
        dl.address == '172.16.200.32:0xbac2'
    }

    def applicationSoftwareVersion() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(oldStyleIS)

        then:
        !m.hasApplicationSofwareVersion()

        when:
        m = parser.parse(newStyleIS)

        then:
        m.hasApplicationSofwareVersion()
        m.applicationSoftwareVersion == 'PRG:in_heat_req'
    }


    def flashArchive() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(oldStyleIS)

        then:
        !m.hasFlashArchiveTime()

        when:
        //Flash Archive Status: Valid on 09/15/10 14:05:21
        m = parser.parse(newStyleIS)

        then:
        m.hasFlashArchiveTime()
        m.flashArchiveTime == new Date(110, 8, 15, 14, 5, 21)
        m.flashArchiveStatus == "Valid"
        m.hasFlashArchiveStatus()

        when:
        m = parser.parse(new StringReader("Flash Archive Status: Unsupported\n"))

        then:
        !m.hasFlashArchiveTime()
        m.hasFlashArchiveStatus()
        m.flashArchiveStatus == "Unsupported"
    }

    def testPrograms() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(oldStyleIS)

        then:
        m.getProgramsLoaded()  == 5
        m.getProgramsRunning() == 4

        when:
        m = parser.parse(newStyleIS)

        then:
        m.getProgramsLoaded()  == 3
        m.getProgramsRunning() == 2

        when:
        m = parser.parse(new StringReader("This is\na bunch\n of garbage"))

        then:
        !m.hasProgramsLoaded()
        !m.hasProgramsRunning()
    }

    def firmwareSections() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(oldStyleIS)
        List<FirmwareVersion> sections = m.getFirmwareVersions()

        //  Boot16-E         - v4.01:001   Jun 19 2007
        //  LGRME2M DRIVER   - v2.24:013   Sep  3 2009

        then:
        m.hasFirmwareVersions()
        sections.size() == 2
        sections[0].name        == "Boot16-E"
        sections[0].version     == "4.01:001"
        sections[0].dateString  == "Jun 19 2007"
        sections[1].name        == "LGRME2M DRIVER"
        sections[1].version     == "2.24:013"
        sections[1].dateString  == "Sep  3 2009"

        when:
        m = parser.parse(newStyleIS)
        sections = m.getFirmwareVersions()

        //Boot16-E         - v4.03:001   Jun 15 2009
        //LGRME2M DRIVER   - v4.01:296   Sep  7 2010

        then:
        m.hasFirmwareVersions()
        sections.size() == 2
        sections[0].name        == "Boot16-E"
        sections[0].version     == "4.03:001"
        sections[0].dateString  == "Jun 15 2009"
        sections[1].name        == "LGRME2M DRIVER"
        sections[1].version     == "4.01:296"
        sections[1].dateString  == "Sep  7 2010"
    }

    def resetCounts() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(oldStyleIS)

        Map<String,Integer> rc = m.getResetCounts()

        then:
        rc.size() == 7
        rc.get("Power failures") == 18
        rc.get("Brownouts") == 5
        rc.get("Commanded warm boots") == 35
        rc.get("CPU clock failures") == 0
    }


    def messages() {
        setup:
        ModstatParser parser = new ModstatParser()

        Modstat m = parser.parse(oldStyleIS)

        when:
        List<Message> errors = m.getErrorMessages()

        then:
        errors.size() == 2
        errors[0].message == "EEPROM2: Read error at address 0x00"
        errors[0].date == new Date(110, 7, 23, 22, 44, 59)
        errors[1].message == "Yet another important warning 08/22/11"

        when:
        List<Message> warnings = m.getWarningMessages()

        then:
        warnings.size() == 2
        warnings[0].message == "Main firmware not found at startup."
        warnings[1].message == "Brownout protection activated."
        warnings[1].date == null

        when:
        List<Message> info = m.getInfoMessages()
        //Power restored after power failure.       09/07/10 09:15:40
        //Clock changed from 08/24/10 04:13:34 to   08/24/10 04:13:01
        //Software clock changed to                 08/18/10 00:30:31
        //RESET: BACnet reinitialize warmstart      08/02/10 11:10:25

        then:
        info.size() == 4
        info[0].message == 'Power restored after power failure.'
        info[0].date == new Date(110, 8, 7, 9, 15, 40)
        info[1].message == 'Clock changed from 08/24/10 04:13:34 to'
        info[1].date == new Date(110, 7, 24, 4, 13, 1)
        info[3].message == 'RESET: BACnet reinitialize warmstart'
    }


    def arcCounts() {
        setup:
        ModstatParser parser = new ModstatParser()

        Modstat m = parser.parse(oldStyleIS)

        when:
        Map<String,Long> counts = m.getArcnetReconfigs()

        then:
        counts.size() == 2
        counts.get("Total") == 3
        counts.get("Initiated by this node") == 1

        when:
        counts = m.getSecondaryArcnetReconfigs()

        then:
        counts.size() == 2
        counts.get("Total") == 1
        counts.get("Initiated by this node") == 0

        when:
        m = parser.parse(newStyleIS)

        then:
        m.hasArcnetReconfigs()
        !m.hasSecondaryArcnetReconfigs()
    }

    def bacnetError() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(newStyleIS)
        //BACnet comm errors in the last 7 days (cleared by format):    Incoming Outgoing
        //  ARC156 data link                          09/15/10 16:42:54        0        3

        then:
        m.hasBacnetErrors()
        m.bacnetErrors[0].dataLinkName == 'ARC156 data link'
        m.bacnetErrors[0].mostRecentTime == new Date(110, 8, 15, 16, 42, 54)
        m.bacnetErrors[0].incomingCount == 0
        m.bacnetErrors[0].outgoingCount == 3
    }

    def routes() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(newStyleIS)

        then:
        m.hasRoutesUsed()
        m.hasRoutesMax()
        m.routesUsed == 166
        m.routesMax == 500
    }


    def hardwareInfo() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(newStyleIS)
        //Main board hardware:
        //  Type=69, board=4, manufactured on 02/23/2001, S/N LME120010N
        //  RAM: 2048 kBytes;    FLASH: 1024 kBytes, type = 1

        HardwareInfo info = m.getCoreHardwareInfo()

        then:
        m.hasCoreHardwareInfo()
        !m.hasBaseHardwareInfo()
        info.type == 69
        info.board == 4
        info.manufactureDate == new Date(101, 1, 23)
        info.serialNumber == 'LME120010N'
        info.kRam ==2048
        info.kFlash == 1024
        info.flashType == 1

        when:
        m = parser.parse(oldStyleIS)
        info = m.getCoreHardwareInfo()
        HardwareInfo baseInfo = m.getBaseHardwareInfo()

        //Core board hardware:
        //  Type=0, board=1, manufactured on 09/28/2000, S/N CR6090003T
        //  RAM: 2048 kBytes;    FLASH: 1024 kBytes, type = 1
        //Base board hardware:
        //  Type=93, board=3, manufactured on 09/28/2000, S/N U32090024N

        then:
        m.hasCoreHardwareInfo()
        m.hasBaseHardwareInfo()
        info.type == 0
        info.board == 1
        info.manufactureDate == new Date(100, 8, 28)
        info.serialNumber == 'CR6090003T'
        info.kRam == 2048
        info.kFlash == 1024
        info.flashType == 1

        baseInfo.type == 93
        baseInfo.board == 3
        baseInfo.manufactureDate == new Date(100, 8, 28)
        baseInfo.serialNumber == 'U32090024N'
    }


    def freeHeap() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        //Free heap space = 271500.
        Modstat m = parser.parse(oldStyleIS)

        then:
        m.getFreeHeap()  == 271500

        when:
        //Largest free heap space = 18432.
        m = parser.parse(newStyleIS)

        then:
        m.freeHeap  == 18432

    }

    def database() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        //Database size = 1524138 , used = 213698, free = 1310440.
        Modstat m = parser.parse(oldStyleIS)

        then:
        m.hasDatabaseSize()
        m.hasDatabaseFree()
        m.hasDatabaseUsed()
        m.databaseSize == 1524138
        m.databaseUsed == 213698
        m.databaseFree == 1310440
    }

    def flashStorage() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        //Flash storage size = 458755
        //  File storage size = 458752 (max = 458750), used = 5412, free = 453340

        Modstat m = parser.parse(newStyleIS)
        FlashStorage fs = m.flashStorage

        then:
        m.hasFlashStorage()
        fs.flashSize == 7061504
        fs.archiveSize == 7045120
        fs.hasArchiveSize()
        fs.totalFileSize == 16384
        fs.maxFileSize == 7061504
        fs.usedFileSize == 6376
        fs.freeFileSize == 10008
    }



    def switches() {
        setup:
        ModstatParser parser = new ModstatParser()

        when:
        Modstat m = parser.parse(oldStyleIS)

        then:
        m.switches == '0x2488000'

        when:
        m = parser.parse(new StringReader("Raw physical switches = 01B20000 00000000\n"))

        then:
        m.switches == '01B20000 00000000'
    }


    def network() {
        setup:
        ModstatParser parser = new ModstatParser()

        Modstat m = parser.parse(oldStyleIS)
        //Network Information:
        //  Ethernet MAC address  = 00-E0-C9-00-14-E5
        //  Current IP Address    = 172.16.12.18
        //  Current Subnet Mask   = 255.255.0.0
        //  Current Gateway Addr  = 172.16.0.3
        //  Assigned IP Address   = 172.16.12.18
        //  Assigned Subnet Mask  = 255.255.0.0
        //  Assigned Gateway Addr = 172.16.0.3


        when:
        Map info = m.getNetworkInfo()

        then:
        info['Ethernet MAC address'] == '00-E0-C9-00-14-E5'
        info['Current IP Address'] == '172.16.12.18'
        info['Current Subnet Mask'] == '255.255.0.0'
        info['Current Gateway Addr'] == '172.16.0.3'
        info['Assigned IP Address'] == '172.16.12.18'
        info['Assigned Subnet Mask'] == '255.255.0.0'
        info['Assigned Gateway Addr'] == '172.16.0.3'
    }


    def ethernet() {
        setup:
        ModstatParser parser = new ModstatParser()

        Modstat m = parser.parse(newStyleIS)
        //Ethernet statistics:
        //  Total frames received    = 2010682
        //  Total frames transmitted = 1043164
        //  Tx deferred A            = 1
        //  Tx deferred B            = 2
        //  Rx dribble errors        = 3
        //  Receptions missed        = 206


        when:
        Map info = m.getEthernetStats()

        then:
        m.hasEthernetStats()
        info[Modstat.EthernetStats.TOTAL_RECEIVED]     == 2010682
        info[Modstat.EthernetStats.TOTAL_TRANSMITTED]  == 1043164
        info[Modstat.EthernetStats.TX_DEFERRED_A]      == 1
        info[Modstat.EthernetStats.TX_DEFERRED_B]      == 2
        info[Modstat.EthernetStats.RX_DRIBBLE_ERRORS]  == 3
        info[Modstat.EthernetStats.RX_RECEPTION_MISSED]== 206
        info[Modstat.EthernetStats.RX_TOO_BIG]         == 7
    }

    def secondaryArcnetStats() {
        setup:
        ModstatParser parser = new ModstatParser()

        Modstat m = parser.parse(newStyleIS)
        //Secondary ARC156 cumulative diagnostics since last reset:
        //  Rx READY
        //  Tx READY
        //  SlaveResets=1
        //  RxCmd=1976670
        //  TxCmd=3829326
        //  OverrunErrors=1
        //  ParityErrors=0


        when:
        Map info = m.getSecondaryArcnetStats()

        then:
        m.hasSecondaryArcnetStats()
        m.hasSecondaryArcnetRxState()
        m.hasSecondaryArcnetTxState()
        m.secondaryArcnetRxState == "READY"
        m.secondaryArcnetTxState == "READY"
        info[Modstat.SecondaryArcnetStats.SLAVE_RESETS]     == 1
        info[Modstat.SecondaryArcnetStats.RX_CMD]           == 1976670
        info[Modstat.SecondaryArcnetStats.TX_CMD]           == 3829326
        info[Modstat.SecondaryArcnetStats.OVERRUN_ERRORS]   == 1
        info[Modstat.SecondaryArcnetStats.PARITY_ERRORS]    == 0
    }


    def bacnetPort() {
        setup:
        ModstatParser parser = new ModstatParser()

        Modstat m = parser.parse(newStyleIS)
        //        BACnet Port:
        //          Protocol=BACnet/ARC156
        //          Baud Rate=156000 bps


        when:
        Map info = m.getBacnetPort()

        then:
        m.hasBacnetPort()
        info[Modstat.BACnetPort.PROTOCOL]  == "BACnet/ARC156"
        info[Modstat.BACnetPort.BAUD_RATE] == "156000 bps"
        info.size() == 2
    }



    def parsedAllNew() {
        setup:
        ModstatParser parser = new ModstatParser()

        Modstat m = parser.parse(newStyleIS)

        when:
        def lines = m.getUnparsedLines()

        then:
        lines.size() == 0

    }

    def parsedAllOld() {
        setup:
        ModstatParser parser = new ModstatParser()

        Modstat m = parser.parse(oldStyleIS)

        when:
        def lines = m.getUnparsedLines()

        then:
        lines.size() == 0

    }
}
