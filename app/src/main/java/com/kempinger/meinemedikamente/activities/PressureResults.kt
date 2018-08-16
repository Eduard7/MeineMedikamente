package com.kempinger.meinemedikamente.activities

import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.widget.ImageView
import com.kempinger.meinemedikamente.R

import com.kempinger.meinemedikamente.model.DisplayPressureColorScala
import com.kempinger.meinemedikamente.model.Globals

import kotlinx.android.synthetic.main.activity_pressure_results.*
import java.text.DecimalFormat

class PressureResults : AppCompatActivity() {

    lateinit var animationHeart : AnimationDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pressure_results)
        val window = this.getWindow()
        Globals.setBackGroundColor(window,findViewById<ConstraintLayout>(R.id.pressure_result_id))
        displayResultSoFar()
        button_goback.setOnClickListener {
            finish()
        }
        heartAnimation()
    }

    private fun heartAnimation(){

        val img = findViewById<ImageView>(R.id.heartid)
        img.setBackgroundResource(R.drawable.heartbeat) // load images into background
        val frameAnimation: AnimationDrawable = img.background as AnimationDrawable

        frameAnimation.start()



    }
    private fun displayResultSoFar() {

        val foodnote = "¹⁾ "
        if (!person_isMan) {
            val p =  foodnote + getString(R.string.sextypeW) + " / " + getString(R.string.age) + " " + person_age.toString()
            label_person.text = p

        } else {
            val p = foodnote + getString(R.string.sextypeM) + " / " + getString(R.string.age) + " " +person_age.toString()
            label_person.text = p
        }

        val dbsize = dbdato.size
        val tmps = getString(R.string.number_of_emasurements) + " $dbsize"
        textView42.text = tmps

        val desimalformat = DecimalFormat("###.0")
        if (dbsize >1 ) {
            val diaLimitSys = arrayListOf<Int>()
            val diaLimitDia = arrayListOf<Int>()



            for (n in 0..dbdiatolic.size - 1) {

                val (diabolValue,sysbolValue) = DisplayPressureColorScala()
                        .returnLimits(dbdiatolic.get(index = n)
                                .toInt(), dbsystolic.get(index = n).toInt())


                diaLimitDia.add(diabolValue)
                diaLimitSys.add(sysbolValue)

            }

            val overLimitSys = diaLimitSys.count { x -> x > 0 }
            val underLimitSys = diaLimitSys.count { x -> x == 0 }
            val overLimitDia = diaLimitDia.count { x -> x > 0 }
            val underLimitDia = diaLimitDia.count { x -> x == 0 }

            val prosunterSys = (underLimitSys.toFloat() / dbsize.toFloat()).toFloat() * 100
            val prosoverSys = (overLimitSys.toFloat() / dbsize.toFloat()).toFloat() * 100
            val prosunterDia = (underLimitDia.toFloat() / dbsize.toFloat()).toFloat() * 100
            val prosoverDia = (overLimitDia.toFloat() / dbsize.toFloat()).toFloat() * 100


            val tmpf = "%4d"

            var tmpx = String.format(tmpf,underLimitSys)  + " $erlik "
            syslabel_normal_a.text =  tmpx
            val labelSys1 =   String.format("%4.1f", prosunterSys)   +" %"

            tmpx = String.format(tmpf, overLimitSys) + " $erlik "
            syslabel_over_a.text =  tmpx

            val labelSys2 = String.format("%4.1f", prosoverSys) + " %"
            tmpx = String.format(tmpf,underLimitDia) + " $erlik "
            dialabel_normal_a.text =  tmpx
            val labelDia1   = String.format("%4.1f", prosunterDia)  + " %"
            tmpx = String.format(tmpf,overLimitDia) + " $erlik "
            dialabel_over_a.text =  tmpx
            val labelDia2 =   String.format("%4.1f", prosoverDia)  + " %"

            syslabel_normal.text = labelSys1
            syslabel_over.text = labelSys2
            dialabel_normal.text = labelDia1
            dialabel_over.text = labelDia2


        }

    }
}
