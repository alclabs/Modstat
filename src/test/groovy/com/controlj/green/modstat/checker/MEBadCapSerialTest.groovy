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
        /*  Start of ranges.  They end at xxxxA9998P
            "IOU560000P"
            "ME8560000P"
            "M8L560000P"
            "M8U560000P"
            "O8E560000P"
            "PSI560000P"
            "PSO560000P"
            "SIO560000P"
         */


        where:
        test         | result
        "IOU560001P" | true
        "IOU5B9998P" | false
        "IOU5A9998P" | true
        "IOU5C0001P" | false
        "M8L560021P" | true
        "M8L550021P" | false
        "SIO5A0000P" | true
        "SIO5A0123P" | true
        "O8E564001P" | true
        "O8E540010P" | false
    }
}
