package com.trippytips.glassblowerstoolchest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "scedulesDB.db"
val TABLE_NAME = "schedule"
val COL_THICKNESS = "thickness"
val COL_NAME = "name"
val COL_STEP1 = "ramp1"
val COL_STEP2 = "degrees1"
val COL_STEP3 = "hold1"
val COL_STEP4 = "ramp2"
val COL_STEP5 = "degrees2"
val COL_STEP6 = "hold2"
val COL_STEP7 = "ramp3"
val COL_STEP8 = "degrees3"
val COL_STEP9 = "hold3"
val COL_STEP10 = "ramp4"
val COL_STEP11 = "degrees4"
val COL_STEP12 = "hold4"
val COL_STEP13 = "ramp5"
val COL_STEP14 = "degrees5"
val COL_STEP15 = "hold5"
val COL_ID = "id"
val COL_COE = "coe"
val COL_UNITS = "units"


class ksDataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        //What to do when the device does not contain the Database


        val createTable =
            """CREATE TABLE $TABLE_NAME($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_NAME VARCHAR(25),$COL_THICKNESS VARCHAR(25),$COL_STEP1 VARCHAR(25),$COL_STEP2 VARCHAR(25),$COL_STEP3 VARCHAR(25),$COL_STEP4 VARCHAR(25),$COL_STEP5 VARCHAR(25),$COL_STEP6 VARCHAR(25),$COL_STEP7 VARCHAR(25),$COL_STEP8 VARCHAR(25),$COL_STEP9 VARCHAR(25),$COL_STEP10 VARCHAR(25),$COL_STEP11 VARCHAR(25),$COL_STEP12 VARCHAR(25),$COL_STEP13 VARCHAR(25),$COL_STEP14 VARCHAR(25),$COL_STEP15 VARCHAR(25),$COL_COE VARCHAR(5),$COL_UNITS VARCHAR(8))""";

        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertData(ks: Ks){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_ID,ks.id)
        cv.put(COL_NAME,ks.name)
        cv.put(COL_THICKNESS,ks.thickness)
        cv.put(COL_STEP1,ks.step1)
        cv.put(COL_STEP2,ks.step2)
        cv.put(COL_STEP3,ks.step3)
        cv.put(COL_STEP4,ks.step4)
        cv.put(COL_STEP5,ks.step5)
        cv.put(COL_STEP6,ks.step6)
        cv.put(COL_STEP7,ks.step7)
        cv.put(COL_STEP8,ks.step8)
        cv.put(COL_STEP9,ks.step9)
        cv.put(COL_STEP10,ks.step10)
        cv.put(COL_STEP11,ks.step11)
        cv.put(COL_STEP12,ks.step12)
        cv.put(COL_STEP13,ks.step13)
        cv.put(COL_STEP14,ks.step14)
        cv.put(COL_STEP15,ks.step15)
        cv.put(COL_COE,ks.coe)
        cv.put(COL_UNITS,ks.units)

        var result = db.insert(TABLE_NAME,null,cv)
        if(result == (-1).toLong())
            Toast.makeText(context,"FAIL!",Toast.LENGTH_SHORT).show()
        //else
            //Toast.makeText(context,"Success!",Toast.LENGTH_SHORT).show()

    }

    fun updateData(ks: Ks){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_ID,ks.id)
        cv.put(COL_NAME,ks.name)
        cv.put(COL_THICKNESS,ks.thickness)
        cv.put(COL_STEP1,ks.step1)
        cv.put(COL_STEP2,ks.step2)
        cv.put(COL_STEP3,ks.step3)
        cv.put(COL_STEP4,ks.step4)
        cv.put(COL_STEP5,ks.step5)
        cv.put(COL_STEP6,ks.step6)
        cv.put(COL_STEP7,ks.step7)
        cv.put(COL_STEP8,ks.step8)
        cv.put(COL_STEP9,ks.step9)
        cv.put(COL_STEP10,ks.step10)
        cv.put(COL_STEP11,ks.step11)
        cv.put(COL_STEP12,ks.step12)
        cv.put(COL_STEP13,ks.step13)
        cv.put(COL_STEP14,ks.step14)
        cv.put(COL_STEP15,ks.step15)
        cv.put(COL_COE,ks.coe)
        cv.put(COL_UNITS,ks.units)
        var result = db.update(TABLE_NAME, cv, "ID = ?", arrayOf(ks.id.toString()))

        if(result > 0)
            //Toast.makeText(context,"Updated Successfully!",Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context,"Update FAILED!",Toast.LENGTH_LONG).show()

    }

    /*fun updateData() {
        //read the data from the database
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        //moveToFirst method will return true if the cursor is not null and has at least one value.
        if(result.moveToFirst()){
            do{
                var cv = ContentValues()
                cv.put(COL_NAME,"New Name")
                db.update(TABLE_NAME,cv, COL_NAME +
                        "=? AND " + COL_STEP1 +
                        "=? AND " + COL_STEP2 +
                        "=? AND " + COL_STEP3 +
                        "=? AND " + COL_STEP4 +
                        "=? AND " + COL_STEP5 +
                        "=? AND " + COL_STEP6 +
                        "=? AND " + COL_STEP7 +
                        "=? AND " + COL_STEP8 +
                        "=? AND " + COL_STEP9 +
                        "=? AND " + COL_STEP10 +
                        "=? AND " + COL_STEP11 +
                        "=? AND " + COL_STEP12 +
                        "=? AND " + COL_STEP13 +
                        "=? AND " + COL_STEP14 +
                        "=? AND " + COL_STEP15 +
                        "=? AND ",
                    arrayOf(result.getString(result.getColumnIndex(COL_NAME)),
                        result.getString(result.getColumnIndex(COL_STEP1)),
                        result.getString(result.getColumnIndex(COL_STEP2)),
                        result.getString(result.getColumnIndex(COL_STEP3)),
                        result.getString(result.getColumnIndex(COL_STEP4)),
                        result.getString(result.getColumnIndex(COL_STEP5)),
                        result.getString(result.getColumnIndex(COL_STEP6)),
                        result.getString(result.getColumnIndex(COL_STEP7)),
                        result.getString(result.getColumnIndex(COL_STEP8)),
                        result.getString(result.getColumnIndex(COL_STEP9)),
                        result.getString(result.getColumnIndex(COL_STEP10)),
                        result.getString(result.getColumnIndex(COL_STEP11)),
                        result.getString(result.getColumnIndex(COL_STEP12)),
                        result.getString(result.getColumnIndex(COL_STEP13)),
                        result.getString(result.getColumnIndex(COL_STEP14)),
                        result.getString(result.getColumnIndex(COL_STEP15))))

            }while (result.moveToNext())
        }

        result.close()
        db.close()
    }
*/

    fun readData() : MutableList<Ks>{
        var list: MutableList<Ks> = ArrayList()

        //read the data from the database
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        //moveToFirst method will return true if the cursor is not null and has at least one value.
        if(result.moveToFirst()){
            do{
                var ks = Ks()
                ks.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                ks.name = result.getString(result.getColumnIndex(COL_NAME))
                ks.thickness = result.getString(result.getColumnIndex(COL_THICKNESS))
                ks.step1 = result.getString(result.getColumnIndex(COL_STEP1))
                ks.step2 = result.getString(result.getColumnIndex(COL_STEP2))
                ks.step3 = result.getString(result.getColumnIndex(COL_STEP3))
                ks.step4 = result.getString(result.getColumnIndex(COL_STEP4))
                ks.step5 = result.getString(result.getColumnIndex(COL_STEP5))
                ks.step6 = result.getString(result.getColumnIndex(COL_STEP6))
                ks.step7 = result.getString(result.getColumnIndex(COL_STEP7))
                ks.step8 = result.getString(result.getColumnIndex(COL_STEP8))
                ks.step9 = result.getString(result.getColumnIndex(COL_STEP9))
                ks.step10 = result.getString(result.getColumnIndex(COL_STEP10))
                ks.step11 = result.getString(result.getColumnIndex(COL_STEP11))
                ks.step12 = result.getString(result.getColumnIndex(COL_STEP12))
                ks.step13 = result.getString(result.getColumnIndex(COL_STEP13))
                ks.step14 = result.getString(result.getColumnIndex(COL_STEP14))
                ks.step15 = result.getString(result.getColumnIndex(COL_STEP15))
                ks.coe = result.getString(result.getColumnIndex(COL_COE)).toInt()
                try{ks.units = result.getString(result.getColumnIndex(COL_UNITS))}catch(e:Exception){
                    Toast.makeText(context, "The database is outdated\n" +
                                                "Please delete the app data\n" +
                                                "or Uninstall and Re-Install." +
                                                "This won't happen on release of the app\n" +
                                                "Only in the test app.",Toast.LENGTH_LONG).show()
                }
                list.add(ks)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }



    fun deleteData(){
        val db = this.writableDatabase

        db.delete(TABLE_NAME, null, null)

        db.close()
    }

    fun deleteFirst(){
        val db = this.writableDatabase


        db.delete(TABLE_NAME, COL_ID+"=?", arrayOf(1.toString()))

        db.close()

    }

    fun deleteSelected(ks:Ks){
        val db = this.writableDatabase

        db.delete(TABLE_NAME,COL_ID+"=?", arrayOf(ks.id.toString()))

        db.close()
    }

}

