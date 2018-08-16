package com.kempinger.meinemedikamente.model

import android.content.Context
import android.widget.Toast
import com.kempinger.meinemedikamente.activities.locale
import java.text.SimpleDateFormat
import java.util.*

class DateConverter
{



    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm", locale)
        return format.format(date)

    }

    fun dateDiff(y:Int,m:Int, d:Int) : Int{

        val locale : Locale? = Locale.getDefault()
        val kalender = Calendar.getInstance(locale)


        val tYear = kalender.get(Calendar.YEAR)
        val tMonth = kalender.get(Calendar.MONTH)+1
        val tDay = kalender.get(Calendar.DAY_OF_MONTH)


        val startUpDate = Calendar.getInstance()
        val todaysDate = Calendar.getInstance()

        startUpDate.clear()
        startUpDate.set(y, m, d)
        todaysDate.clear()
        todaysDate.set(tYear, tMonth, tDay)

        val timeDifference = todaysDate.timeInMillis - startUpDate.timeInMillis

        val dayCount = timeDifference.toFloat() / (24 * 60 * 60 * 1000)
        return  dayCount.toInt()
    }

}