package com.kempinger.meinemedikamente.activities


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.kempinger.meinemedikamente.R

import com.kempinger.meinemedikamente.model.DBmedizin
import com.kempinger.meinemedikamente.model.Globals
import com.kempinger.meinemedikamente.model.InitArrays
import com.kempinger.meinemedikamente.model.SnackBar
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


var headingOnly = true
var dblogSelect = ""
var dbVdag = "-"
var dbAntallIPackning = 12
var dbmedikament = ""
var dosistxt = ""
var dbablauf = "-"
var dbalarm = false
var dbalarmzeit = ""
var dbdaggenommen = false
var dbdosis = ""
var dbvmlAll = ""
var dbtgl = 0
var dbmahlzeit = 0
var dbletztesdatum = "-"
var dbanzahlgenommen = 0
var dbuuid = ""
var myemail = ""
var reloadetis = false
var densityDpi = 0
var selectedChar = "*"
var setWecker = false
var pusshMe = true //FF5F1884 546371  0F7373 0a7272 4C395D" a20025
var savedColorID = 0
var colorselected = arrayListOf<String>("#043EB2", "#475059", "#771823", "#113854", "#035F45", "#4C395D")
var useDefaultAlarmActivity = false
var felleBackGroundColorID = 0
const val showGreenChar = true
const val punktPunkt = " •••"
const val ok = "✓"
const val varsel = "⚠️"
val locale: Locale? = Locale.getDefault()


val weckerEinAus = arrayListOf<String>()
var databasesize = 0
const val prefname = "FellesColorBackground"
const val preftrial = "Trialstatus"
const val prefMore = "ToogleImage"
const val preftoggle = "ToggleStatus"
const val filename = "com.kempinger.systembackground.prefs"
const val TABLENAME_MEDIKA = "medika.realm"
const val TABLENAME_PERSON = "person.realm"
const val TABLENAME_PRESSURE = "pressure.realm"
const val TABLENAME_TRIAL = "trial.realm"
const val TABLENAME_LOG = "logfile.realm"
const val REPEAT_ALARM = "repeat"
const val SYSEXTRA = "sysextra"
const val DIAEXTRA = "diaextra"
var trialSlutt = false
var trialStartet = false
const val trialVersion = true


class MainActivity : AppCompatActivity() {


    lateinit var mygesures: GestureDetector

    lateinit var view_ibruk : TextView
    lateinit var view_neu : TextView
    lateinit var view_einnahme : TextView
    lateinit var imageC : ImageView
    lateinit var imageL: ImageView
    lateinit var imageN : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
        //  val v: View = findViewById(android.R.id.content)

        setContentView(R.layout.activity_main)



//        db.getLogDataDao().getAllLogdata()
//        db.getMedisinDao().getMedisinById("ertetetertete")

        view_einnahme = findViewById(R.id.textView_einnahme)
        view_ibruk = findViewById(R.id.textView_imgebrauch)
        view_neu = findViewById(R.id.textView_neu)


        mygesures = GestureDetector(this, MyGestureRutines())

        var touch = View.OnTouchListener { v, event ->
            mygesures.onTouchEvent(event)

        }

//
//        imageView16.setOnLongClickListener(View.OnLongClickListener {




            val fadeInAnimation = AnimationUtils.loadAnimation(
                    this, R.anim.alpha_from)
            view_einnahme.startAnimation(fadeInAnimation)
            val fadeInAnimation0 = AnimationUtils.loadAnimation(
                    this, R.anim.alpha_from)
            view_neu.startAnimation(fadeInAnimation0)
            val fadeInAnimation1 = AnimationUtils.loadAnimation(
                    this, R.anim.alpha_from)
            view_ibruk.startAnimation(fadeInAnimation1)


           // hideMenuText()

//            return@OnLongClickListener true
//
//        })

        fun doBounce(img:View) {
            val bounceImg = AnimationUtils.loadAnimation(this,R.anim.bounce_image)
            img.startAnimation(bounceImg)

        }

        if (trialVersion) {
            trialSlutt = restoreTrailStatus()
            if (trialSlutt) {

                showTrailInfo()

            }
        }


        val rotateSet = AnimatorSet()
        rotateSet.start()


        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (isLoggedIn) {
            logoutImage.alpha = 1f
        } else {
            logoutImage.alpha = 0f
        }
        //LOG OUT FACEBOOK
        logoutImage.setOnClickListener {

            logoutFacebook()
        }


        //RESTORE COLORS
        restoreFromShared()



       // Globals().toast(this,"Hallo Edi")




        if (wellcome) {

            Handler().postDelayed({

                wellcome = false

            }, 500L * 3L)
        }

        weckerEinAus.add(getString(R.string.weckerein))
        weckerEinAus.add(getString(R.string.weckeraus))

        Realm.init(this)

        getScreenSize()

        dosistxt = getString(R.string.dosis)


        val tmp0 = getString(R.string.tgl)

        //val w = getString(R.string.wochentlich)

        InitArrays().initTaglich(tmp0)
        Locale.setDefault(locale)


        // GO TO PRESSURE ACTIVITY
        button_blodtrykk.setOnClickListener {

            startActivity(Intent(this, PressureActivity::class.java))
            animateButton(pressure_icon_image)
        }

        // SHOW ABOUT TEXT
        button_about.setOnClickListener {

            val intent = Intent(this, About2Activity::class.java)
            startActivity(intent)

        }


        // ADD NEW MEDIKAMENT
        button_new.setOnClickListener {
            animateButton(imageView6)
            startActivity(Intent(this, NeuesMedikament::class.java))
            overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)

        }

        //SHOW TABELVIEW
        button_liste.setOnClickListener {
            animateButton(imageView4)
            startActivity(Intent(this, MedikamentListe::class.java))
            overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)

        }


        val rotate = RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        )

        val rotasjonVarighet = 13000L

        rotate.duration = rotasjonVarighet
        rotate.repeatMode = Animation.REVERSE
        rotate.repeatCount = Animation.INFINITE

        imageViewmedizin.startAnimation(rotate)

        button_log.setOnClickListener {
            animateButton(imageView3)
            headingOnly = true
            startActivity(Intent(this, LogActivity::class.java))
            overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
        }

        enabledButton()

        doBounce(imageViewConfirm)
        doBounce(imageViewList)
        doBounce(imageViewNew)

     //  hideMenuText()


    }


    private fun fadeOut(img:View) {
        val fadeInAnimation = AnimationUtils.loadAnimation(
                this, R.anim.alpha_to_fast)
        img.startAnimation(fadeInAnimation)
    }
    private fun fadeIn(img:View) {
        val fadeInAnimation = AnimationUtils.loadAnimation(
                this, R.anim.alpha_from_fast)
        img.startAnimation(fadeInAnimation)
    }
    private fun animateButton(img: ImageView) {
        val duration = 1000L
        val alphafrom = 0.33f
        val alphato = 1.0f
        val rotatebutton = ObjectAnimator.ofFloat(img, View.ALPHA, alphafrom, alphato)
        val rotateSet = AnimatorSet()
        rotatebutton.duration = duration
        rotateSet.play(rotatebutton)
        rotateSet.start()
    }

    private fun checkDB(): Boolean {
        Realm.init(this)
        val tablename = TABLENAME_MEDIKA
        val config = RealmConfiguration.Builder()
                .name(tablename).build()

        val realm = Realm.getInstance(config)

        realm.beginTransaction()
        val medikamentBD = realm.where(DBmedizin::class.java).findAll()
        realm.commitTransaction()


        databasesize = medikamentBD.size
        return (medikamentBD.size == 0)
    }

/*    private fun hideMenuText(){



            Handler().postDelayed({



                val fadeInAnimation = AnimationUtils.loadAnimation(
                        this, R.anim.alpha_to)
                view_einnahme.startAnimation(fadeInAnimation)
                val fadeInAnimation0 = AnimationUtils.loadAnimation(
                        this, R.anim.alpha_to)
                view_neu.startAnimation(fadeInAnimation0)
                val fadeInAnimation1 = AnimationUtils.loadAnimation(
                        this, R.anim.alpha_to)
                view_ibruk.startAnimation(fadeInAnimation1)


            }, 500L * 8L)

    }*/
    private fun enabledButton() {

        val dbnotExist = checkDB()

        val im3 = findViewById<ImageView>(R.id.imageView3)
        val im4 = findViewById<ImageView>(R.id.imageView4)

        if (dbnotExist) {

/*
            SnackBar().showInfo(findViewById(android.R.id.content), getString(R.string.main_achtung_toast_01), false)
*/

            imageStartHere.visibility = View.VISIBLE
            im3.imageAlpha = 100
            im4.imageAlpha = 100

        } else {
            imageStartHere.visibility = View.INVISIBLE

            im4.imageAlpha = 255
            im3.imageAlpha = 255
        }
        button_liste.isEnabled = !dbnotExist
        button_log.isEnabled = !dbnotExist


    }

    override fun onResume() {
        super.onResume()
        enabledButton()

    }

    // used for alramactivity
    private fun getScreenSize() {
        screenSize()

    }

    private fun screenSize() {


        val deviceDensity = resources.displayMetrics.density

        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val deviceHeight = outMetrics.heightPixels / deviceDensity
        //  val deviceWidth = outMetrics.widthPixels / deviceDensity


        densityDpi = outMetrics.densityDpi //deviceHeight.toInt()
        if (deviceHeight < 480) {
            useDefaultAlarmActivity = true
        }


        /*  val display = wm.defaultDisplay


          val size = Point()
          display?.getSize(size)

          val metrics = DisplayMetrics()
          wm!!.defaultDisplay?.getMetrics(metrics)
          densityDpi = metrics.densityDpi



          if (densityDpi < 480) {
              useDefaultAlarmActivity = true
          }
  */

    }

    private fun logoutFacebook() {

        val alert = AlertDialog.Builder(this).create()
        alert.setTitle("Log out from Facebook?")
        alert.setMessage("\n" + getString(R.string.main_achtung_toast_02))
        alert.setIcon(R.drawable.medikament_logo_a)
        val ry = getString(R.string.yes_finish)
        val rn = getString(R.string.no_finish)
        val rc = getString(R.string.youclicket)


        alert.setButton(AlertDialog.BUTTON_POSITIVE, ry) { _, _ ->

            SnackBar().showInfo(findViewById(android.R.id.content), "$rc [$ry]", true)

            LoginManager.getInstance().logOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

            pusshMe = true
            finishAndRemoveTask()
            this.finishAffinity()

        }
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, rn) { _, _ ->
            SnackBar().showInfo(findViewById(android.R.id.content), "$rc [$rn]", false)
        }


        alert.show()
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#0000FF"))
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0000"))

    }

    private fun leaveApp() {
        val simpleAlert = AlertDialog.Builder(this).create()
        simpleAlert.setTitle(getString(R.string.save_achtung_toast_01))
        simpleAlert.setMessage("\n" + getString(R.string.main_achtung_toast_02))

        simpleAlert.setIcon(R.drawable.medikament_logo_a)


        val ry = getString(R.string.yes_finish)
        val rn = getString(R.string.no_finish)
        val rc = getString(R.string.youclicket)

        /*ERSTATT dialogInterface*/
        simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, ry) { _, _ ->

            SnackBar().showInfo(findViewById(android.R.id.content), "$rc [$ry]", true)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

            pusshMe = true
            finishAndRemoveTask()
            this.finishAffinity()

        }

        /*ERSTATT dialogInterface*/
        simpleAlert.setButton(AlertDialog.BUTTON_NEGATIVE, rn) { _, _ ->
            SnackBar().showInfo(findViewById(android.R.id.content), "$rc [$rn]", false)
        }


        simpleAlert.show()
        simpleAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#0000FF"))
        simpleAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0000"))

    }

    override fun onBackPressed() {


        leaveApp()

    }

    /*RESTORE FROM SHARED*/
    private fun restoreTrailStatus(): Boolean {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)

        return prefs!!.getBoolean(preftrial, false)
    }


    private fun restoreFromShared() {
        felleBackGroundColorID = getShared()
        if (felleBackGroundColorID < 0) felleBackGroundColorID = 0

        val window = this.getWindow()
        Globals.setBackGroundColor(window,findViewById<ConstraintLayout>(R.id.main_layout))
    }


    //*GET FROM SHARED*//*
    private fun getShared(): Int {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)

        return prefs!!.getInt(prefname, 4)

    }

    private fun showTrailInfo() {

        val alert = AlertDialog.Builder(this).create().apply {
            setTitle(getString(R.string.main_achtung_toast_09))
            setMessage(getString(R.string.main_achtung_toast_08))
            setIcon(R.drawable.warning)
        }
        val ry = getString(R.string.main_achtung_toast_00)

        val rc = getString(R.string.save_achtung_toast_05)

        alert.setCancelable(false)
        alert.setCanceledOnTouchOutside(false)
        alert.setButton(AlertDialog.BUTTON_POSITIVE, ry) { _, _ ->

            SnackBar().showInfo(findViewById(android.R.id.content), "$rc [$ry]", true)

            finishAndRemoveTask()

            this.finishAffinity()
        }



        alert.show()
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#0000FF"))

    }


}

class MyGestureRutines : GestureDetector.SimpleOnGestureListener() {





    override fun onLongPress(e: MotionEvent?) {




        return super.onLongPress(e)
    }

}









