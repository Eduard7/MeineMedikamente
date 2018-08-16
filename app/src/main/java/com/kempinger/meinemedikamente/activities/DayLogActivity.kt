package com.kempinger.meinemedikamente.activities

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.model.DBlogfile
import com.kempinger.meinemedikamente.model.Globals
import com.kempinger.meinemedikamente.model.ReturnFormatedDate
import com.kempinger.meinemedikamente.model.SnackBar
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_day_log.*
import java.text.SimpleDateFormat
import java.util.*


class DayLogActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_log)
        val window = this.getWindow()
        Globals.setBackGroundColor(window, findViewById<ConstraintLayout>(R.id.daylog_activity))

        mediklabel.text = dbmedikament
        dosislabel.text = dbdosis
        loadLog()
        val tmpd = logLongDateArray.size.toString()+"x"
        antalllabel.text = tmpd
        showCalendar()
        buttonback.setOnClickListener {
            finish()
        }

    }

    private fun showCalendar() {
        val locale = Locale.getDefault()


        val format = SimpleDateFormat("MMMM - yyyy", locale)
        var dagstr = format.format(Date()).toString()

        mndaar.text = dagstr
        val calendarView = findViewById<CompactCalendarView>(R.id.compactcalendar_view)
        calendarView.setUseThreeLetterAbbreviation(true)


        calendarView.background = getDrawable(R.color.felles_backgrunn)

        calendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {


            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                dagstr = format.format(firstDayOfNewMonth).toString()

                mndaar.text = dagstr
            }

            override fun onDayClick(dateClicked: Date?) {
               // val context = applicationContext

                val v : View = findViewById(android.R.id.content)
                val dc =   ReturnFormatedDate.theDateToReturn(dateClicked!!)
                SnackBar().showInfo(v,"$dc!!",showGreenChar)

            }

        })


    }

   private fun markEvents() {
        val calendarView = findViewById<CompactCalendarView>(R.id.compactcalendar_view)
        calendarView.setUseThreeLetterAbbreviation(true)

        calendarView.invalidate()
        for (n in 0 until logLongDateArray.size) {
            val p = logLongDateArray.get(index = n)
            if (n == 0) {
                val event = Event(Color.RED, p, "")
                calendarView.addEvent(event,false)
            } else {
                val event = Event(Color.WHITE, p, "")
                calendarView.addEvent(event,false)
            }


        }
    }

    private fun loadLog() {

        logMedikamentArray.clear()
        logDatumArray.clear()
        logUhrzeitArray.clear()
        logLongDateArray.clear()

        logTmpArray.clear()


        logantallInntakArray.clear()

        Realm.init(this)


        val config = RealmConfiguration.Builder()
                .name(TABLENAME_LOG).build()

        val realm = Realm.getInstance(config)


        //    dblogSelect="GLOBOIT"


            realm.beginTransaction()
            val log = realm.where(DBlogfile::class.java).equalTo("dbID", dbuuid).findAll()


            log.forEach { logfile ->
                logMedikamentArray.add(logfile.dbMedikament).toString()
                logUhrzeitArray.add(logfile.dbUhrZeit).toString()
                logDosisArray.add(logfile.dbDosis).toString()
                logDatumArray.add(logfile.dbLetztesDatum).toString()
                logLongDateArray.add(logfile.dbDatoLong)
            }
            realm.commitTransaction()

        logLongDateArray.sort()
        markEvents()



    }


}





