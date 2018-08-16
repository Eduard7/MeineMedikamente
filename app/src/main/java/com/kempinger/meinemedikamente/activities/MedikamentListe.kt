package com.kempinger.meinemedikamente.activities


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.adapters.MedikamentAdapter
import com.kempinger.meinemedikamente.model.DBmedizin
import com.kempinger.meinemedikamente.model.DateConverter
import com.kempinger.meinemedikamente.model.Globals

import io.realm.Case
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_medikament_liste.*




var pos = 0
// -- daylog


var mlaststate : Int = 0

class MedikamentListe : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medikament_liste)
        overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)

        setBackGroundColor()


       /* restoreFromShared()*/



      /*  if (trialSlutt) {

            Toast.makeText(this,R.string.main_achtung_toast_09, Toast.LENGTH_LONG)
            finishAndRemoveTask()

            this.finishAffinity()

        }*/

        load()

        assignSortButtons()



      //  button_sort1.isEnabled =  (medikamentArray.size > 2)

        fun reload() {


          //  load()
            val intent = Intent(this, MedikamentListe::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(intent)
            overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
        }



        button_sort1.setOnClickListener {
            selectedChar = button_sort1.text.toString()
            reloadetis = true
            reload()
        }
        button_sort2.setOnClickListener {
            selectedChar =  button_sort2.text.toString()
            reloadetis = true
            reload()
        }
        button_sort3.setOnClickListener {
            selectedChar = button_sort3.text.toString()
            reloadetis = true
            reload()
        }
        button_sort4.setOnClickListener {
            selectedChar = button_sort4.text.toString()
            reloadetis = true
            reload()
        }
        button_sort5.setOnClickListener {
            selectedChar = button_sort5.text.toString()
            reloadetis = true
            reload()
        }
        button_sort6.setOnClickListener {
            selectedChar = button_sort6.text.toString()
            reloadetis = true
            reload()
        }
        button_sort7.setOnClickListener {
            selectedChar = button_sort7.text.toString()

            reload()
        }
        button_sort8.setOnClickListener {
            selectedChar = button_sort8.text.toString()
            reloadetis = true
            reload()
        }
        button_sort9.setOnClickListener {
            selectedChar = button_sort9.text.toString()
            reloadetis = true
            if ( button_sort9.text == "*") {
                reloadetis = false
            }
            reload()
        }


        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main.adapter = MedikamentAdapter()

        val resId = R.anim.layout_animation_fall_down
        val animation = AnimationUtils.loadLayoutAnimation(this, resId)
        recyclerView_main.setLayoutAnimation(animation)
        recyclerView_main.scheduleLayoutAnimation()
        recyclerView_main.setOnScrollChangeListener{ _, _, _, _, _ ->


            if (recyclerView_main.scrollState == 1) {


                if (mlaststate < 2) {
                Handler().postDelayed({

                    mlaststate = 1
                    animateHiddeButtons()
                }, 300L * 1L)
            } else if(recyclerView_main.scrollState == 2) {
                    Handler().postDelayed({
                        mlaststate = 2
                        animateSortButtons()
                    }, 500L * 5L)
                }

            }

        }




    }




    override fun onResume() {
        super.onResume()
      load()
        recyclerView_main.layoutManager = LinearLayoutManager(this)


        recyclerView_main.adapter = MedikamentAdapter()

    }



    private fun animateHiddeButtons() {
        // button_sort1.visibility = View.VISIBLE
        val alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.duration = mduration
        button_sort1.startAnimation(alphaAnimation)
        button_sort2.startAnimation(alphaAnimation)
        button_sort3.startAnimation(alphaAnimation)
        button_sort4.startAnimation(alphaAnimation)
        button_sort5.startAnimation(alphaAnimation)
        button_sort6.startAnimation(alphaAnimation)
        button_sort7.startAnimation(alphaAnimation)
        button_sort8.startAnimation(alphaAnimation)
        button_sort9.startAnimation(alphaAnimation)


    }
    private fun animateSortButtons() {
       // button_sort1.visibility = View.VISIBLE
        mlaststate = 1
        val alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.duration = mduration
        button_sort1.startAnimation(alphaAnimation)
        button_sort2.startAnimation(alphaAnimation)
        button_sort3.startAnimation(alphaAnimation)
        button_sort4.startAnimation(alphaAnimation)
        button_sort5.startAnimation(alphaAnimation)
        button_sort6.startAnimation(alphaAnimation)
        button_sort7.startAnimation(alphaAnimation)
        button_sort8.startAnimation(alphaAnimation)
        button_sort9.startAnimation(alphaAnimation)




    }

    private fun assignSortButtons(){
        button_sort1.visibility = View.INVISIBLE
        button_sort2.visibility = View.INVISIBLE
       // button_sort1.visibility = View.INVISIBLE
        button_sort2.visibility = View.INVISIBLE
        button_sort3.visibility = View.INVISIBLE
        button_sort4.visibility = View.INVISIBLE
        button_sort5.visibility = View.INVISIBLE
        button_sort6.visibility = View.INVISIBLE
        button_sort7.visibility = View.INVISIBLE
        button_sort8.visibility = View.INVISIBLE
        button_sort9.visibility = View.INVISIBLE



        if (reloadetis) {
            button_sort9.visibility = View.VISIBLE
            button_sort9.text = "*"
        }
        val charArray  = arrayListOf<Char>().toHashSet()
        charArray.clear()

        medikamentArray.forEach {
            val firstChar = it.first()
            charArray.add(firstChar)

        }

        val chararray = ArrayList(charArray)
        var chSize = chararray.size

        if (chSize>8) chSize = 9

           if (chSize > 0) {
                button_sort1.text = chararray[0].toString()
                button_sort1.visibility = View.VISIBLE
            }
            if (chSize > 1) {
                button_sort2.text = chararray[1].toString()
                button_sort2.visibility = View.VISIBLE
            }
            if (chSize > 2) {
                button_sort3.text = chararray[2].toString()
                button_sort3.visibility = View.VISIBLE
            }
            if (chSize > 3) {
                button_sort4.text = chararray[3].toString()
                button_sort4.visibility = View.VISIBLE
            }
            if (chSize > 4) {
                button_sort5.text = chararray[4].toString()
                button_sort5.visibility = View.VISIBLE
            }
            if (chSize > 5) {
                button_sort6.text = chararray[5].toString()
                button_sort6.visibility = View.VISIBLE
            }
            if (chSize > 6) {
                button_sort7.text = chararray[6].toString()
                button_sort7.visibility = View.VISIBLE
            }
            if (chSize > 7) {
                button_sort8.text = chararray[7].toString()
                button_sort8.visibility = View.VISIBLE
            }
            if (chSize > 8) {
                button_sort9.text = chararray[8].toString()
                button_sort9.visibility = View.VISIBLE
            }
    }






    private fun load() {


        letztesDatumArray.clear()
        ablaufArray.clear()
        oppstartArray.clear()
        vmallArray.clear()
        alarmsetArray.clear()
        alarmZeitArray.clear()
        genommenArray.clear()
        dosisArray.clear()
        anzahlArray.clear()
        tglArray.clear()
        medikamentArray.clear()
        mahlzeit.clear()
        diffArray.clear()
        tglAntallArray.clear()



        uuidArray.clear()
        Realm.init(this)
        val tablename = TABLENAME_MEDIKA
        val config = RealmConfiguration.Builder().name(tablename).build()
        val realm = Realm.getInstance(config)
        realm.beginTransaction()

        var result = realm.where(DBmedizin::class.java).findAll()


          result = result.sort("dbDate",Sort.DESCENDING)
        if (selectedChar == "*") {}
        else {
            result = realm.where(DBmedizin::class.java).beginsWith("dbMedikament", selectedChar, Case.INSENSITIVE).findAll()
        }


        for (n in result){

           medikamentArray.add(n.dbMedikament)
            vmallArray.add(n.dbVMAN)
            vmaNArray.add(n.dbN)
            mahlzeit.add(n.dbMahlzeit)
            alarmsetArray.add(n.dbAlarm)
            alarmZeitArray.add(n.dbAlarmZeit)
            ablaufArray.add(n.dbAblauf)
            tglArray.add(n.dbTaglich)
            tglAntallArray.add(n.dbAnzahlMedikament)
            letztesDatumArray.add(n.dbLetztesDatum)
            dosisArray.add(n.dbDosisEnhet)
            uuidArray.add(n.dbID)
            genommenArray.add(n.dbAnzahlGenommen)
            oppstartArray.add(n.dbStartDatum!!)
            anzahlArray.add(n.dbAnzahl)


            val tm = n.dbStartDatum!!
            val td = (tm.time)
            val dt1 = DateConverter().convertLongToTime(td)


            val a = dt1.split("."," ")
            val ay = a.get(index = 0).toInt()
            val am = a.get(index = 1).toInt()
            val ad = a.get(index = 2).toInt()


            val tdx = DateConverter().dateDiff(ay, am, ad)
            diffArray.add(tdx)


        }
        realm.commitTransaction()

    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun setBackGroundColor() {
        val window = this.getWindow()
        Globals.setBackGroundColor(window,findViewById<ConstraintLayout>(R.id.medListe))
    }



}
