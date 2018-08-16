package com.kempinger.meinemedikamente.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*


/*
* ROOM
*  @Entity
*  public class Medizin
*  {
*   private var id:Int
*   private var name:String
*  }
*
* */
@RealmClass

open class DBmedizin : RealmObject() {

    @PrimaryKey
    var dbID : String =""
    var dbDate : String = ""
    var dbStartDatum : Date? = null
    var dbMedikament : String = ""
    var dbAblauf : String = ""
    var dbTaglich : Int = 1
    var dbAlarm : Boolean = false
    var dbAlarmZeit : String = ""
    var dbVMAN : String = ""
    var dbN : Boolean = false
    var dbMahlzeit :Int = 0
    var dbDagGenommen = false
    var dbDosisEnhet: String =""
    var dbAnzahlGenommen : Int = 0
    var dbLetztesDatum : String =""
    var dbAnzahl : Int = 0
    var dbAnzahlMedikament = 0

}

@RealmClass
open class DBTrial : RealmObject() {
    var dbDate : String = ""
    var dbStartDatum : Date? = null
}


@RealmClass
open class DBlogfile : RealmObject() {
    var dbID : String =""
    var dbLetztesDatum : String =""
    var dbUhrZeit : String = ""
    var dbDosis : String = ""
    var dbMedikament : String = ""
    var dbDatoLong : Long = 0
}

@RealmClass
open class DBBlodPressure: RealmObject() {
    @PrimaryKey
    var dbID : String = ""
    var dbUser : String = ""
    var dbSortDatoAar : Int = 0
    var dbSortDatoMnd : Int = 0
    var dbSortDatoDag : Int = 0
    var dbKlokka : Int = 0 // tid for måling in morgen middag kveld
    var dbSys : String = ""
    var dbDia : String = ""
    var dbPuls: String = ""
    var dbDato : String = ""

}

@RealmClass
open class Person: RealmObject() {
    @PrimaryKey
    var dbID : String=""
    var dbAge : Int = 50 //default
    var dbSex : Boolean = true //woman
}