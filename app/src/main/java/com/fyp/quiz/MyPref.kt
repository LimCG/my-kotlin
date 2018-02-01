package com.fyp.quiz

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by limcg on 06/01/2018.
 */
class MyPref {

    companion object {

        val PREFS_FILENAME = MyApp.instance!!.packageName
        lateinit var prefs : SharedPreferences

        var userIDPrefs: Int
            get() = prefs.getInt("user_id_key", 0)
            set(value) = prefs.edit().putInt("user_id_key", value).apply()

        var userFullNamePrefs: String
            get() = prefs.getString("user_full_name", null)
            set(value) = prefs.edit().putString("user_full_name", value).apply()


        fun init(context : Context)
        {
            prefs = context.getSharedPreferences(PREFS_FILENAME, 0)

        }

    }


}