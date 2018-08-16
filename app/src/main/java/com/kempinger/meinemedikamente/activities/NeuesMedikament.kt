package com.kempinger.meinemedikamente.activities


//import android.support.design.widget.Snackbar
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.adapters.gangerAntall
import com.kempinger.meinemedikamente.model.*
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_neues_medikament.*
import kotlinx.android.synthetic.main.activity_neues_medikament.view.*
import java.util.*


// ---

const val moveitems = 800f
var medianSYSArray = arrayListOf<String>()
var medianDIAArray = arrayListOf<String>()
var medikamentArray = arrayListOf<String>()
var mahlzeit = arrayListOf<Int>()
var ablaufArray = arrayListOf<String>()
var oppstartArray = arrayListOf<Date>()
var vmallArray = arrayListOf<String>()
var vmaNArray = arrayListOf<Boolean>()
var alarmsetArray = arrayListOf(false)
var alarmZeitArray = arrayListOf<String>()
var genommenArray = arrayListOf<Int>()
var dosisArray = arrayListOf<String>()
var anzahlArray = arrayListOf<Int>()
var letztesDatumArray = arrayListOf<String>()
var uuidArray = arrayListOf<String>()
var tglArray = arrayListOf<Int>()
var diffArray = arrayListOf<Int>()
var tglAntallArray = arrayListOf<Int>()
val antallTgl = arrayListOf("1 stk Tbl.", "2 stk Tbl.", "3 stk Tbl.", "4 stk Tbl.", "5 stk Tbl.", "6 stk Tbl.", "7 stk Tbl.", "8 stk Tbl.", "9 stk Tbl.", "10 stk Tbl.", "11 stk Tbl.", "12 stk Tbl.")
val taglichArray = arrayListOf<String>()
var medUtvalgArray = arrayListOf<String>()


//var activateHelpIconsCounter1 = 0
var activateHelpIconsCounter2 = 0
const val mduration = 800L
const val erlik = " ￫"

class NeuesMedikament : AppCompatActivity() {


    private var slayout: CoordinatorLayout? = null
    private var flag = false
    private var enhet = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_neues_medikament)


        val window = this.getWindow()
        Globals.setBackGroundColor(window,findViewById<ConstraintLayout>(R.id.const_neu_background))


        dbtgl = 0
        gangerAntall = 1
        dbAntallIPackning = 12


        var more = getSharedToggleImage()


        val sw = findViewById<ImageView>(R.id.more_less)
        switchToSimply(more)


        sw?.setOnClickListener {
            more =!more
            saveSharedToggleImage(more)
            switchToSimply(more)
        }



        medUtvalgArray.clear()

        medUtvalgArray.add(getString(R.string.med_pille))
        medUtvalgArray.add(getString(R.string.med_zapf))
        medUtvalgArray.add(getString(R.string.med_tropf))
        medUtvalgArray.add(getString(R.string.med_kaps))
        medUtvalgArray.add(getString(R.string.med_sprize))
        medUtvalgArray.add(getString(R.string.med_salbe))
        medUtvalgArray.add(getString(R.string.med_apullen))
        medUtvalgArray.add(getString(R.string.med_inhal))
        medUtvalgArray.add(getString(R.string.med_losung))
        medUtvalgArray.add(getString(R.string.med_mix))
        medUtvalgArray.add(getString(R.string.med_emulsion))
        medUtvalgArray.add(getString(R.string.plaster))
        medUtvalgArray.add(getString(R.string.med_sonst))



        imageViewWeckerN.visibility = INVISIBLE


      //  textView2.paintFlags = textView2.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        dbalarm = false

        enhet = " " + getString(R.string.med_einhet)



        enhet = " " + getString(R.string.med_einheter)
        initAntallArray(enhet)

        editQuantity.setText(R.string.set12)
        val adapterTgl = ArrayAdapter(this, R.layout.spinner_layout, antallTgl)

        val adapterMedUtvalg = ArrayAdapter(this, R.layout.spinner_layout, medUtvalgArray)


        editQuantity.setOnEditorActionListener { _, action, _ ->
            activateHelpIcons(4)
            var handled = false


            if (action == EditorInfo.IME_ACTION_DONE) {
                handled = true
                val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)


                if (editQuantity.text.isEmpty()) {
                    dbAntallIPackning = 12
                    editQuantity.setText(R.string.set12)

                } else {
                    if (editQuantity.text.length < 4) {
                        dbAntallIPackning = editQuantity.text.toString().toInt()
                    } else {
                        dbAntallIPackning = 120
                        editQuantity.setText(R.string.set120)
                    }


                }

                checkMatchPiller()
            }
            handled


        }


        fun initadapter() {

            adapterTgl.setDropDownViewResource(R.layout.spinner_layout)

            adapterMedUtvalg.setDropDownViewResource(R.layout.spinner_layout)
        }

        togglesetalarm.setOnClickListener {
            switchOnOff()
        }


        dbablauf = "-"

        togglesetalarm.isEnabled = false


        initadapter()


        val lastp = antallTgl.size-1
        val lastm = medUtvalgArray.size-1
        spinner_stk_tbl.adapter = adapterTgl
        spinner_medikament.adapter = adapterMedUtvalg
        spinner_stk_tbl.isEnabled = false

        spinner_stk_tbl.setSelection(lastp, true)
        spinner_medikament.setSelection(lastm, true)


        medikamentname.setOnEditorActionListener { _, action, _ ->
            activateHelpIcons(1)
            var handled = false


            if (action == EditorInfo.IME_ACTION_DONE) {

                handled = true
                val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)


                flag = true
                togglesetalarm.isEnabled = true
                imageViewWeckerN.alpha = 1f

                if (medikamentname.text.isEmpty()) {
                    flag = false
                    togglesetalarm.isEnabled = false
                    imageViewWeckerN.alpha = 0.3f
                    imageViewWeckerN.visibility = INVISIBLE
                    hideWatch()

                } else {

                    var pstr = filterNoneAscii( medikamentname.text.toString()) //.substring(0,22)
                    if (pstr.length > 24) {
                        pstr = pstr.substring(0, 24)
                    }
                    medikamentname.setText(measurementToLowerCase(pstr))

                }
            }
            handled

        }


        /*TOGGLE Morning Noon ..*/


        toggleAbend.setOnClickListener {
            if (toggleAbend.isChecked) {
                imageViewEve.setImageResource(R.drawable.round_toggle_on_icon)
            } else {
                imageViewEve.setImageResource(R.drawable.round_toggle_off_icon)
            }

        }
        toggleNacht.setOnClickListener {
            if (toggleNacht.isChecked) {
                imageViewNight.setImageResource(R.drawable.round_toggle_on_icon)
            } else {
                imageViewNight.setImageResource(R.drawable.round_toggle_off_icon)
            }

        }
        toggleMittag.setOnClickListener {
            if (toggleMittag.isChecked) {
                imageViewNoon.setImageResource(R.drawable.round_toggle_on_icon)
            } else {
                imageViewNoon.setImageResource(R.drawable.round_toggle_off_icon)
            }
        }
        toggleVormittag.setOnClickListener {
            if (toggleVormittag.isChecked) {
                imageViewMorning.setImageResource(R.drawable.round_toggle_on_icon)
            } else {
                imageViewMorning.setImageResource(R.drawable.round_toggle_off_icon)
            }
        }


        //MEDITYPE UTVALG
        spinner_medikament.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val tmpd = getString(R.string.dosage_pr_day) + "  $erlik  $dbAntallIPackning"
                dosis_id.text = tmpd

                spinner_medikament.getChildAt(0).visibility = View.INVISIBLE

                resetRadioTaglig()

                activateHelpIcons(2)
                activateHelpIcons(3)
                imageViewWeckerN.visibility = View.VISIBLE
                placeWatch()


                val strx = medUtvalgArray.get(index = position)
                spinner_medikament_text.text = strx.toString()
                initAntallArray(strx)

                dbdosis = antallTgl.get(index = 0)
                spinner_stk_tbl.isEnabled = true
                spinner_stk_tbl_text.text = dbdosis
                spinner_stk_tbl.setSelection(0)

                adapterTgl.setNotifyOnChange(true)
                adapterTgl.notifyDataSetChanged()
                adapterMedUtvalg.setNotifyOnChange(true)

            }
        }
        /*SPINNER jeden tag*/
        spinner_stk_tbl.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (activateHelpIconsCounter2 > 0) {
                    activateHelpIcons(3)
                }

                spinner_stk_tbl.getChildAt(0).visibility = INVISIBLE
                activateHelpIconsCounter2 += 1
                dbdosis = antallTgl.get(index = position)
                spinner_stk_tbl_text.text = dbdosis
                gangerAntall = position + 1
                checkMatchPiller()

            }

        }

        /*SPINNER stündlig*/


        /*RADIO BUTTON*/

        val radiobutton = findViewById<RadioGroup>(R.id.radioGroup2)

        radiobutton.setOnCheckedChangeListener { radioGroup2, _ ->
            activateHelpIcons(5)


            if (radioGroup2.radio1x.isChecked) dbtgl = 0
            if (radioGroup2.radio2x.isChecked) dbtgl = 1
            if (radioGroup2.radio3x.isChecked) dbtgl = 2
            if (radioGroup2.radio4x.isChecked) dbtgl = 3
            if (radioGroup2.radio5x.isChecked) dbtgl = 4

            checkMatchPiller()

        }

        val radioMahlzeit = findViewById<RadioGroup>(R.id.radio_group_mahlzeit)
        dbmahlzeit = 3
        radioMahlzeit.setOnCheckedChangeListener { radios, _ ->

            activateHelpIcons(5)
            if (radios.radioButtonN.isChecked) dbmahlzeit = 2
            if (radios.radioButtonZ.isChecked) dbmahlzeit = 1
            if (radios.radioButtonV.isChecked) dbmahlzeit = 0
            if (radios.radioButtonAdLib.isChecked) dbmahlzeit = 3

        }


        /*SAVE */

        buttonsave.setOnClickListener {


            val star = "  "
            var tmp = ""
            if (toggleVormittag.isChecked) {
                tmp =  getString(R.string.fruh)

            }
            if (toggleMittag.isChecked) {
                tmp += star + getString(R.string.mittag)

            }
            if (toggleAbend.isChecked) {
                tmp += star + getString(R.string.abend)

            }
            if (toggleNacht.isChecked) {
                tmp += star + getString(R.string.nacht)

            }


            dbvmlAll = tmp
            save()
        }


        activateHelpIcons(0) // switch on
    }

    private fun filterNoneAscii(text: String) : String {

        val regex = Regex("[^A-Za-z0-9 ]")
        val text2return = regex.replace(text, "")
        return text2return
    }

    private fun resetRadioTaglig() {
        dbtgl = 0
      radio1x.isChecked = true
      radio2x.isChecked = false
      radio3x.isChecked = false
      radio4x.isChecked = false
      radio5x.isChecked = false
    }
    private fun measurementToLowerCase(enhet: String): String {

        if (enhet.length < 5) return enhet //AB 2MG
        val f = enhet.substring(enhet.length - 2)
        if (f.toUpperCase() == "MG" || f.toUpperCase() == "ML") {

            val n = enhet.substring(0, enhet.length - 2) + f.toLowerCase()
            return n
        }
        val u = enhet.substring(enhet.length - 3)
        if (u.toUpperCase() == "MCG") {
            val n = enhet.substring(0, enhet.length - 3) + "µg"
            return n
        }
        return enhet
    }

    private fun animateToggleImageButton(img: ImageView, on: Boolean) {

        if (on) {
            img.visibility = View.VISIBLE
            val fadeInAnimation = AnimationUtils.loadAnimation(
                    this, R.anim.alpha_from)
            img.startAnimation(fadeInAnimation)

        } else {
            //   img.alpha = 1f
            if (img.visibility == 0) {
                val fadeInAnimation = AnimationUtils.loadAnimation(
                        this, R.anim.alpha_to)
                img.startAnimation(fadeInAnimation)
            }
            img.visibility = View.GONE
        }


    }


 //EXTENDED INFO


    //HIDE Extendet Info
    private fun hideInfo(img: View) {
        img.startAnimation(TranslateAnimation(0f, 0f, 0f, moveitems).apply {
            duration = mduration
        })
        img.visibility = View.INVISIBLE
    }


    private fun hideExtendedInformation() {

        hideInfo(radioGroup2)
        hideInfo(radio_group_mahlzeit)
        hideInfo(toggleVormittag_label)
        hideInfo(imageViewMorning)
        hideInfo(imageViewEve)
        hideInfo(toggleAbend_label)
        hideInfo(toggleNacht_label)
        hideInfo(imageViewNight)
        hideInfo(imageViewNoon)
        hideInfo(toggleMittag_label)
        hideInfo(textView2)
        hideInfo(textView3)

        toggleMittag.isEnabled = false
        toggleNacht.isEnabled = false
        toggleAbend.isEnabled = false
        toggleVormittag.isEnabled = false


        help_icon5.alpha = 0f
    }


    private fun placeInfo(img:View){
        img.visibility = View.VISIBLE


        img.startAnimation(TranslateAnimation(0f, 0f, moveitems, 0f).apply {
            duration = mduration

        })

    }

    private fun placeExtendedInfo() {

        placeInfo(radioGroup2)
        placeInfo(radio_group_mahlzeit)
        placeInfo(toggleVormittag_label)
        placeInfo(imageViewMorning)
        placeInfo(imageViewEve)
        placeInfo(toggleAbend_label)
        placeInfo(toggleNacht_label)
        placeInfo(imageViewNight)
        placeInfo(imageViewNoon)
        placeInfo(toggleMittag_label)
        placeInfo(textView2)
        placeInfo(textView3)

        toggleMittag.isEnabled = true
        toggleNacht.isEnabled = true
        toggleAbend.isEnabled = true
        toggleVormittag.isEnabled = true
        help_icon5.alpha = 1f

        toggleVormittag.isChecked = false
        toggleAbend.isChecked = false
        toggleMittag.isChecked = false
        toggleNacht.isChecked = false
        imageViewMorning.setImageResource(R.drawable.round_toggle_off_icon)
        imageViewNoon.setImageResource(R.drawable.round_toggle_off_icon)
        imageViewEve.setImageResource(R.drawable.round_toggle_off_icon)
        imageViewNight.setImageResource(R.drawable.round_toggle_off_icon)

        radio_group_mahlzeit.clearCheck()
        dbmahlzeit = 3
        radioButtonAdLib.isChecked = true
        radioGroup2.clearCheck()
        radio1x.isChecked = true
        dbtgl = 0
    }
    private fun switchToSimply(on: Boolean) {


        val vis = when (on) {
            false -> View.GONE
            true -> View.VISIBLE
        }
        if (on) {
            more_less.setImageResource(R.drawable.ic_expand_more_black_24dp)
            more_less.setColorFilter(Color.WHITE)
        } else {
            more_less.setImageResource(R.drawable.ic_expand_less_black_24dp)
            more_less.setColorFilter(Color.GREEN)
        }

        if (vis == View.GONE) {
            health_image.visibility = View.VISIBLE
            animateToggleImageButton(health_image, true)
           hideExtendedInformation()
        } else {
            animateToggleImageButton(health_image, false)
            health_image.visibility = View.INVISIBLE
            placeExtendedInfo()
        }

    }


    //CHECH ANTALL PILLER MATCH
    private fun checkMatchPiller() {
        textView13blink.setTextColor(Color.WHITE)


        val tmps = getString(R.string.quantity)

        val tm = gangerAntall * (dbtgl + 1)
        val tm2 = dbAntallIPackning % gangerAntall
        val tmpsobs = getString(R.string.quantity) + "  → $tm   ?"
        val tmpsobs2 = getString(R.string.quantity) + "  → $tm % $dbAntallIPackning ≠ 0 !"
        val dosis = (dbAntallIPackning / tm).toInt()



        if (tm > dbAntallIPackning) {
            textView13blink.setTextColor(Color.GREEN)
            textView13blink.setText(tmpsobs)
            blinktext(true)
            dosis_id.setPaintFlags( Paint.STRIKE_THRU_TEXT_FLAG)
            return
        }

        if ( dbAntallIPackning % tm != 0) {
            textView13blink.setTextColor(Color.GREEN)
            textView13blink.setText(tmpsobs2)
            blinktext(true)
            dosis_id.paint.strokeMiter
            dosis_id.setPaintFlags( Paint.STRIKE_THRU_TEXT_FLAG)

            return
        }
        if ((dbAntallIPackning % (gangerAntall)) != 0) {
            textView13blink.setTextColor(Color.GREEN)
            textView13blink.setText(tmpsobs2)
            blinktext(true)
            dosis_id.setPaintFlags( Paint.STRIKE_THRU_TEXT_FLAG)

        } else {

            textView13blink.setText(tmps)
            dosis_id.visibility = View.VISIBLE
            dosis_id.setPaintFlags(Paint.ANTI_ALIAS_FLAG)
            blinktext(false)
        }

        val tmpd = getString(R.string.dosage_pr_day) + " $erlik $dosis"
        dosis_id.text = tmpd

    }

    private fun blinktext(on: Boolean) {

        val anim = AlphaAnimation(0.0f, 1.0f)

        anim.duration = 400 //You can manage the time of the blink with this parameter
        anim.startOffset = 250
        anim.repeatMode = Animation.REVERSE
        if (on) {
            anim.repeatCount = Animation.INFINITE
            textView13blink.startAnimation(anim)

        } else {
            anim.repeatCount = Animation.ABSOLUTE

            textView13blink.startAnimation(anim)
            textView13blink.setTextColor(Color.WHITE)
        }
    }



    private fun initAntallArray(enhet: String) {

        antallTgl.clear()
        val ganger = " *"
        for (m in 1..12) {
            val item = "$m$ganger $enhet"
            antallTgl.add(item)


        }


    }

    override fun onResume() {
        super.onResume()
       // activateHelpIconsCounter1 = 0
        activateHelpIconsCounter2 = 0
    }

    private fun switchOnOff() {

        dbalarm = togglesetalarm.isChecked


        if (togglesetalarm.isChecked) {

            dbmedikament = medikamentname.text.toString()
            imageViewWeckerN.alpha = 1f
            imageViewWeckerN.setImageDrawable(getDrawable(R.drawable.ic_alarm_on_black_24dp))

            val intent = Intent(this, SetAlarmActivity::class.java)

            if (useDefaultAlarmActivity) {

                intent.putExtra(REPEAT_ALARM, dbtgl)
                startActivity(intent)
            } else {
                intent.putExtra(REPEAT_ALARM, dbtgl)
                startActivity(intent)
            }


        } else {
            if (flag) {
                togglesetalarm.isEnabled = true
                imageViewWeckerN.alpha = 1f
            } else {
                togglesetalarm.isEnabled = false
                imageViewWeckerN.alpha = 0.3f
            }
            imageViewWeckerN.setImageDrawable(getDrawable(R.drawable.ic_alarm_add_black_24dp))
        }

    }




    private fun animate(img: ImageView) {
        val alphaAnimation = AlphaAnimation(1f, 0f)
        alphaAnimation.duration = mduration
        img.startAnimation(alphaAnimation)

        img.visibility = INVISIBLE
    }


    private fun activateHelpIcons(done: Int) {


        fun makeVisible() {

            help_icon1.visibility = VISIBLE
            help_icon2.visibility = VISIBLE
            help_icon3.visibility = VISIBLE
            help_icon4.visibility = VISIBLE
            help_icon5.visibility = VISIBLE
        }

        when (done) {
            1 -> animate(help_icon1)
            2 -> animate(help_icon2)
            3 -> animate(help_icon3)
            4 -> animate(help_icon4)
            5 -> animate(help_icon5)
            else -> makeVisible()
        }


    }


    private fun placeWatch() {
        imageViewWeckerN.alpha = 0.3f
        imageViewWeckerN.startAnimation(TranslateAnimation(0f, 0f, (densityDpi / 2).toFloat(), 0f).apply {

            duration = mduration

        })
    }

    private fun saveSharedTrial(ema: Boolean) {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        val editor = prefs!!.edit()
        editor.putBoolean(preftrial, ema)
        editor.apply()


    }
    //SJEKK UKE OVER

    private fun loadTrail(): Boolean {

        Realm.init(this)
        val tablename = TABLENAME_TRIAL
        val config = RealmConfiguration.Builder()
                .name(tablename).build()

        val realm = Realm.getInstance(config)



        realm.beginTransaction()
        val result = realm.where(DBTrial::class.java).findFirst()

        val dato = result?.dbStartDatum

        realm.commitTransaction()


        if (dato != null) {
            trialStartet = true
            val tm = dato
            val td = (tm.time)
            val dt1 = DateConverter().convertLongToTime(td)

            val a = dt1.split(".", " ")
            val ay = a.get(index = 0).toInt()
            val am = a.get(index = 1).toInt()
            val ad = a.get(index = 2).toInt()

            val tdx = DateConverter().dateDiff(ay, am, ad)
            if (tdx < 0) { // dato satt tilbake

                Toast.makeText(this, R.string.wrongdate, Toast.LENGTH_LONG).show()
                trialSlutt = true

                saveSharedTrial(trialSlutt)
                return true

            }
            if (tdx >= 0 && tdx <= 7) {
                val tmp = "${7 - tdx} " + getString(R.string.daysleft)
                Toast.makeText(this, tmp, Toast.LENGTH_LONG).show()
            }
            if (tdx > 7) {
                val tmp = getString(R.string.main_achtung_toast_09)
                Toast.makeText(this, tmp, Toast.LENGTH_LONG).show()
                trialSlutt = true

                saveSharedTrial(trialSlutt)
                return true

            }
        }
        return trialStartet
    }

    // SAVE TRIAL

    private fun saveTrail() {


        if (!loadTrail()) {
            Realm.init(this)
            val tablename = TABLENAME_TRIAL
            val config = RealmConfiguration.Builder()
                    .name(tablename).build()

            val realm = Realm.getInstance(config)

            realm.beginTransaction()

            val uuid = UUID.randomUUID().toString()
            dbuuid = uuid
            val trialDB = realm.createObject(DBTrial::class.java)

            trialDB.dbStartDatum = Date()
            trialDB.dbDate = GetDateForDB.getDateTime().toString()

            realm.commitTransaction()
        }
    }



    private fun hideWatch() {
        imageViewWeckerN.startAnimation(TranslateAnimation(0f, 0f, 0f, 600f).apply {

            duration = mduration

        })
    }


    private fun save() {


        if (trialVersion) {
            saveTrail()

            if (trialSlutt) {
                Toast.makeText(this, "Sorry, but this Trial has ended!", Toast.LENGTH_LONG).show()
                finish()
            }

        }
        dbmedikament = medikamentname.text.toString()
        dbdaggenommen = false
        dbanzahlgenommen = 0

        if (dbdosis.isEmpty()) {
            dbdosis = antallTgl[0]
        }
        if (dbmedikament.length > 2) {
            var um = "$uhrzeitMinutte"
            if (uhrzeitMinutte < 10) {
                um = "0$uhrzeitMinutte"

            }

            dbalarmzeit = "$uhrzeitStunde:$um"
            buttonsave.isEnabled = false
            buttonsave.setTextColor(Color.GRAY)
            Realm.init(this)
            val tablename = TABLENAME_MEDIKA
            val config = RealmConfiguration.Builder()
                    .name(tablename).build()

            val realm = Realm.getInstance(config)


            /*BEGIN*/
            realm.beginTransaction()

            dbAntallIPackning = 12


            if (editQuantity.text.isEmpty()) {
                dbAntallIPackning = 0

            } else {

                dbAntallIPackning = editQuantity.text.toString().toInt()


            }

            val uuid = UUID.randomUUID().toString()
            dbuuid = uuid
            val medizinDB = realm.createObject(DBmedizin::class.java, uuid)




            medizinDB.dbMedikament = dbmedikament//.toUpperCase(locale!!)
            medizinDB.dbAblauf = dbablauf
            medizinDB.dbAlarm = dbalarm
            medizinDB.dbAlarmZeit = dbalarmzeit
            medizinDB.dbDagGenommen = dbdaggenommen
            medizinDB.dbAnzahlGenommen = dbanzahlgenommen
            medizinDB.dbMahlzeit = dbmahlzeit
            medizinDB.dbTaglich = dbtgl // x jede stunde
            medizinDB.dbAnzahlMedikament = gangerAntall
            medizinDB.dbDosisEnhet = dbdosis //+ " " + dbutvalg
            medizinDB.dbVMAN = dbvmlAll
            medizinDB.dbN = toggleNacht.isChecked
            medizinDB.dbStartDatum = Date()
            medizinDB.dbDate = GetDateForDB.getDateTime().toString()
            medizinDB.dbAnzahl = dbAntallIPackning

            medizinDB.dbLetztesDatum = dbletztesdatum

            /*COMMIT*/
            realm.commitTransaction()

            finish()

        } else {

            val v: View = findViewById(android.R.id.content)
            SnackBar().showInfo(v, getString(R.string.save_toast).toString(), false)

        }


    }

    private fun saveBeforClose() {

        val v: View = findViewById(android.R.id.content)
        val alert = AlertDialog.Builder(this).create().apply {
            setTitle(getString(R.string.save_achtung_toast_01))
            setMessage(getString(R.string.save_achtung_toast_02))
            setIcon(R.drawable.warning)
        }
        val ry = getString(R.string.save_achtung_toast_03)
        val rn = getString(R.string.save_achtung_toast_04)
        val rc = getString(R.string.save_achtung_toast_05)
        alert.setButton(AlertDialog.BUTTON_POSITIVE, ry) { _, _ ->


            SnackBar().showInfo(v, "$rc [$ry]", true)
            save()
        }

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, rn) { _, _ ->


            SnackBar().showInfo(v, "$rc [$rn]", !showGreenChar)
            finish()
        }
        alert.show()
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#0000FF"))
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0000"))

    }

    override fun onBackPressed() {
        //  super.onBackPressed()
        if (flag) {
            saveBeforClose()
        } else {
            finish()
        }

    }

    private fun getSharedToggleImage(): Boolean {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        return prefs!!.getBoolean(prefMore, true)

    }

    private fun saveSharedToggleImage(sw: Boolean) {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        val editor = prefs!!.edit()
        editor.putBoolean(prefMore, sw)
        editor.apply()


    }


}
