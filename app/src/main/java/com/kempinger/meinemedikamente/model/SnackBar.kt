package com.kempinger.meinemedikamente.model

import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.View
import com.kempinger.meinemedikamente.activities.ok

class SnackBar {

     fun showInfo(v:View, msg:String,accepted: Boolean) {

        //val v : View = v
        val snb = Snackbar.make(v,msg, Snackbar.LENGTH_LONG)
                .setAction(ok){}

        if (!accepted) {
            snb.setActionTextColor(Color.RED)
        } else {
            snb.setActionTextColor(Color.GREEN)
        }
                .show()

    }
}