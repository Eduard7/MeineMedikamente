package com.kempinger.meinemedikamente.adapters


import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.activities.*
import com.kempinger.meinemedikamente.model.GetTimeAgo
import com.kempinger.meinemedikamente.model.ReturnFormatedDate
import kotlinx.android.synthetic.main.medikament_liste.view.*

var gangerAntall = 1

/**
 * Created by eduardkempinger on 26/03/2018.
 */




class MedikamentAdapter : RecyclerView.Adapter<ViewHolder>() {

    val context : MedikamentAdapter = this





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {



        val layoutInfalter = LayoutInflater.from(parent.context)
        val rows = layoutInfalter.inflate(R.layout.medikament_liste, parent, false)


        return ViewHolder(rows)
    }

    override fun getItemViewType(position: Int): Int {


        pos = position
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {

        return medikamentArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.view.medikamentLabel.text = medikamentArray[position]
        dbanzahlgenommen = genommenArray.get(index = position)
        val lstr = "$dosistxt nr.  $dbanzahlgenommen    →   " + letztesDatumArray.get(index = position)
        holder.view.letzteeinnahmeLabel?.text = lstr

        val tmpd = ReturnFormatedDate.theDateToReturn(oppstartArray.get(index = position))
        val tmpdiff = diffArray.get(index = position)
        val resurceStringPointer = GetTimeAgo().days(tmpdiff)
        val converteDays = GetTimeAgo().convertDays(tmpdiff)




        holder.view.oppstart_dato.text = tmpd
        when (resurceStringPointer) {

            0 -> holder.view.dayslabel.setText(R.string.daysago)
            1 -> holder.view.dayslabel.setText(R.string.weeksago)
            2 -> holder.view.dayslabel.setText(R.string.monhtsago)
            3 -> holder.view.dayslabel.setText(R.string.yearsago)
            10 -> holder.view.dayslabel.setText(R.string.today)
            11 -> holder.view.dayslabel.setText(R.string.wrongdate)
            else -> holder.view.dayslabel.text = ""

        }

        fun moreAlarms(start: String) : String {
            var tmpstr = clockSymbol + " "
            val p = start.split(":")
            val m = p[1]
            var uz = p[0].toInt()
            val steps : Int
            dbtgl = tglArray.get(index = position)
            when (dbtgl) {
                0 -> steps = 0
                1 -> steps = 7
                2 -> steps = 5
                3 -> steps = 4

                else -> steps = 3
            }
            for (i in 0..dbtgl) {
                tmpstr += "$uz:$m   "
                uz += steps
                if (uz >=24 ){uz = uz - 24}
            }
            return tmpstr
        }

        if (converteDays > 0) {
            holder.view.dayscount.text = converteDays.toString()
        } else {
            holder.view.dayscount.text = "-"
        }
        dbuuid = uuidArray.get(index = position)

        holder.view.vmaLabel?.text = ""
        holder.view.vmaLabel?.text = vmallArray[position]
        dbVdag = holder.view.vmaLabel?.text.toString()



        if (alarmsetArray.get(index = position)) {
            holder.view.ersterwekker.visibility = View.VISIBLE
            val nextalarms = moreAlarms(alarmZeitArray.get(index = position))
           // val wtmp =   alarmZeitArray.get(index = position)
            holder.view.alarmZeitlabel.text = ""

            holder.view.ersterwekker.setText(nextalarms)
          //  holder.view.wechericon.setImageResource(R.drawable.ic_alarm_on_black_24dp)
          //  holder.view.wechericon.setColorFilter(Color.parseColor("#115a7b"))
            holder.view.wechericon.visibility = View.INVISIBLE

        } else {
            holder.view.wechericon.visibility = View.VISIBLE
            holder.view.ersterwekker.visibility = View.INVISIBLE
            holder.view.alarmZeitlabel.text =""
            holder.view.wechericon.setImageResource(R.drawable.ic_alarm_off_black_24dp)
            holder.view.ersterwekker.text = ""
            holder.view.wechericon.setColorFilter(Color.GRAY)


        }


        val mstr =  mahlzeit[position]
        if(mstr == 0) { holder.view.imageViewMahlzeit.setImageResource(R.drawable.vor_mahlzeit)}
        if(mstr == 1)  {holder.view.imageViewMahlzeit.setImageResource(R.drawable.zur_mahlzeit)}
        if(mstr == 2)  {holder.view.imageViewMahlzeit.setImageResource(R.drawable.nach_mahlzeit)}
        if(mstr == 3)  {holder.view.imageViewMahlzeit.setImageResource(R.drawable.nach_belieben)}



        val tstr = dosisArray.get(index = position) + " / " + taglichArray[tglArray[position]]

        holder.view.einnameLabel?.text = tstr
        dbablauf = ablaufArray.get(index = position)

        holder.view.ablaufLabel?.text = dbablauf

        dbdosis = tstr

        /* ON CLICK LISTENER*/
        holder.itemView?.setOnClickListener {

            dbtgl = tglArray[position]
            dbalarm = alarmsetArray.get(index = position)
            dbmedikament = medikamentArray.get(index = position)
            dbuuid = uuidArray.get(index = position)
            dbanzahlgenommen = genommenArray.get(index = position)
            dbablauf = ablaufArray.get(index = position)
            dbdosis = tstr
            holder.view.vmaLabel?.text = vmallArray[position]
            gangerAntall = tglAntallArray.get(index = position)

            dbVdag = holder.view.vmaLabel?.text.toString()
            dbVdag = vmallArray[position]
            dbAntallIPackning = anzahlArray.get( index = position)

            Goto(holder.view)

        }


    }

}


private class Goto(view: View) {
    init {
        val intent = Intent(view.context, MedikamentEinnahme::class.java)
        view.context.startActivity(intent)


    }
}


class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    init {


    }
}