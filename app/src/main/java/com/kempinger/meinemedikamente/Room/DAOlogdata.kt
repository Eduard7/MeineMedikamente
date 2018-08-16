//package com.kempinger.meinemedikamente.Room
//
//
//import android.arch.persistence.room.*
//
//
//@Dao
//interface DAOlogdata
//{
//
//    // LOG DATA
//    @Query("SELECT * from logDB ORDER BY dbMedikament")
//    fun getAllLogdata(): List<ROOMlogfile>
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun saveLogData(item: ROOMlogfile)
//
//    @Update
//    fun updateLogData(item: ROOMlogfile)
//
//    @Delete
//    fun removeLogData(item: ROOMlogfile)
//}
