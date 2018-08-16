package com.kempinger.meinemedikamente.activities


import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.support.v7.app.AppCompatActivity
import android.widget.Switch
import com.kempinger.meinemedikamente.R
import kotlinx.android.synthetic.main.activity_set_alarm.*


var uhrzeitStunde : Int = 8
var uhrzeitMinutte : Int = 15
class SetAlarmActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm)


        context = this
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager



        if (!hourswitch.isChecked) {
            hourswitch.text = getString(R.string.hour24).toString()
        } else {
            hourswitch.text = getString(R.string.hourampm).toString()
        }

        val sw = findViewById<Switch>(R.id.hourswitch)

        sw?.setOnClickListener {
            theTimePicker.setIs24HourView(hourswitch.isChecked)
            if (!hourswitch.isChecked) {
                hourswitch.text = getString(R.string.hour24).toString()
            } else {
                hourswitch.text  = getString(R.string.hourampm).toString()
            }
        }


        weckerLabel.setText(dbmedikament)
        var weckerText = dbmedikament
        theTimePicker.setIs24HourView(true)




        theTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->

            var m = "$minute"
            if(m.length==1) m="0$m"
            val h = "$hourOfDay:$m"
            klokkalabel.text = h
        }
        setWecker = false
        alarmChanged = false


        resetbutton.setOnClickListener {

            val intent = Intent(this, SetAlarmActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            dbtgl = 0
            startActivity(intent)




        }

            button_update.setOnClickListener {


                alarmChanged = true
                val hour = theTimePicker.hour
                val minute = theTimePicker.minute

                val dager = arrayListOf<Int>()


                if (weckerLabel.text.isEmpty()) {

                    weckerText = "Medikamenteinnahme"
                } else {

                    weckerText = weckerLabel.text.toString()
                }

                uhrzeitStunde = hour
                uhrzeitMinutte = minute

                setWecker = true
                button_update.isEnabled = false

                if (montag.isChecked) dager.add(2)
                if (dienstag.isChecked) dager.add(3)
                if (mittwoch.isChecked) dager.add(4)
                if (donnerstag.isChecked) dager.add(5)
                if (freitag.isChecked) dager.add(6)
                if (samstag.isChecked) dager.add(7)
                if (sonntag.isChecked) dager.add(1)

                var uz = uhrzeitStunde

                var steps : Int
                when (dbtgl) {
                    0 -> steps = 0
                    1 -> steps = 7
                    2 -> steps = 5
                    3 -> steps = 4

                    else -> steps = 3
                }

                for (i in 0..dbtgl) {

                    val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM)
                    alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, weckerText)
                    alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, uz)
                    alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, uhrzeitMinutte)
                    alarmIntent.putExtra(AlarmClock.EXTRA_DAYS, dager)
                    startActivity(alarmIntent)
                    uz += steps
                    if (uz >=24) {uz = uz - 24}
            }

            }


    }



}
