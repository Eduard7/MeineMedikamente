package com.kempinger.meinemedikamente.model

import com.kempinger.meinemedikamente.activities.*

class DisplayPressureColorScala {


    var lowest  = 0
    var lowestDia = 0


    fun returnColorSys(sysPress: Int): String {


        var col = 0

        var index = 0

        var sysValue = 0
        when (person_age) {
            in 20..29 -> index = 0
            in 30..39 -> index = 1
            in 40..49 -> index = 2
            in 50..59 -> index = 3
            in 60..69 -> index = 4
            in 70..79 -> index = 5
            in 80..110 -> index = 6
        }

        //WOMAN
        if (!person_isMan) {
            lowest = 100
            sysValue = blodtrykk_kvinne_sys.get(index = index)

        }

        //MANN
        if (person_isMan) {
            lowest = 110
            sysValue = blodtrykk_mann_sys.get(index = index)

        }


        if (sysPress <= sysValue) {
            col = 0
        }
        if ( sysPress   >= (sysValue+5)) {
            col = 1
        }
        if ( sysPress >= (sysValue+10)) {
            col = 2
        }
        if ( sysPress < lowest) {
            col = 2
        }



        return pressureColor.get(index = col)
    }

    fun returnLimits(diaPress:Int,sysPress:Int ) : Pair<Int,Int> {
        var coldia = 0
        var colsys = 0

        var index = 0
        var diaValue = 0
        var sysValue = 0

        when (person_age) {
            in 20..29 -> index = 0
            in 30..39 -> index = 1
            in 40..49 -> index = 2
            in 50..59 -> index = 3
            in 60..69 -> index = 4
            in 70..79 -> index = 5
            in 80..90 -> index = 6
        }


        //WOMAN
        if (!person_isMan) {
            diaValue = blodtrykk_kvinne_dia.get(index = index)
            sysValue = blodtrykk_kvinne_sys.get(index = index)
            lowest = 100
            lowestDia = diaValue - 10
        }

        //MANN
        if (person_isMan) {
            diaValue = blodtrykk_mann_dia.get(index = index)
            sysValue = blodtrykk_mann_sys.get(index = index)
            lowest = 110
            lowestDia = diaValue - 13

        }


        if (diaPress < diaValue) {
            coldia = 0
        }
        if ( diaPress   >= (diaValue+2)) {
            coldia = 1
        }
        if ( diaPress  >= (diaValue+3)) {
            coldia = 2
        }
        if ( diaPress < lowestDia) {
            coldia = 2
        }

        if (sysPress < sysValue) {
            colsys = 0
        }
        if ( sysPress   >= (sysValue+5)) {
            colsys = 1
        }
        if ( sysPress  >= (sysValue+10)) {
            colsys = 2
        }
        if ( sysPress < lowest) {
            colsys = 2
        }

        return  Pair(coldia,colsys)
    }
    fun returnColorDia(diaPress: Int): String {

        var col = 0

        var index = 0
        var diaValue = 0

        when (person_age) {
            in 20..29 -> index = 0
            in 30..39 -> index = 1
            in 40..49 -> index = 2
            in 50..59 -> index = 3
            in 60..69 -> index = 4
            in 70..79 -> index = 5
            in 80..90 -> index = 6
        }

        //WOMAN
        if (!person_isMan) {
            diaValue = blodtrykk_kvinne_dia.get(index = index)
            lowestDia = diaValue - 10
        }

        //MANN
        if (person_isMan) {
            diaValue = blodtrykk_mann_dia.get(index = index)
            lowestDia = diaValue - 13

        }
        if (diaPress < diaValue) {
            col = 0
        }
        if ( diaPress   >= (diaValue+2)) {
            col = 1
        }
        if ( diaPress  >= (diaValue+3)) {
            col = 2
        }
        if ( diaPress < lowestDia) {
            col = 2
        }

        return pressureColor.get(index = col)
    }


}

