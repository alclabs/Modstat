package com.controlj.green.modstat.checker

import spock.lang.Specification

/**
 * Created by sappling on 2/27/2016.
 */
class MEBadCapSerialTest extends Specification {
    def "Test isBetween"() {
        expect:
        MEBadCapSerial.isBetween(test, lower, upper) == result

        where:
        test | lower | upper | result
        "BB" | "B0"  | "C0"  | true
        "A9" | "B0"  | "C0"  | false
        "88" | "88"  | "99"  | false
        "99" | "88"  | "99"  | false
    }


    def "Test isInRange"() {
        expect:
        MEBadCapSerial.isInRange(test) == result
        /*  Ranges
               560001P - 630190P
            IOU
            M8U
            M8E
            M8L
            O8E
            PSO
            ME8
            SIO
         */


        where:
        test         | result
        "IOU560001P" | true
        "IOU5B9998P" | true
        "IOU6A9998P" | false
        "IOU5C0001P" | true
        "M8L560021P" | true
        "M8L550021P" | false
        "SIO5A0000P" | true
        "SIO630191P" | false
        "O8E564001P" | true
        "O8E540010P" | false
    }
}
