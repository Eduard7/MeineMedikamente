//package com.kempinger.meinemedikamente.Room
//
//
//import android.arch.persistence.room.*
//
//@Dao
//interface DAOmedizin {
//
//
//    // MEDIKAMENT DATA
//    @Query("SELECT * FROM medikamentDB ORDER BY dbStartDatum")
//    fun getAllMedikamenter(): List<ROOMMedikamenter>
//
//    @Query("SELECT * FROM medikamentDB WHERE dbID = :uuid")
//    fun getMedisinById(uuid:String)
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun saveMedikament(item: ROOMMedikamenter)
//
//    @Update
//    fun updateMedikament(item: ROOMMedikamenter)
//
//    @Delete
//    fun removeMedikament(item: ROOMMedikamenter)
//
//
//
//
//
//}
