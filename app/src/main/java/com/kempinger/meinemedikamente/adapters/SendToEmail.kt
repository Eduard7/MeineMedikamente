package com.kempinger.meinemedikamente.adapters

import android.content.Context
import com.kempinger.meinemedikamente.*
import com.kempinger.meinemedikamente.activities.*


class SendToEmail (val context: Context){



    var bodyStr = ""  // Utskrift av blodtrykk /n Dato : fra -- til

  //  val context = context
    private   val tab = " \b \b \b"

    private val newline = "\n"

    fun makeready() : String{

//        fun fmtInt(i:Int) : String {
//
//            return String.format("% 6d",i)
//        }
        val dbsize = dbdato.size
        val sum1 = dbpuls.sumBy { it.toInt() }
        val sum2 = dbdiatolic.sumBy { it.toInt() }
        val sum3 = dbsystolic.sumBy { it.toInt() }
        val minDIA = dbdiatolic.min().toString()
        val minSYS = dbsystolic.min().toString()
        val minPLS = dbpuls.min().toString()
        val maxDIA = dbdiatolic.max().toString()
        val maxSYS = dbsystolic.max().toString()
        val maxPLS = dbpuls.max().toString()
        val diastr = context.getString(R.string.diat).toString()
        val sysstr = context.getString(R.string.syst).toString()
        val pls =  context.getString(R.string.pulst).toString()
        val datime = context.getString(R.string.datum)
        val linjenr = context.getString(R.string.line)
        val periode = context.getString(R.string.periode)




        bodyStr = linjenr + tab + tab + tab + sysstr  + tab + tab +  diastr  + tab + tab + pls + tab + tab + datime
        bodyStr += newline + "____________________________________________________" + newline + newline

        for (n in 0..dbsystolic.size-1 ){

            val linje = String.format("%03d", n + 1)
            val a = String.format("%5d", dbsystolic.get(index = n).toInt())
            val b = String.format("%5d", dbdiatolic.get(index = n).toInt())
            val c = String.format("%5d", dbpuls.get(index = n).toInt())
            val d = String.format("%16s", dbdato.get(index = n) )
            val e =  String.format("%1s", dbKlokka.get(index = n))
            bodyStr += linje + " ->"  + tab + tab +tab +
                    a + tab + tab + tab + tab +
                    b + tab + tab + tab + tab +
                    c + tab + tab + tab + tab +
                    d + tab + tab + tab + tab +
                    e + newline
        }


        bodyStr += newline + "____________________________________________________" + newline + newline + newline


        //val diastr2 = context.getString(R.string.chartLableDia).toString()
       // val sysstr2 = context.getString(R.string.chartLableSys).toString()
        val pls2 =  context.getString(R.string.chartLablePuls).toString()
        val avgstr = context.getString(R.string.avg)
        val maxstr = context.getString(R.string.maximum)
        val minstr = context.getString(R.string.minimum)


        val sysstrmax = maxSYS.padEnd(20)

        val diastrmax =   maxDIA.padEnd(20)
        val plsstrmax =   maxPLS.padEnd(20)
        val sysstrmin =  minSYS.padEnd(20)
        val diastrmin =  minDIA.padEnd(20)
        val plsstrmin = minPLS.padEnd(20)
        val s3 = (sum3 / dbsize).toString()
        val s2 = (sum2 / dbsize).toString()
        val s1 = (sum1 / dbsize).toString()
        val sysstravg= s3.padEnd(20)
        val diastravg =  s2.padEnd(20)
        val plsstravg = s1.padEnd(20)

        val frmstr = "%20s"
       // val frmstr2 = "%20s"
        val padint = 14

        bodyStr += linjenr + String.format(frmstr,maxstr) + tab + tab + String.format(frmstr, minstr) + tab + tab + String.format(frmstr,avgstr)  + newline + newline
        bodyStr += sysstr.padEnd(padint+8)+ tab + tab + tab +
                sysstrmax + tab + tab +
                sysstrmin + tab + tab +
                sysstravg + newline
        bodyStr += diastr.padEnd(padint+11) + tab + tab + tab +
                diastrmax + tab + tab +
                diastrmin + tab + tab +
                diastravg + newline
        bodyStr += pls2.padEnd(padint+11) + tab + tab + tab +
                plsstrmax + tab + tab +
                plsstrmin + tab + tab +
                plsstravg + newline

        bodyStr +=  "____________________________________________________" + newline + newline
        bodyStr += periode + " :" + tab + fromDate + "  -  " + toDate

        return bodyStr

    }




}