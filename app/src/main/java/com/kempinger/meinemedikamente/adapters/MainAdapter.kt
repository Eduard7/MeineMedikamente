package com.kempinger.meinemedikamente.adapters


import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.activities.*
import com.kempinger.meinemedikamente.model.CountInntak
import kotlinx.android.synthetic.main.recycler_layout.view.*

var headingStr : String=""
//(val clickListener: AdapterView.OnItemClickListener)
class MainAdapter  : RecyclerView.Adapter<CustomViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val cellforrow = layoutinflater.inflate(R.layout.recycler_layout, parent, false)

        return CustomViewHolder(cellforrow)
    }

    override fun getItemCount(): Int {


           if (headingOnly) {

           return logHeadings.size
        } else {
            return logMedikamentArray.size
         }
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.itemView?.setOnClickListener {

            if (headingOnly) {
                dblogSelect = logHeadings.get(index = position)
                headingStr = logHeadings.get(index = position)
                GotoNext(holder.view)

            } else {
                GotoNext(holder.view)
            }

            headingOnly = !headingOnly


        }

        if (headingOnly) {
            holder.view.medikamentTellLabel.visibility = View.VISIBLE
            holder.view.medikamentText.setTextColor(Color.BLACK)
            val cntinntak = CountInntak.getInntakCount(logHeadings.get(index = position))
            val strinntak = logHeadings.get(index = position)
            val tostring = cntinntak.toString() + "x"
            holder.view.medikamentTellLabel.text =  tostring
            holder.view.medikamentText?.text = strinntak
            holder.view.imageViewTablette.setImageResource(R.drawable.medizin_logo)



            holder.view.dosierungText?.setText(R.string.dobbel_click)
        } else {



            holder.view.medikamentTellLabel.visibility = View.INVISIBLE

            holder.view.imageViewTablette.setImageResource(R.drawable.palmegrene)
            holder.view.medikamentText.setTextColor(Color.BLUE)
            holder.view.medikamentText?.setText(R.string.dosis)
            holder.view.datumText?.text = logDatumArray.get(index = position)
            holder.view.uhrzeitText?.text = logUhrzeitArray.get(index = position)
            holder.view.dosierungText?.text = logDosisArray.get(index = position)
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }
}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    init {

    }
}


class GotoNext(view: View) {
    init {

            val intent = Intent(view.context, LogActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            view.context.startActivity(intent)

    }




}