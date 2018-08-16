//package com.kempinger.meinemedikamente.Room
//
//
//import android.arch.persistence.room.Database
//import android.arch.persistence.room.Room
//import android.arch.persistence.room.RoomDatabase
//import android.content.Context
//
//
//@Database(entities =
//    [ROOMMedikamenter::class,
//    ROOMlogfile::class,
//    ROOMBlodPressure::class,
//    ROOMPerson::class,
//    ROOMTrial::class],
//        version = 1,exportSchema = true)
//
//abstract class MedDatabases : RoomDatabase(){
//
//
//    companion object {
//        private var INSTANCE: MedDatabases? = null
//
//        fun getDatabase(context: Context): MedDatabases {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(context.applicationContext, MedDatabases::class.java, "medizin_db").build()
//            }
//            return INSTANCE as MedDatabases
//        }
//
//        fun destroyInstance() {
//            INSTANCE = null
//        }
//    }
//    abstract fun getMedisinDao():DAOmedizin
//
//    abstract fun getLogDataDao():DAOlogdata
//
//
//}
