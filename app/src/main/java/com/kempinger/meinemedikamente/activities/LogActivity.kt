package com.kempinger.meinemedikamente.activities


import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.AnimationUtils
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.adapters.MainAdapter
import com.kempinger.meinemedikamente.model.DBlogfile
import com.kempinger.meinemedikamente.model.Globals

import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_log.*


val logUhrzeitArray = arrayListOf<String>()
val  logLongDateArray = arrayListOf<Long>()
val logDatumArray = arrayListOf<String>()
val logMedikamentArray = arrayListOf<String>()
val logDosisArray = arrayListOf<String>()
val logTmpArray = arrayListOf<String>().toHashSet()
var logHeadings = arrayListOf<String>()
var logantallInntakArray = arrayListOf<Int>()

class LogActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)

        setBackGroundColor()

        if (headingOnly) {
            button_goback.visibility = View.INVISIBLE
            imageBack.visibility = View.INVISIBLE
        } else {
            button_goback.visibility = View.VISIBLE
            imageBack.visibility = View.VISIBLE
        }

        loadLog()
        logHeadings = ArrayList(logTmpArray.sorted())


        recycler_view.layoutManager= LinearLayoutManager(this)
        recycler_view.adapter = MainAdapter()

        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(this, resId)
        recycler_view.setLayoutAnimation(animation)
        recycler_view.scheduleLayoutAnimation()

        button_goback.setOnClickListener {
            goback()
        }
    }


    private fun loadLog() {

        logMedikamentArray.clear()
        logDatumArray.clear()
        logUhrzeitArray.clear()
        logDatumArray.clear()
        logTmpArray.clear()


        logantallInntakArray.clear()

        Realm.init(this)


        val config = RealmConfiguration.Builder()
                .name("logfile.realm").build()

        val realm = Realm.getInstance(config)


        //    dblogSelect="GLOBOIT"
        if (!headingOnly) {



            realm.beginTransaction()
            val log = realm.where(DBlogfile::class.java).equalTo("dbMedikament", dblogSelect).findAll()

            log.forEach { logfile ->
                logMedikamentArray.add(logfile.dbMedikament).toString()
                logUhrzeitArray.add(logfile.dbUhrZeit).toString()
                logDosisArray.add(logfile.dbDosis).toString()
                logDatumArray.add(logfile.dbLetztesDatum).toString()
            }
            realm.commitTransaction()
            headingOnly = false
            val tmp =  "${getText(R.string.used_list)}  \n\n${logMedikamentArray[0]}"
            headingText.text = tmp

        } else {



            realm.beginTransaction()
            val log = realm.where(DBlogfile::class.java).findAll()


            log.forEach { logfile ->

                val g = logfile.dbMedikament

                logTmpArray += g

            }

            realm.commitTransaction()



            headingText.setText(R.string.used_medikament)


        }

    }



    private fun setBackGroundColor() {
        val window = this.getWindow()

        Globals.setBackGroundColor(window,findViewById<ConstraintLayout>(R.id.log_liste_bck))

    }
    private fun goback(){
        val intent = Intent(this, LogActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        headingOnly = true
        startActivity(intent)

    }


}
