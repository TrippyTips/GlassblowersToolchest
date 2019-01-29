package com.trippytips.glassblowerstoolchest

import android.content.Context

class GTCPreference(context: Context){

    val PREFERENCE_NAME = "GlassblowersToolchest"
    val PREFERENCE_COE = "COE"

    val preference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getCOE() : Int {
        return preference.getInt(PREFERENCE_COE, 33)
    }

    fun setCOE(coe: Int){
        val editor = preference.edit()
        editor.putInt(PREFERENCE_COE, coe)
        editor.apply()
    }


}