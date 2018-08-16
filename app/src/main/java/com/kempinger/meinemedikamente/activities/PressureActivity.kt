package com.kempinger.meinemedikamente.activities


import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.TextView
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.adapters.PressureAdapter
import com.kempinger.meinemedikamente.adapters.SendToEmail
import com.kempinger.meinemedikamente.model.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_pressurez.*
import java.util.*
import kotlin.collections.ArrayList


var dbsystolic = arrayListOf<String>()
var dbdiatolic = arrayListOf<String>()
var dbpuls = arrayListOf<String>()
var dbdato = arrayListOf<String>()
var dbKlokka = arrayListOf<Int>()
var dagensDato: String = ""
var measurementTime: String = ""
var measurementTimeInt = 0
var sortDatoAar: Int = 0
var sortDatoMnd: Int = 0
var sortDatoDag: Int = 0
var medianDIA = 0
var medianSYS = 0
val ccmail = ""

var blodtrykk_mann_dia = arrayListOf(72, 77, 83, 86, 87, 86, 84)
var blodtrykk_mann_sys = arrayListOf(135, 130, 135, 140, 149, 154, 156)

var blodtrykk_kvinne_dia = arrayListOf(70, 73, 78, 82, 84, 85, 86)
var blodtrykk_kvinne_sys = arrayListOf(120, 120, 128, 138, 149, 160, 165)
var blodtrykk_checkbox = arrayListOf<Boolean>()
var blodtrykk_id = arrayListOf<String>()
var blodtrykk_position = arrayListOf<Int>()

var person_isMan = true
var person_age = 40
var person_A = "A"
var person_B = "B"
var personAInUse = true

val clockSymbol = "Alarms:  "
var fromDate = ""
var toDate = ""
var pressureTimeArray = arrayListOf<String>()

var pressureColor = arrayListOf ("#DDDDDD","#ffff00","#F74248")
class PressureActivity : AppCompatActivity() {





     lateinit var graphIntent : Intent
    val context = this
    /*OBSERVABLE EXAMPLE */


    private var syss: Int = 0
    private var dia: Int = 0
    private var pls: Int = 0


    val modes = ArrayList<Int>() // tendencys

    private fun addTimeLabels() {

        pressureTimeArray.clear()
        pressureTimeArray.add(getString(R.string.fruh))
        pressureTimeArray.add(getString(R.string.mittag))
        pressureTimeArray.add(getString(R.string.abend))
        pressureTimeArray.add(getString(R.string.nacht))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pressurez)
        overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)






        setBackGroundColor()
        restoreFromShared()
        addTimeLabels()

        val adapterPressureTime = ArrayAdapter(this, R.layout.spinner_layout, pressureTimeArray)

        pressure_spinner.adapter = adapterPressureTime
        adapterPressureTime.setDropDownViewResource(R.layout.spinner_layout)


        textView8.setSingleLine()
        textView8.isSelected = true

        // LOAD PERSON AND DATA
        loadPerson()

        if (!personAInUse) {

            user_ab_icon.setImageResource(R.drawable.user_b_icon)
        } else {

            user_ab_icon.setImageResource(R.drawable.user_a_icon)
        }

          load()



        for (n in 0..1500) {
            blodtrykk_position.add(-1)
        }

        button_graph.isEnabled = false
        imagePopup.isEnabled = false
        imagePopup.alpha = 0.25f

        button_graph.alpha = 0.5f




        if (dbsystolic.size > 1) {
            button_graph.isEnabled = true
            button_graph.alpha = 1f
            imagePopup.isEnabled = true
            imagePopup.alpha = 1.0f



        }

        getAverage()
        memailadresse = getSharedEmail()


        pressure_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    measurementTime = pressureTimeArray.get(index = position)
                    measurementTimeInt = position
                } else {
                    measurementTime = pressureTimeArray.get(index = 0)
                    measurementTimeInt = 0
                }

            }

        }




        //EMAIL



        val textviewmax = findViewById<TextView>(R.id.textViewMax)
        val textviewmin = findViewById<TextView>(R.id.textViewMin)

        //SHOW MINMUM LABEL
        textviewmin.setOnClickListener {


            loadMaxMin(false)


        }
        //SHOW MAXIMUM LABEL
        textviewmax.setOnClickListener {

            loadMaxMin(true)

        }

        //SHOW GRAPH

        button_graph.setOnClickListener {
          load()




             graphIntent = Intent(this, LineChartActivity::class.java)



            graphIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP


            startActivity(graphIntent)
            overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
        }

        //CHOOSE GENDER
        button_person.setOnClickListener {
            intent = Intent(this, PersonSetUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
        }




        recycler_view_pressures.layoutManager = LinearLayoutManager(this)
        recycler_view_pressures.adapter = PressureAdapter()


        val idag = GetTodaysDate.getDateTime()!!

        dagensDato = idag
        measurementTime = "-"
        pressure_datum.text = dagensDato



        pulse_text.setOnEditorActionListener { _, _, _ ->
            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
        }

        //SAVE

        button_save.setOnClickListener {
            save()
        }



        fun returnDayOfWeek(dayNum: Int): String {
            when (dayNum) {
                1 -> {
                    return (getString(R.string.son2))
                }

                2 -> {
                    return ((getString(R.string.man2)))
                }
                3 -> {
                    return ((getString(R.string.die2)))
                }
                4 -> {
                    return ((getString(R.string.mit2)))
                }
                5 -> {
                    return ((getString(R.string.don2)))

                }
                6 -> {
                    return ((getString(R.string.fre2)))
                }
                7 -> {
                    return ((getString(R.string.sam2)))
                }
                else ->
                    return ""


            }

        }


        fun assingTodaysDate() {
            val kalender = Calendar.getInstance(locale)


            sortDatoAar = kalender.get(Calendar.YEAR)
            sortDatoMnd = kalender.get(Calendar.MONTH) + 1
            sortDatoDag = kalender.get(Calendar.DAY_OF_MONTH)


        }

        assingTodaysDate()


        button_calendar.setOnClickListener {

            val cal = Calendar.getInstance()
            //  var dagstr = cal.get(Calendar.DAY_OF_WEEK)

            var mnd = cal.get(Calendar.MONTH)
            var aar = cal.get(Calendar.YEAR)
            var dag = cal.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, thisYear, thisMonth, thisDay ->

                // val formatter = SimpleDateFormat("EEE yy.MM.dd  H:mm", locale)


                mnd = thisMonth + 1
                dag = thisDay
                aar = thisYear

                val newDate: Calendar = Calendar.getInstance()
                newDate.set(thisYear, thisMonth, thisDay)
                val dagstr = newDate.get(Calendar.DAY_OF_WEEK)

                val dagnavn = returnDayOfWeek(dagstr)

                var dstr = dag.toString()
                if (dag < 10) dstr = "0$dag"
                var mstr = mnd.toString()
                if (mnd < 10) mstr = "0$mnd"

                val dtd = "$dagnavn $dstr.$mstr.$aar"

                sortDatoAar = aar
                sortDatoDag = dag
                sortDatoMnd = mnd
                dagensDato = dtd
                pressure_datum.text = dagensDato
            }, aar, mnd, dag)
            dpd.show()



        }

        //POPUP MENU

        imagePopup.setOnClickListener {
            val popupmenu = PopupMenu(this,imagePopup)
            popupmenu.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.menu_delete -> {
                        if (dbsystolic.size > 0) {
                            deleteMeassurements()
                        }

                        true
                    }
                    R.id.menu_send -> {
                        if (dbsystolic.size > 0) {
                            sendEmail()
                        }

                        true
                    }
                    R.id.menu_status -> {
                        showResultsSoFar()
                        true
                    }
                    else ->

                        false

                }
            }
            popupmenu.inflate(R.menu.menu_main)

            try {
                val fieldpopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldpopup.isAccessible = true
                val mPopup =fieldpopup[popupmenu]
                mPopup.javaClass
                        .getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                        .invoke(mPopup,true)
            } catch (e: Exception) {
                Log.e("PressureActivity","Error open popup menu!" ,e)
            } finally {
                popupmenu.show()
            }


        }
    }

    private fun sendEmail() {

        val initSendEmail = SendToEmail(this)
        val subjectstr = getString(R.string.bodyheading).toString()
        val intent = Intent(Intent.ACTION_SEND)
        val textToSend = initSendEmail.makeready()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(memailadresse))
        intent.putExtra(Intent.EXTRA_CC, arrayOf(ccmail))
        intent.putExtra(Intent.EXTRA_BCC, arrayOf(myemail))
        intent.putExtra(Intent.EXTRA_SUBJECT, subjectstr)
        intent.putExtra(Intent.EXTRA_TEXT, textToSend)

        startActivity(intent)
    }
    private fun deleteMeassurements(){
        var slettStr = ""

        val v: View = findViewById(android.R.id.content)
        for (n in 0..dbdato.size - 1) {
            if (blodtrykk_checkbox[n] == true) {
                slettStr += dbdato[n] + "\n"
            }


        }
        if (!slettStr.isEmpty()) {


            val alert = AlertDialog.Builder(this).create()
            alert.setTitle(getString(R.string.delete_pressuer))

            alert.setMessage(slettStr)
            alert.setIcon(R.drawable.warning)
            val ry = getString(R.string.einnahme_achtung_toast_03)
            val rn = getString(R.string.einnahme_achtung_toast_04)
            val rc = getString(R.string.save_achtung_toast_05)
            alert.setButton(AlertDialog.BUTTON_POSITIVE, ry) { _, _ ->
                deletePressuredata()
            }

            alert.setButton(AlertDialog.BUTTON_NEGATIVE, rn) { _, _ ->

                SnackBar().showInfo(v, "$rc [$rn]", false)
            }
            alert.show()
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#0000FF"))
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF0000"))

        } else {

            SnackBar().showInfo(v, getString(R.string.noselection), false)
        }

    }
    private fun hideKeyboard(view: View) {
        view?.apply {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    private fun deletePressuredata() {

        var markering = false
        for (n in 0..dbdato.size - 1) {
            if (blodtrykk_checkbox[n] == true) {
                markering = true
                val uuid = blodtrykk_id[n]
                Realm.init(this)
                val tablename = TABLENAME_PRESSURE
                val config = RealmConfiguration.Builder().name(tablename).build()

                val realm = Realm.getInstance(config)

                val result = realm.where(DBBlodPressure::class.java).equalTo("dbID", uuid).findAll()

                realm.beginTransaction()
                result.deleteAllFromRealm()
                realm.commitTransaction()
            }
        }
        if (markering) {
            blodtrykk_position.clear()
            for (n in 0..1500) {
                blodtrykk_position.add(-1)
            }
            val intent = Intent(this, PressureActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun inittextViewsWihtZero() {

        val zerostr = "0"
        average_dia.text = zerostr
        average_sys.text = zerostr
        average_puls.text = zerostr

        max_dia.text = zerostr
        max_sys.text = zerostr
        max_puls.text = zerostr

        min_dia.text = zerostr
        min_sys.text = zerostr
        min_puls.text = zerostr
    }



    private fun showResultsSoFar(){
        val intent = Intent(this,PressureResults::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        startActivity(intent)

    }
    private fun getAverage() {

        inittextViewsWihtZero()
        val dbsize = dbdato.size
        val sum1 = dbpuls.sumBy { it.toInt() }
        val sum2 = dbdiatolic.sumBy { it.toInt() }
        val sum3 = dbsystolic.sumBy { it.toInt() }

        if (dbsize > 0) {
            val snittDia = (sum2 / dbsize)
            val snittSys = (sum3 / dbsize)
            average_dia.text = snittDia.toString()
            average_sys.text = snittSys.toString()
            average_puls.text = (sum1 / dbsize).toString()

            max_dia.text = dbdiatolic.max().toString()
            max_sys.text = dbsystolic.max().toString()
            max_puls.text = dbpuls.max().toString()

            min_dia.text = dbdiatolic.min().toString()
            min_sys.text = dbsystolic.min().toString()
            min_puls.text = dbpuls.min().toString()


        }

    }

    private fun loadMaxMin(maxValue: Boolean) {

        Realm.init(this)


        var uuid : String
        if (personAInUse) {
            uuid = person_A
        } else {
            uuid = person_B
        }

        val v: View = findViewById(android.R.id.content)
        val tablename = TABLENAME_PRESSURE
        val config = RealmConfiguration.Builder()
                .name(tablename).build()
        val realm = Realm.getInstance(config)

        realm.beginTransaction()
        var result = realm.where(DBBlodPressure::class.java).equalTo("dbUser", uuid).findAll()

        result = result.sort("dbSys")


        realm.commitTransaction()
        var dt = ""
        var dk = ""
        var measurement = -1
        if (maxValue) {
            result.forEach {

                if (it.dbSys.toInt() > measurement) {
                    measurement = it.dbSys.toInt()
                    dt = it.dbDato
                    dk = pressureTimeArray[it.dbKlokka]
                }
            }
        } else {
            measurement = 500
            result.forEach {

                if (it.dbSys.toInt() < measurement) {
                    measurement = it.dbSys.toInt()
                    dt = it.dbDato
                    dk = pressureTimeArray[it.dbKlokka]
                }
            }

        }
        val dtm = getString(R.string.datum)
        val dkm = getString(R.string.uhrzeit)
        val tmpstr = "$dtm: $dt \n$dkm: $dk  SYS. $measurement"

        SnackBar().showInfo(v, tmpstr, true)

    }

    private fun load(): Boolean {
        dbdiatolic.clear()
        dbsystolic.clear()
        dbpuls.clear()
        dbdato.clear()
        blodtrykk_id.clear()
        blodtrykk_checkbox.clear()
        blodtrykk_position.clear()
        dbKlokka.clear()
        medianDIAArray.clear()
        medianSYSArray.clear()

        Realm.init(this)

        var uuid : String
        if (personAInUse) {
            uuid = person_A
        } else {
            uuid = person_B
        }
        val tablename = TABLENAME_PRESSURE
        val config = RealmConfiguration.Builder()
                .name(tablename).build()
        val realm = Realm.getInstance(config)

        realm.beginTransaction()
        var result = realm.where(DBBlodPressure::class.java).equalTo("dbUser", uuid).findAll()
        result = result.sort("dbSortDatoDag", Sort.ASCENDING)
        result = result.sort("dbSortDatoMnd", Sort.ASCENDING)
        result = result.sort("dbSortDatoAar", Sort.ASCENDING)


        result.forEach {
            dbdiatolic.add(it.dbDia)
            dbsystolic.add(it.dbSys)
            dbpuls.add(it.dbPuls)
            dbdato.add(it.dbDato)
            dbKlokka.add(it.dbKlokka)
            blodtrykk_id.add(it.dbID)
            medianSYSArray.add(it.dbSys)
            medianDIAArray.add(it.dbDia)
            blodtrykk_checkbox.add(false)

        }


        realm.commitTransaction()

        if (medianSYSArray.size > 0) {
            medianDIAArray.sorted()
            medianSYSArray.sorted()
            val mitten = (medianDIAArray.size / 2)
            if (mitten > 0) {
                medianSYS = medianSYSArray[mitten].toInt()
                medianDIA = medianDIAArray[mitten].toInt()
            } else {
                medianSYS = medianSYSArray[0].toInt()
                medianDIA = medianDIAArray[0].toInt()
            }
            if (dbdato.size > 0) {
                fromDate = dbdato.first()
                toDate = dbdato.last()
            }

            return true
        }

        return false
    }

    private fun save() {

        var saveInput = true
        var displayLabel = ""
        val v: View = findViewById(android.R.id.content)
        if (!pulse_text.text.isEmpty()) {
            if (pulse_text.text.length < 4) {
                pls = pulse_text.text.toString().toInt()
            } else {
                pls = 0
                pulse_text.setText("0")
            }

        }
        if (!sys_text.text.isEmpty()) {
            if (sys_text.text.length < 4) {
                syss = sys_text.text.toString().toInt()
            } else {
                syss = 0
                sys_text.setText("0")
            }
            if (sys_text.text.toString().toInt() >250 ){
                syss = 0
                sys_text.setText("0")
            }

        }
        if (!dia_text.text.isEmpty()) {
            if (dia_text.text.length < 4) {
                dia = dia_text.text.toString().toInt()
            } else {
                dia = 0
                dia_text.setText("0")
            }
            if (dia_text.text.toString().toInt() > 150){
                dia = 0
                dia_text.setText("0")
            }

        }
        if (pls == 0 || dia == 0 || syss == 0) {
            saveInput = false
            if (syss == 0) {
                displayLabel = "SYS. = 0\n"
            }
            if (dia == 0) {
                displayLabel += "DIA. = 0\n"
            }
            if (pls == 0) {
                displayLabel += "PULS = 0"
            }
        }
        if (pls > 300) {
            pls = 60
            pulse_text.setText(R.string.set60).toString()
        }
        if (pls <40) {
            pls = 60
        }
        if (dia > 150) {
            dia = 80
            dia_text.setText(R.string.set60).toString()
        }
        if (syss > 250) {
            syss = 140
            sys_text.setText(R.string.set120).toString()
        }


        if (saveInput) {
            Realm.init(this)


            val tablename = TABLENAME_PRESSURE
            val config = RealmConfiguration.Builder()
                    .name(tablename).build()

            val realm = Realm.getInstance(config)


            realm.beginTransaction()

            var uuidPerson : String
            if (personAInUse) {
                uuidPerson = person_A
            } else {
                uuidPerson = person_B
            }

            val uuid = UUID.randomUUID().toString()
            val pressurelog = realm.createObject(DBBlodPressure::class.java, uuid)


            pressurelog.dbUser = uuidPerson
            pressurelog.dbDato = dagensDato
            pressurelog.dbSortDatoAar = sortDatoAar
            pressurelog.dbSortDatoMnd = sortDatoMnd
            pressurelog.dbSortDatoDag = sortDatoDag
            pressurelog.dbDia = dia.toString()
            pressurelog.dbSys = syss.toString()
            pressurelog.dbPuls = pls.toString()
            pressurelog.dbKlokka = measurementTimeInt

            realm.commitTransaction()

            val intent = Intent(this, PressureActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else {

            SnackBar().showInfo(v, displayLabel, false)
        }

        /*BEGIN*/


    }




    private fun loadPerson() {
        Realm.init(this)

        var uuid = "A"
        if (!personAInUse) {
            uuid = "B"
        }

        val tablename = "person.realm"
        val config = RealmConfiguration.Builder()
                .name(tablename).build()

        val realm = Realm.getInstance(config)


        val log = realm.where(Person::class.java).equalTo("dbID", uuid).findAll()

        realm.beginTransaction()
        log.forEach {
            person_age = it.dbAge
            person_isMan = it.dbSex
        }



        realm.commitTransaction()

    }

    //RESTORE FROM SHARED

    private fun restoreFromShared() {

            //    imageView7.setImageResource(R.drawable.menu_background_blue)
            button_graph.background = getDrawable(R.drawable.graph_icon_blue)
            button_calendar.background = getDrawable(R.drawable.calendar_icons_blue)
            imageView11.setImageResource(R.drawable.hvit_ramme)
           // imageView2.setImageResource(R.drawable.ramme_icon_blue)

            // cons_layout.background = getDrawable(R.color.blu_color_bck)

            if (!person_isMan) {
                imageViewPersonIcon.setImageResource(R.drawable.person_icon_woman_blue)
            } else {
                imageViewPersonIcon.setImageResource(R.drawable.person_icon_man_blue)
            }


    }

    private fun setBackGroundColor() {
        val window = this.getWindow()
        Globals.setBackGroundColor(window,findViewById<ConstraintLayout>(R.id.cons_layout))
    }

    private fun getSharedEmail(): String {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        return prefs!!.getString(prefemail, "")

    }

    override fun onResume() {
        super.onResume()
        restoreFromShared()
    }

}


