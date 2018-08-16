package com.kempinger.meinemedikamente.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import bolts.Task.delay
import com.kempinger.meinemedikamente.R
import kotlinx.android.synthetic.main.activity_about2.*
import java.lang.Thread.sleep


var disclaimerisShown = false

class About2Activity : AppCompatActivity() {

    private fun fadeOut(img: View) {
        val fadeInAnimation = AnimationUtils.loadAnimation(
                this, R.anim.alpha_to)
        img.startAnimation(fadeInAnimation)
    }

    private fun fadeIn(img: View) {
        val fadeInAnimation = AnimationUtils.loadAnimation(
                this, R.anim.alpha_from)
        img.startAnimation(fadeInAnimation)
    }


    private lateinit var mRunnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about2)
        disclaimerisShown = false
        val atxt = getString(R.string.about_01) + "\n\n" + getString(R.string.about_02)


        val ctxt = getString(R.string.copyright)
        val atxt2 = getString(R.string.about_03)
        val ttxt = atxt + "\n\n\n\n\n\n" + atxt2


        //colorpicker01.elevation = 10F

        buttondisclaimer.setOnClickListener {

            if (!disclaimerisShown) {
                val disclaimerText = """
DISCLAIMER

The information contained on MedicAlarm mobile app (the "Service") is for general information purposes only.
 MedicAlarm assumes no responsibility for errors or omissions in the contents on the Service.
In no event shall be liable for any special, direct, indirect, consequential, or incidental damages or any damages whatsoever, whether in an action of contract, negligence or other tort, arising out of or in connection with the use of the Service or the contents of the Service. reserves the right to make additions, deletions, or modification to the contents on the Service at any time without prior notice.
© Copyright 2018 by E.B.Kempinger
            """.trimIndent()

                fadeOut(about_text)
                about_text.textSize = 12F
                about_text.text = disclaimerText
                fadeIn(about_text)

                hideInfo(imageView8)
            } else {
                fadeOut(about_text)
                about_text.textSize = 18F
                about_text.text = ttxt
                fadeIn(about_text)


                placeInfo(imageView8)
            }
            disclaimerisShown = !disclaimerisShown

        }
        fun colorizeColorPickers() {

            for (n in 0..colorselected.size - 1) {
                val assignColor = Color.parseColor(colorselected.get(index = n))
                when (n) {

                    0 -> colorpicker01.setColorFilter(assignColor)
                    1 -> colorpicker02.setColorFilter(assignColor)
                    2 -> colorpicker03.setColorFilter(assignColor)
                    3 -> colorpicker04.setColorFilter(assignColor)
                    4 -> colorpicker05.setColorFilter(assignColor)
                    5 -> colorpicker06.setColorFilter(assignColor)
                    else -> colorpicker01.setColorFilter(assignColor)
                }
            }

        }
        //colorizeColorPickers()

        /*  fun animateMenu() {


              val autoTransition = AutoTransition()
              val millsec = 900L
              autoTransition.duration = millsec
              val constrainSet1 = ConstraintSet()
              val constrainSet2 = ConstraintSet()

              // cons_set1 = from second xml file
              val constraintLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.about_main_fadein)
              constrainSet2.clone(this, R.layout.activity_about2)

              constrainSet1.clone(constraintLayout)

  //           val mtransition = ChangeBounds()
  //            //mtransition.interpolator = BounceInterpolator()
  //           // mtransition.interpolator = AnticipateInterpolator(1.0f)
  //
  //            mtransition.interpolator = AccelerateInterpolator(0.5f)
  //
  //             mtransition.interpolator = OvershootInterpolator(0.2f)

              TransitionManager.beginDelayedTransition(constraintLayout, autoTransition)
              constrainSet2.applyTo(constraintLayout)


          }*/


        val imgArray = arrayListOf<ImageView>(colorpicker01, colorpicker02, colorpicker03, colorpicker04, colorpicker05, colorpicker06)



        fun doBounce(img: View) {
            val bounceImg = AnimationUtils.loadAnimation(this, R.anim.bounce_image_sharedinterpolator_expand)

            img.startAnimation(bounceImg)

        }


        fun runanimation(){
            imgArray.forEach {
                doBounce(it)




            }
        }

        runanimation()

        about_text.text = ttxt
        copyright.text = ctxt
        button_close.setOnClickListener {
            closeActivity()
        }



        colorpicker01.setOnClickListener {

            savedColorID = 0
            saveAndQuit()

        }

        colorpicker02.setOnClickListener {

            savedColorID = 1
            saveAndQuit()
        }

        colorpicker03.setOnClickListener {

            savedColorID = 2
            saveAndQuit()
        }

        colorpicker04.setOnClickListener {

            savedColorID = 3
            saveAndQuit()

        }
        colorpicker05.setOnClickListener {

            savedColorID = 4
            saveAndQuit()
        }

        colorpicker06.setOnClickListener {

            savedColorID = 5
            saveAndQuit()


        }
    }

    private fun saveAndQuit() {
        saveToShared(savedColorID)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun saveToShared(p2: Int) {

        //p2  0 = brown
        //p2  1 = blue
        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        val editor = prefs!!.edit()

        editor.putInt(prefname, p2)

        editor.apply()


    }

    private fun closeActivity() {

        finish()


    }

    private fun hideInfo(img: View) {
        img.startAnimation(TranslateAnimation(0f, 0f, 0f, moveitems * 2).apply {
            duration = mduration * 2
        })
        img.visibility = View.INVISIBLE
    }

    private fun placeInfo(img: View) {
        img.visibility = View.VISIBLE


        img.startAnimation(TranslateAnimation(0f, 0f, moveitems * 2, 0f).apply {
            duration = mduration * 2

        })

    }

}
