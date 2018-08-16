package com.kempinger.meinemedikamente.model

import com.kempinger.meinemedikamente.activities.taglichArray


class InitArrays
{


    fun initTaglich(enhet: String) {

        taglichArray.clear()
                    for (m in 0..4){
            val tmp = "${m+1} x $enhet"
            taglichArray.add(tmp )
        }
     //  taglichArray.add(w)


    }

}

/*

-=0)8*7&6^5%4$3#`~`z`§`~``'";:[{{{}{}:"…æPÚÆ{}[][]{}
 */