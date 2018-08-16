package com.kempinger.meinemedikamente.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.transition.AutoTransition
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.animation.*
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.adapters.gangerAntall
import com.kempinger.meinemedikamente.model.*
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_medikament_einnahme.*
import java.text.SimpleDateFormat
import java.util.*

// [] {};';':":

var ablaufUpdate = false
var alarmChanged = false
var alarmStatus = false

class MedikamentEinnahme : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medikament_einnahme_fadein)
       overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
        setBackGroundColor(false)

        if (trialVersion) {

            if (trialSlutt) {

                Toast.makeText(this, R.string.main_achtung_toast_09, Toast.LENGTH_LONG).show()
                finishAndRemoveTask()

                this.finishAffinity()

            }
        }




        fun animateMenu() {


            val  autoTransition =   AutoTransition()
            val millsec = 1100L
            autoTransition.duration = millsec
            val constrainSet1 = ConstraintSet()
            val constrainSet2 = ConstraintSet()

            // cons_set1 = from second xml file


            val mtransition = ChangeBounds()

            mtransition.interpolator = OvershootInterpolator()
            val constraintLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.einnahme_bck2)
            constrainSet2.clone(this, R.layout.activity_medikament_einnahme)

            constrainSet1.clone(constraintLayout)



            TransitionManager.beginDelayedTransition(constraintLayout,autoTransition)
            constrainSet2.applyTo(constraintLayout)


        }
        Handler().postDelayed({

            animateMenu()
          //  setBackGroundColor(true)


        }, 50L * 1L)

        button_bestaetning.isEnabled = true
        Realm.init(this)

      /*  restoreFromShared()*/

        button_bestaetning.setOnClickListener {
            button_bestaetning.isEnabled = false
            saveAlert()
            blinktext(false)
        }

        if (gangerAntall == 0) {
            gangerAntall = 1
        }

        alarmChanged = false
        button_alarm.isChecked = dbalarm
        alarmStatus = dbalarm


        if (dbanzahlgenommen == 0 ){
            button_missed.isEnabled = false
            button_missed.alpha = 0.5f
        } else {
            button_missed.isEnabled = true
            button_missed.alpha = 1f
        }


        button_missed.setOnClickListener {
            val intent = Intent(this, DayLogActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
        }


        medikamentlabel.text = dbmedikament
        dosierungLabel.text = dbdosis
        vmalabelText.text = dbVdag


        if (dbAntallIPackning > 0) {

            val dzahl = dbanzahlgenommen * gangerAntall
            var tmps = getString(R.string.eingenommen) + " " + dzahl.toString() + " " + getString(R.string.off) + " $dbAntallIPackning"
            if (dzahl >= dbAntallIPackning) {

                einnahmeLabel.setTextColor(Color.YELLOW)
                tmps = varsel + " " + tmps
            }
            einnahmeLabel.text = tmps
        }
        if (dbAntallIPackning == 0) {
            val tmps2 = getString(R.string.eingenommen) + " " + dbanzahlgenommen.toString()
            einnahmeLabel.text = tmps2
        }


        datum_heute.text = GetTodaysDate.getDateTime().toString()

        val cal = Calendar.getInstance()
        val dagstr = cal.get(Calendar.DAY_OF_WEEK)
        val str1 = getString(R.string.bestattigen1)
        val str2 = getString(R.string.bestattigen2)+"!"
        val str3 = str1 + " ${dbanzahlgenommen + 1} " + str2
        textblink.text = str3

        button_alarm.setOnClickListener {
/*
            val springAnimation = SpringAnimation(imageViewWecker2,DynamicAnimation.TRANSLATION_Y,0f)

            springAnimation.spring.stiffness = 0.1f
            springAnimation.spring.dampingRatio = 3f
            springAnimation.setStartVelocity(80.0f)
            springAnimation.start()*/
            val v : View = findViewById(android.R.id.content)

            if (button_alarm.isChecked) {





                val timePicker = TimePicker(this)
                timePicker.setOnTimeChangedListener { _, _, _ ->
                }

                val intent = Intent(this,SetAlarmActivity::class.java)
                intent.putExtra(REPEAT_ALARM, dbtgl)

                startActivity(intent)


                imageViewWecker2.setImageResource(R.drawable.ic_alarm_on_black_24dp)
            } else {



                SnackBar().showInfo(v,getString(R.string.einnahme_achtung_toast_01),showGreenChar)
                imageViewWecker2.setImageResource(R.drawable.ic_alarm_off_black_24dp)
                setalarm()


            }


        }


        toggle1.setBackgroundResource(R.drawable.toogle_button_style)
        toggle2.setBackgroundResource(R.drawable.toogle_button_style)
        toggle3.setBackgroundResource(R.drawable.toogle_button_style)
        toggle4.setBackgroundResource(R.drawable.toogle_button_style)
        toggle5.setBackgroundResource(R.drawable.toogle_button_style)
        toggle6.setBackgroundResource(R.drawable.toogle_button_style)
        toggle7.setBackgroundResource(R.drawable.toogle_button_style)

        when (dagstr) {
            1 -> {
                toggle7.setBackgroundResource(R.drawable.toogle_button_style_day_indikator)
                toggle7.setTextColor(Color.BLACK)
            }

            2 -> {
                toggle1.setBackgroundResource(R.drawable.toogle_button_style_day_indikator)
                toggle1.setTextColor(Color.BLACK)
            }
            3 -> {
                toggle2.setBackgroundResource(R.drawable.toogle_button_style_day_indikator)
                toggle2.setTextColor(Color.BLACK)
            }
            4 -> {
                toggle3.setBackgroundResource(R.drawable.toogle_button_style_day_indikator)
                toggle3.setTextColor(Color.BLACK)
            }
            5 -> {
                toggle4.setBackgroundResource(R.drawable.toogle_button_style_day_indikator)
                toggle4.setTextColor(Color.BLACK)
            }
            6 -> {
                toggle5.setBackgroundResource(R.drawable.toogle_button_style_day_indikator)
                toggle5.setTextColor(Color.BLACK)
            }
            7 -> {
                toggle6.setBackgroundResource(R.drawable.toogle_button_style_day_indikator)
                toggle6.setTextColor(Color.BLACK)
            }


        }

        /*DELET LOG*/
        fun deleteLog() {

            val config = RealmConfiguration.Builder()
                    .name("logfile.realm").build()

            val realm = Realm.getInstance(config)

            val medlog = realm.where(DBlogfile::class.java).equalTo("dbID", dbuuid).findAll()

            realm.beginTransaction()
            medlog.deleteAllFromRealm()
            realm.commitTransaction()

            val intent = Intent(this, MedikamentListe::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)

        }

        fun deleteMedisin() {

            val tablename = TABLENAME_MEDIKA
            val config = RealmConfiguration.Builder()
                    .name(tablename).build()

            val realm = Realm.getInstance(config)


            val medk = realm.where(DBmedizin::class.java).equalTo("dbID", dbuuid).findAll()

            realm.beginTransaction()
            medk.deleteFirstFromRealm()
            realm.commitTransaction()


        }


        /*DISPLAY ALERT*/
        fun deleteAlert() {
            val simpleAlert = AlertDialog.Builder(this).create()

            simpleAlert.setTitle(getString(R.string.save_achtung_toast_01))
            simpleAlert.setMessage("$dbmedikament -> \uD83D\uDDD1 ? \n\n" + getString(R.string.einnahme_achtung_toast_02t))

            simpleAlert.setIcon(R.drawable.warning)
            val v : View = findViewById(android.R.id.content)

            val ry = getString(R.string.einnahme_achtung_toast_03)
            val rn = getString(R.string.einnahme_achtung_toast_04)
            val rc = getString(R.string.save_achtung_toast_05)
            /*ERSTATT dialogInterface*/
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, ry) { _, _ ->
                deleteMedisin()
                deleteLog()

            }

            /*ERSTATT dialogInterface*/
            simpleAlert.setButton(AlertDialog.BUTTON_NEGATIVE, rn) { _, _ ->

                SnackBar().showInfo(v,"$rc [$rn]",!showGreenChar)
            }


            simpleAlert.show()
            simpleAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#0000FF"))
            simpleAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0000"))

        }
        button_delte.setOnClickListener {
            deleteAlert()
        }

        //activate blinking
        blinktext(true)


    }

    private fun blinktext(on:Boolean) {

        val anim = AlphaAnimation(0.0f, 1.0f)

        anim.duration = 400 //You can manage the time of the blink with this parameter
        anim.startOffset = 250
        anim.repeatMode = Animation.REVERSE
        if (on) {

            textblink.setTextColor(Color.GREEN)
            textblink.setSingleLine()
            textblink.isSelected = true
           // anim.repeatCount = Animation.INFINITE
           // textblink.startAnimation(anim)

        } else {
           // anim.repeatCount = Animation.ABSOLUTE

            textblink.isSelected = false
           // textblink.startAnimation(anim)
            textblink.setTextColor(Color.GRAY)
        }
    }


    private fun saveAlert() {
        val simpleAlert = AlertDialog.Builder(this).create()
        val tv = TextView(this)
        tv.setBackgroundResource(R.color.action_weiter_color)
        tv.textSize = 24F
        simpleAlert.setTitle(getString(R.string.save_achtung_toast_01))
        val str1 = getString(R.string.einnahme_achtung_toast_05)

        simpleAlert.setMessage("$str1 \n$dbmedikament!")

        simpleAlert.setIcon(R.drawable.medikament_logo_a)


        val ry = getString(R.string.save_achtung_toast_03)
        val rn = getString(R.string.save_achtung_toast_04)
        val rc = getString(R.string.save_achtung_toast_05)
        /*ERSTATT dialogInterface*/
        simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, ry) { _, _ ->

            val v : View = findViewById(android.R.id.content)
            SnackBar().showInfo(v,"$rc [$ry]",showGreenChar)
            updateDB()
        }

        /*ERSTATT dialogInterface*/
        val v : View = findViewById(android.R.id.content)
        simpleAlert.setButton(AlertDialog.BUTTON_NEGATIVE, rn) { _, _ ->

            SnackBar().showInfo(v,"$rc [$rn]",!showGreenChar)
        }


        simpleAlert.show()
        simpleAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#0000FF"))
        simpleAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0000"))

    }

    //* SAVE LOG*/

    private fun saveLog() {


        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .name("logfile.realm").build()

        val realm = Realm.getInstance(config)

        realm.beginTransaction()

        val tm = Date()
        val tdatoLong = (tm.time)


        val logDB = realm.createObject(DBlogfile::class.java)

        logDB.dbID = dbuuid
        logDB.dbLetztesDatum = GetTodaysDate.getDateTime().toString()
        val uz = SimpleDateFormat("H:mm", locale).format(Date().time)
        logDB.dbUhrZeit = uz.toString()
        logDB.dbMedikament = dbmedikament
        logDB.dbDosis = dbdosis
        logDB.dbDatoLong = tdatoLong


        realm.commitTransaction()





    }

    // UPDATE WECKER
    private fun updateWecker() {


        if (setWecker) {
            Realm.init(this)
            val tablename = TABLENAME_MEDIKA
            val config = RealmConfiguration.Builder()
                    .name(tablename).build()

            val realm = Realm.getInstance(config)
            realm.beginTransaction()

            var um = "$uhrzeitMinutte"
            if (uhrzeitMinutte < 10) {
                um = "0$uhrzeitMinutte"

            }

            val medk = realm.where(DBmedizin::class.java).equalTo("dbID", dbuuid).findFirst()


            medk!!.dbAlarm = button_alarm.isChecked

            medk.dbAlarmZeit = "$uhrzeitStunde:$um"


            realm.commitTransaction()

        }


        setWecker = false


    }


    /*
    // UPDATE ABLAUFDATUM
    private fun updateAblaufDatum() {
        Realm.init(this)
        val tablename = "medika.realm"
        val config = RealmConfiguration.Builder()
                .name(tablename).build()

        val realm = Realm.getInstance(config)
        realm.beginTransaction()


        val medk = realm.where(DBEntities::class.java).equalTo("dbID", dbuuid).findFirst()

        medk!!.dbAblauf = dbablauf
        realm.commitTransaction()

    }
   */

//UPDATE DAY LOG


    // UPDATE DB
    private fun updateDB() {
        Realm.init(this)

        val tablename = TABLENAME_MEDIKA

        val config = RealmConfiguration.Builder()
                .name(tablename).build()

        val realm = Realm.getInstance(config)
        realm.beginTransaction()


        val medk = realm.where(DBmedizin::class.java)
                .equalTo("dbID", dbuuid).findFirst()

        medk!!.dbAnzahlGenommen += 1


        medk!!.dbAlarm = button_alarm.isChecked
        medk.dbAblauf = dbablauf

        medk.dbLetztesDatum = GetTodaysDate.getDateTime().toString()
        val anz = medk.dbAnzahlGenommen


        ablaufUpdate = false


        val tmp = getString(R.string.eingenommen) + " " + anz.toString()

        einnahmeLabel.text = tmp
        realm.commitTransaction()
        saveLog()

    }

    /*SWITC ON OF*/
    private fun setalarm() {


        alarmStatus = false
        val alarmIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS)

        startActivity(alarmIntent)

    }


    private fun setImage() {


        if (!alarmChanged) {

            button_alarm.isChecked = alarmStatus
        }
        if (button_alarm.isChecked) {


            imageViewWecker2.setImageDrawable(getDrawable(R.drawable.ic_alarm_on_black_24dp))
        } else {

            imageViewWecker2.setImageDrawable(getDrawable(R.drawable.ic_alarm_off_black_24dp))


        }


    }

    private fun setBackGroundColor(mmain: Boolean) {
        val window = this.getWindow()
        if (mmain) {
            Globals.setBackGroundColor(window,findViewById<ConstraintLayout>(R.id.einnahme_bck))
        } else {
            Globals.setBackGroundColor(window,findViewById<ConstraintLayout>(R.id.einnahme_bck2))
        }
    }


    override fun onResume() {
        super.onResume()
        setImage()
        setWecker = true
        updateWecker()


    }



}
