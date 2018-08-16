package com.kempinger.meinemedikamente.model


import io.realm.Realm
import io.realm.RealmConfiguration

object CountInntak {


     fun getInntakCount(stringToFind : String) : Int  {

        val config = RealmConfiguration.Builder()
                .name("logfile.realm").build()

        val realm = Realm.getInstance(config)
        realm.beginTransaction()
        val log = realm.where(DBlogfile::class.java).equalTo("dbMedikament", stringToFind).findAll()

        val cnt = 1



        realm.commitTransaction()
        if (log.size > 1) {
            return log.size
        } else
        {
            return cnt
        }



    }
}