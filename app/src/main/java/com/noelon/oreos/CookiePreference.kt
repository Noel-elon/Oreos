package com.noelon.oreos

import android.app.Activity
import android.content.Context.MODE_PRIVATE

class CookiePreference(context: Activity) {
    private val prefName = "my_prefs"
    private val cookieOneKey = "cookieOne"
    private val cookieTwoKey = "cookieTwo"
    private val cookieState = "cookieState"
    private val mPrefs = context.getSharedPreferences(prefName, MODE_PRIVATE)
    private val editor = mPrefs.edit()


    fun getCookieOne(): String? {
        val cookieOne = mPrefs.getString(cookieOneKey, null)
        return cookieOne
    }

    fun getCookieTwo(): String? {
        val cookieTwo = mPrefs.getString(cookieTwoKey, null)
        return cookieTwo
    }

    fun getCookieState(): Boolean {
        return mPrefs.getBoolean(cookieState, false)
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


}


