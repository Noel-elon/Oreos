package com.noelon.oreos

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class CookiePreference(context: Activity) {
    private val prefName = "my_prefs"
    private val cookieOneKey = "cookieOne"
    private val cookieTwoKey = "cookieTwo"
    private val cookieState = "cookieState"
    private val cookieTwoState = "cookieTwoState"
    private val setKey = "cookiesSet"
    private val gson = Gson()
    private val mPrefs = context.getSharedPreferences(prefName, MODE_PRIVATE)
    private val editor = mPrefs.edit()


    fun getCookieOne(): String? {
        val cookieOne = mPrefs.getString(cookieOneKey, null)
        return cookieOne
    }

    fun getCookieMap(domain: String): Map<String?, String?>? {
        var map: Map<String?, String?>? = null
        val set = mPrefs.getStringSet(setKey, setOf("default"))
        for (i in set!!) {
            if (i.contains(domain, true)) {
                map = gson.fromJson(
                    i,
                    object : TypeToken<HashMap<String?, String?>?>() {}.type
                )
            }
        }
        return map
    }

    fun setCookieMap(domain: String, cookie: String) {
        val map = mapOf(domain to cookie)
        val mapString = gson.toJson(map)
        val set = mPrefs.getStringSet(setKey, null)
        if (set != null) {
            set.add(mapString)
            editor.putStringSet(setKey, set)
        } else {
            val set2 = setOf(mapString)
            editor.putStringSet(setKey, set2)
        }
        editor.apply()

    }

    fun getCookieTwo(): String? {
        val cookieTwo = mPrefs.getString(cookieTwoKey, null)
        return cookieTwo
    }

    fun getCookieState(): Boolean {
        return mPrefs.getBoolean(cookieState, false)
    }

    fun getCookieTwoState(): Boolean {
        return mPrefs.getBoolean(cookieTwoState, false)
    }

    fun setCookieOne(cookie: String?) {
        if (cookie != null) {
            editor.putString(cookieOneKey, cookie)
            editor.apply()
        }
    }

    fun setCookieTwo(cookie: String?) {
        if (cookie != null) {
            editor.putString(cookieTwoKey, cookie)
            editor.apply()
        }
    }

    fun setCookieState(state: Boolean) {
        editor.putBoolean(cookieState, state)
        editor.apply()

    }

    fun setTwoCookieState(state: Boolean) {
        editor.putBoolean(cookieTwoState, state)
        editor.apply()

    }


}


