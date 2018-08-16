//package com.kempinger.meinemedikamente.Room
//
//import android.arch.persistence.room.*
//
//
//
//@Dao
//interface DAOPerson {
//
//
//    @Insert
//    fun savePerson(item: ROOMPerson)
//
//    @Update
//    fun updatePerson(item: ROOMPerson)
//
//
//    @Query("SELECT * FROM personDB WHERE dbID = :uuid")
//    fun deletePerson(uuid: String)
//
//
//    @Query("SELECT * FROM personDB WHERE dbID = :uuid")
//    fun getPerson(uuid:String)
//
//
//
//}