package com.kempinger.meinemedikamente.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.AutoTransition
import android.transition.TransitionManager
import com.kempinger.meinemedikamente.R

var wellcome=true
var billing = false
var userpassword = "1234"
var username = "User"
const val PASSWORD ="1234"
const val USERNAME ="User"
var xpassword =""
var xusername = ""

class SplashActivity : AppCompatActivity() {

   private var mDelayHandler: Handler? = null
    private val splashdelay: Long = 3000

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, LogInActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)





        setContentView(R.layout.activity_splash2)



        fun animateMenu() {


            val  autoTransition =   AutoTransition()
            val millsec = 1500L
            autoTransition.duration = millsec
            val constrainSet1 = ConstraintSet()
            val constrainSet2 = ConstraintSet()

            // cons_set1 = from second xml file
            val constraintLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.splash_2)
            constrainSet2.clone(this, R.layout.activity_splash)

            constrainSet1.clone(constraintLayout)

//            val mtransition = ChangeBounds()
//            mtransition.interpolator = AnticipateInterpolator(0.2f)
            //    val mtransition = ChangeBounds()

            //   mtransition.interpolator = OvershootInterpolator(1f)

            TransitionManager.beginDelayedTransition(constraintLayout,autoTransition)
            constrainSet2.applyTo(constraintLayout)

        }
        Handler().postDelayed({

            animateMenu()


        }, 100L * 1L)



        mDelayHandler = Handler()


     mDelayHandler!!.postDelayed(mRunnable, splashdelay)

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }


}

