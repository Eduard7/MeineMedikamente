package com.kempinger.meinemedikamente.model

import android.content.Context
import com.kempinger.meinemedikamente.activities.locale
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*

class GetTimeAgo {

     val context : Context? = null
    fun days(diff: Int): Int {

        return   when (diff) {

             0 -> 10
            in 1..7 -> 0
            in 8..29 -> 1
            in 30..364 -> 2
            in 365..36500 -> 3
            else -> 11

        }

    }

    fun convertDays(diff:Int) :Int {

        return when (diff) {
            in 0..7 ->  diff
            in 8..30 ->  round(diff.toDouble() / 7.0).toInt()
            in 31..365 ->   round(diff.toDouble() / 30.0).toInt()
            else ->  round(diff.toDouble() / 365.0).toInt()

        }
    }
}

//object GetNextDate {
//    fun getNextDate() : String {
//        val locale = Locale.getDefault()
//        val cal = Calendar.getInstance()
//        val formatter = SimpleDateFormat("EEE dd.MM.yyyy", locale)
//        cal.time = (Date())
//        cal.add(Calendar.DAY_OF_MONTH, 1)
//        val nextDate  = formatter.format(cal.time).toString().capitalize()
//        return nextDate
//    }
//}

    // LocalDate.now().plusDays(1)

object GetTodaysDate {

   private  val locale = Locale.getDefault()
    fun getDateTime(): String? {


        val formatter = SimpleDateFormat("EEE dd.MM.yyyy", locale)
        val dagstr = formatter.format(Date()).toString()
        val locale : Locale? = Locale.getDefault()

        val kalender = Calendar.getInstance(locale)
        val tYear = kalender.get(Calendar.YEAR)
        val tMonth = kalender.get(Calendar.MONTH)+1
        val tDay = kalender.get(Calendar.DAY_OF_MONTH)
        var tDayStr = tDay.toString()
        if(tDay < 10 )   tDayStr = "0$tDay"
        var tMndStr = tMonth.toString()
        if(tMonth <10 )  tMndStr = "0$tMonth"
        val pnkt = "."

        return dagstr.substring(0,3) + " " + tDayStr + pnkt + tMndStr + pnkt + tYear.toString()

    }
}

//object  GetTime {
//    fun getTime(): String? {
//
//        val formatter = SimpleDateFormat("H:mm",locale)
//
//        return formatter.format(Date()).toString()
//
//    }
//}
object GetDateForDB {
    //val locale = Locale.getDefault()
    fun getDateTime(): String? {

        val formatter = SimpleDateFormat("yyyy.MM.dd  H:mm", locale)

        return formatter.format(Date()).toString()

    }
}
//object GetDateForDayLog {
//    //val locale = Locale.getDefault()
//    fun getDateTime(): String? {
//
//        val formatter = SimpleDateFormat("MMMM d, yyyy",locale)
//
//        return formatter.format(Date()).toString()
//
//    }
//}
object ReturnFormatedDate {

    fun theDateToReturn(dte: Date): String? {

        val formatter = SimpleDateFormat("dd.MM.yyyy", locale)

        return formatter.format(dte).toString()

    }
}
