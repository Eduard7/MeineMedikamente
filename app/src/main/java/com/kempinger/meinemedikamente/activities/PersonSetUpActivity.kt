package com.kempinger.meinemedikamente.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.model.Globals
import com.kempinger.meinemedikamente.model.Person
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_person_set_up.*

const val prefemail = "UserEmail"
const val prefpswrd = "UserPassword"
const val prefusers = "Usernames"
const val prefuserAB = "UserAB"

const val prefpro = "Fylltsystem"

var memailadresse=""
class PersonSetUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_set_up)

        setBackGroundColor()

        personAInUse = getUserAB()
        toggleButtonUser.isChecked = personAInUse



        val tmp2 =  getString(R.string.indicator)
        textindicator.text = tmp2

        fun refreshDisplay() {
            emailadresse.setText(getSharedEmail())
            editTextage.setText(person_age.toString())


            if (!person_isMan) {
                textViewsex.text = getString(R.string.sextypeW)

            } else {
                textViewsex.text = getString(R.string.sextypeM)


            }
            if (!person_isMan) {

                gender_switch.setImageResource(R.drawable.gender_man_off)
            } else {

                gender_switch.setImageResource(R.drawable.gender_man_on)

            }
            toggleButtonUser.isChecked = personAInUse
            if (personAInUse){
                userswitch.setImageResource(R.drawable.user_a_on)
            } else {
                userswitch.setImageResource(R.drawable.user_a_off)
            }

        }
        load()

        //USER A-> B
        toggleButtonUser.setOnClickListener {

            personAInUse = toggleButtonUser.isChecked
            if (personAInUse){
                userswitch.setImageResource(R.drawable.user_a_on)
            } else {
                userswitch.setImageResource(R.drawable.user_a_off)
            }
            saveUserAB(personAInUse)
            load()
            refreshDisplay()
          //  emailadresse.setText(getSharedEmail())
          //  editTextage.setText(person_age.toString())



        }


        getSharedEmail()
        refreshDisplay()


        //GENDER MAN WOMAN
        toggleButtonsex.setOnClickListener {
            person_isMan = toggleButtonsex.isChecked
            textViewsex.text = if (!person_isMan) {
                getString(R.string.sextypeW)

            } else {
                getString(R.string.sextypeM)

            }



            if (!person_isMan) {

                gender_switch.setImageResource(R.drawable.gender_man_off)
            } else {

                gender_switch.setImageResource(R.drawable.gender_man_on)

            }
            savePersonData()

           // refreshDisplay()
        }
        displayTabelAgeRelatert()

        emailadresse.setOnEditorActionListener { _, actionId, _ ->



            var handled = false


            if (actionId == EditorInfo.IME_ACTION_DONE) {

                handled = true
                val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)

                if (emailadresse.text.isNotEmpty() ) {

                    saveSharedEmail(emailadresse.text.toString())
                    memailadresse = emailadresse.text.toString()

                } else {
                emailadresse.setText("")
                }
            }

            handled


        }

        editTextage.setOnEditorActionListener { _, actionId, _ ->


            var handled = false

            if (!editTextage.text.isEmpty()) {
                if (editTextage.text.toString().toInt() < 20) editTextage.setText(R.string.set20)
                if (editTextage.text.toString().toInt() > 89) editTextage.setText(R.string.set80)
            } else {
                editTextage.setText(R.string.set60)
            }


            if (actionId == EditorInfo.IME_ACTION_DONE) {


                handled = true
                val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)

            }


            handled


        }



        fun doBounce() {
            val bounceImg = AnimationUtils.loadAnimation(this,R.anim.bounce_image_sharedinterpolator)
            imageViewAparat.startAnimation(bounceImg)

        }
        doBounce()

    }

    private fun deleteOldPersonData() {

        Realm.init(this)

        var uuid = "A"
        if (!personAInUse) {
            uuid = "B"
        }
        val tablename = TABLENAME_PERSON
        val config = RealmConfiguration.Builder()
                .name(tablename).build()

        val realm = Realm.getInstance(config)

        val log = realm.where(Person::class.java).equalTo("dbID", uuid).findAll()

        realm.beginTransaction()

        log.deleteAllFromRealm()

        realm.commitTransaction()

    }

    private fun savePersonData() {

        deleteOldPersonData()
        Realm.init(this)

        val tablename = TABLENAME_PERSON
        val config = RealmConfiguration.Builder()
                .name(tablename).build()

        val realm = Realm.getInstance(config)

        realm.beginTransaction()

        var uuid = "A"
        if (!personAInUse) {
            uuid = "B"
        }
        val logDB = realm.createObject(Person::class.java,uuid)

        if (!editTextage.text.isEmpty()) {
            logDB.dbAge = editTextage.text.toString().toInt()
        } else {
            logDB.dbAge = 40
        }


        logDB.dbSex = person_isMan

        realm.commitTransaction()



    }

    private fun leaveActitvity(){
        intent = Intent(this, PressureActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

    }

    private fun displayTabelAgeRelatert() {

        /*var blodtrykk_mann_dia = arrayListOf(72,77,83,86,87,86,84)
var blodtrykk_mann_sys = arrayListOf(135,130,135,140,149,154,156)
var blodtrykk_kvinne_dia = arrayListOf(70,73,78,82,84,85,86)
var blodtrykk_kvinne_sys = arrayListOf(120,120,128,138,149,160,165)*/


        var agestr = ""
        var womstr = ""
        var manstr = ""
        var fra = 20

        val strAgesFrom = getString(R.string.from)
        val strToAges = getString(R.string.to)
        for (n in 0..6) {
            agestr += "$strAgesFrom $fra $strToAges ${fra+9}\n"
            fra += 10
            womstr += "${blodtrykk_kvinne_sys[n]} / ${blodtrykk_kvinne_dia[n]}\n"
            manstr += "${blodtrykk_mann_sys[n]} / ${blodtrykk_mann_dia[n]}\n"

        }
        agefromto.text = agestr
        agewoman.text = womstr
        ageman.text = manstr


    }
    private fun saveSharedEmail(ema: String) {

        val sharedFileName  = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        val editor = prefs!!.edit()
        editor.putString(prefemail, ema)
        editor.apply()


    }
    private fun getSharedEmail(): String {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        return prefs!!.getString(prefemail, "")

    }

    private fun saveUserAB(ema: Boolean) {

        val sharedFileName  = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        val editor = prefs!!.edit()
        editor.putBoolean(prefuserAB, ema)
        editor.apply()


    }
    private fun getUserAB(): Boolean {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        return prefs!!.getBoolean(prefuserAB, true)

    }
    private fun load() {
        Realm.init(this)

        var uuid = "A"
        if (!personAInUse) {
            uuid = "B"
        }

        val tablename = "person.realm"
        val config = RealmConfiguration.Builder()
                .name(tablename).build()

        val realm = Realm.getInstance(config)


        val log = realm.where(Person::class.java).equalTo("dbID",uuid).findAll()

        realm.beginTransaction()
        log.forEach {
            person_age = it.dbAge
            person_isMan = it.dbSex
        }



        realm.commitTransaction()

    }

    override fun onBackPressed() {


        savePersonData()
        saveUserAB(personAInUse)
        leaveActitvity()

    }

    private fun setBackGroundColor() {
        val window = this.getWindow()
        Globals.setBackGroundColor(window,findViewById<ConstraintLayout>(R.id.person_background))
    }


}
