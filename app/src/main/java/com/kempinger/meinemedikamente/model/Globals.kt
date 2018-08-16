package com.kempinger.meinemedikamente.model

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.view.WindowManager
import android.widget.Toast
import com.kempinger.meinemedikamente.activities.colorselected
import com.kempinger.meinemedikamente.activities.felleBackGroundColorID
import android.view.Window

object Globals// Application()
{

      fun setBackGroundColor(win:Window, layout: ConstraintLayout) {

          val mcolor = Color.parseColor(colorselected.get(index = felleBackGroundColorID))
          layout.setBackgroundColor(mcolor)
          win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
          win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
          win.setStatusBarColor(mcolor)

    }



}
