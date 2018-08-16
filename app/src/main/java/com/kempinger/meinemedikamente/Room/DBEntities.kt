//package com.kempinger.meinemedikamente.Room
//
//import android.arch.persistence.room.Entity
//import io.realm.RealmObject
//import io.realm.annotations.PrimaryKey
//import io.realm.annotations.RealmClass
//import java.util.*
//
//
//@Entity(tableName = "medikamentDB")
//
//data class ROOMMedikamenter (
//
//    @PrimaryKey
//    var dbID : String,
//    var dbDate : String,
//    var dbStartDatum : Date?,
//    var dbMedikament : String,
//    var dbAblauf : String,
//    var dbTaglich : Int,
//    var dbAlarm : Boolean,
//    var dbAlarmZeit : String,
//    var dbVMAN : String,
//    var dbN : Boolean,
//    var dbMahlzeit :Int,
//    var dbDagGenommen : Boolean,
//    var dbDosisEnhet: String,
//    var dbAnzahlGenommen : Int,
//    var dbLetztesDatum : String,
//    var dbAnzahl : Int,
//    var dbAnzahlMedikament :Int
//
//){constructor():this("","",null,"","",1,false,"","",false,0,false,"",0,"",0,0)}
//
//@Entity(tableName = "logDB")
//data class ROOMlogfile  (
//    var dbID : String,
//    var dbLetztesDatum : String,
//    var dbUhrZeit : String,
//    var dbDosis : String,
//    var dbMedikament : String,
//    var dbDatoLong : Long
//) {constructor() : this("","","","","",0)}
//
//
//@Entity(tableName = "trialDB")
//data class ROOMTrial  (
//    var dbDate : String ,
//    var dbStartDatum : Date?
//) {constructor() : this("", Date())}
//
//
//@Entity(tableName = "pressureDB")
//open class ROOMBlodPressure (
//    @PrimaryKey
//    var dbID : String,
//    var dbUser : String,
//    var dbSortDatoAar : Int,
//    var dbSortDatoMnd : Int,
//    var dbSortDatoDag : Int,
//    var dbKlokka : Int ,
//    var dbSys : String,
//    var dbDia : String ,
//    var dbPuls: String ,
//    var dbDato : String
//
//) {constructor() : this("","",0,0,0,0,"","","","")}
//
//@Entity(tableName = "personDB")
//open class ROOMPerson(
//    @PrimaryKey
//    var dbID : String,
//    var dbAge : Int ,
//    var dbSex : Boolean//woman = false
//    )
//{constructor() :this("",50,false)}