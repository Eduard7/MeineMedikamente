package com.kempinger.meinemedikamente.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.model.SnackBar
import kotlinx.android.synthetic.main.activity_line_chart.*
import java.text.NumberFormat
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set


class LineChartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart)


        var modesX = arrayListOf<Int>()
        var modesA = arrayListOf<Int>()




        dbsystolic.forEach {
            modesX.add(it.toInt())

        }
        val a = themode(modesX)



        dbdiatolic.forEach {
            modesA.add(it.toInt())
        }
        val b = themode(modesA)





        val graphs = findViewById<GraphView>(R.id.graph)
        fromDate = dbdato.first()
        toDate = dbdato.last()

        val tmpstr = "$fromDate  " + getString(R.string.to) + " $toDate"
        textViewdato.text = tmpstr
        val nf = NumberFormat.getInstance()
        nf.minimumFractionDigits = 3
        nf.minimumIntegerDigits = 2

        val tmplabel = getString(R.string.median) + " " +  medianDIA.toString()
        val tmplabel2 = getString(R.string.median) + " " +  medianSYS.toString()
        medianlabel_dia!!.text = tmplabel
        medianlabel_sys!!.text = tmplabel2

        val tmpS = getString(R.string.syst) + ": $a"
        val tmpD = getString(R.string.diat) + ": $b"


        if (a.isNotEmpty()) {
            modes_sys.text = tmpS
        } else {
            val tmp = getString(R.string.syst) + punktPunkt
            modes_sys.text = tmp
        }
        if (b.isNotEmpty()) {
            modes_dia.text = tmpD
        } else {
            val tmp = getString(R.string.diat) + punktPunkt
            modes_dia.text = tmp
        }

        findLowestHighest()

        val seriesSys = LineGraphSeries<DataPoint>()
        val seriesDia = LineGraphSeries<DataPoint>()
        val seriesPls = LineGraphSeries<DataPoint>()



        //PULS
        val maxXdataPoints = dbsystolic.size.toDouble()
        val maxdatapointsSys = dbsystolic.max()?.toInt()

        for (i in 0 until dbsystolic.size)
        {

            val x = i.toDouble()+1
            val y = dbpuls[i].toDouble()
            seriesPls.appendData(DataPoint(x,y), true, maxdatapointsSys!!)
        }

        //SYSTOLIC



        for (i in 0 until dbsystolic.size)
        {

            val x = i.toDouble()+1
            val y = dbsystolic[i].toDouble()
            seriesSys.appendData(DataPoint(x,y), true, maxdatapointsSys!!)
        }

        //DIATOLIC




        for (i in 0 until dbdiatolic.size)
        {
            val x = i.toDouble()+1
            val y = dbdiatolic[i].toDouble()
            seriesDia.appendData(DataPoint(x,y), true, maxdatapointsSys!!)
        }

        // enable scaling and scrolling
        graphs.viewport.isYAxisBoundsManual = true
        graphs.viewport.setMinY(40.0)
        graphs.viewport.setMaxY(maxdatapointsSys!!.toDouble()+10)

        graphs.viewport.isXAxisBoundsManual = true
        graphs.viewport.setMinX(1.0)
        graphs.viewport.setMaxX(maxXdataPoints)

        graphs.viewport.isScrollable = true
        graphs.viewport.isScalable = true

        graphs.viewport.setScalableY(true)
        graphs.viewport.setScrollableY(true)



        seriesDia.color = Color.BLUE
        seriesDia.dataPointsRadius = 10f
        seriesDia.isDrawDataPoints = true

        seriesSys.color = Color.RED
        seriesSys.isDrawDataPoints = true
        seriesSys.dataPointsRadius = 10f

        val cl = getColor(R.color.chartplscolor)
        seriesPls.color = cl
        seriesPls.isDrawDataPoints = true
        seriesPls.dataPointsRadius = 10f



        seriesSys.setAnimated(true)
        seriesDia.setAnimated(true)
        seriesPls.setAnimated(true)
        graphs.addSeries(seriesSys)
        graphs.addSeries(seriesDia)
        graphs.addSeries(seriesPls)

        val v : View = findViewById(android.R.id.content)
        seriesDia.setOnDataPointTapListener { _, dataPoint ->


            val dto =  dataPoint.x.toInt()-1
            val tstr1 = "--> " +  dbdato[dto] + " - " + dbKlokka[dto]
            SnackBar().showInfo(v,tstr1,showGreenChar)

        }
        seriesSys.setOnDataPointTapListener { _, dataPoint ->

            val dto =  dataPoint.x.toInt()-1
            val tstr0 = "--> " +  dbdato[dto] + " - " + dbKlokka[dto]
            SnackBar().showInfo(v,tstr0,showGreenChar)
        }
        button_reset.setOnClickListener {
            val intent = Intent(this, LineChartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }

    private fun findLowestHighest() {

        var modesH : Int
        var modesL : Int

        var modesFormiddag = 0
        var modesKveld = 0
        for (i in 0 until dbsystolic.size) {

            if (dbKlokka[i] <=1 ) {// formiddag middag {
                val dsys = dbsystolic.get(index = i).toInt()
                val ddia = dbdiatolic.get(index = i).toInt()
                val dsum = ddia + dsys
                modesFormiddag += dsum

            }
            if (dbKlokka[i] >=2) {// kveld natt{
                val dsys = dbsystolic.get(index = i).toInt()
                val ddia = dbdiatolic.get(index = i).toInt()
                val dsum = ddia + dsys
                modesKveld += dsum

            }
        }
        if (modesFormiddag > modesKveld){
            modesH = 0
            modesL = 2
        } else {
            modesH = 2
            modesL = 0

        }
        val dla = findViewById<TextView>(R.id.daytime_lowest)
        val dha = findViewById<TextView>(R.id.daytime_highest)



        val dal = pressureTimeArray.get(index = modesL) + "/" + pressureTimeArray.get(index = modesL+1)
        dla.text =  dal
        val dah = pressureTimeArray.get(index = modesH) + "/" + pressureTimeArray.get(index = modesH+1)
        dha.text = dah

    }

    //MOSTLY measured
    private   fun themode(numbers: ArrayList<Int>): List<Int> {
        val modes = ArrayList<Int>()
        val countMap = HashMap<Int, Int>()

        var max = -1

        for (n in numbers) {
            var count : Int

            if (countMap.containsKey(n)) {
                count = countMap[n]?.plus(1) ?: 1
            } else {
                count = 1
            }

            countMap[n] = count

            if (count > max) {
                max = count
            }
        }

        for ((key, value) in countMap) {
            if (value == max) {
                modes.add(key)
            }
        }
        if (modes.size > 3) return emptyList()

        return modes
    }

}
