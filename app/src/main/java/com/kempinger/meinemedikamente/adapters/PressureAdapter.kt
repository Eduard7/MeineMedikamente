package com.kempinger.meinemedikamente.adapters


import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.activities.*
import com.kempinger.meinemedikamente.model.DisplayPressureColorScala
import kotlinx.android.synthetic.main.pressure_layout.view.*


// class PressureAdapter : RecyclerView.Adapter<CustomViewHolder0>()
class PressureAdapter : RecyclerView.Adapter<CustomViewHolder0>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder0 {

        val layoutinflater = LayoutInflater.from(parent.context)
        val cellforrow = layoutinflater.inflate(R.layout.pressure_layout, parent, false)
        return CustomViewHolder0(cellforrow)
    }

    override fun getItemCount(): Int {

        return dbdato.size
    }


    override fun onBindViewHolder(holder: CustomViewHolder0, position: Int) {


        holder.view.pressure_date.text = dbdato.get(index = position)
        holder.view.pressure_dia.text = dbdiatolic.get(index = position)
        holder.view.pressure_sys.text = dbsystolic.get(index = position)
        val tmpkl =   dbKlokka.get(index = position)
        holder.view.pressure_klokka.text = pressureTimeArray[tmpkl]
        holder.view.pressure_klokka.setTextColor(Color.WHITE)
        val mcoldia = DisplayPressureColorScala().returnColorDia(dbdiatolic.get(index = position).toInt())
        val mcolsys = DisplayPressureColorScala().returnColorSys(dbsystolic.get(index = position).toInt())


        holder.view.pressure_dia.setTextColor(Color.parseColor(mcoldia))
        holder.view.pressure_sys.setTextColor(Color.parseColor(mcolsys))
        holder.view.pressure_puls.text = dbpuls.get(index = position)
        holder.view.checkBox.isChecked = blodtrykk_checkbox.get(index = position)



        holder.view.setOnClickListener {

            holder.view.checkBox.isChecked = !holder.view.checkBox.isChecked
            blodtrykk_checkbox[position] = holder.view.checkBox.isChecked

        }


    }

}


private fun invoke() {


}

class CustomViewHolder0(val view: View) : RecyclerView.ViewHolder(view) {
    init {

    }

}








