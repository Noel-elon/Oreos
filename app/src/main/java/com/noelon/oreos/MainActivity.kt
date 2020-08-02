package com.noelon.oreos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.CookieHandler
import java.net.CookiePolicy
import java.net.URL
import java.net.URLConnection


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = "https://www.google.com"


//
//        web_view.settings.javaScriptEnabled = true
//        web_view.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                val cookieManager: CookieManager = CookieManager.getInstance()
//                cookieManager.setAcceptCookie(true)
//
//                getCookies(url!!)
//
//
//            }
//
//
//        }
//        web_view.loadUrl(url)
        GlobalScope.launch(Dispatchers.IO) {
            try {

                val web = WebService
                var response = web.sendGet(url)
                Log.i("Cookies: ", response)
            } catch (e: Exception) {
                Log.i("CookieL", e.toString())

            }
        }

    }


    fun getCookies(urlString: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val manager = java.net.CookieManager()
                manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
                CookieHandler.setDefault(manager)
                val url = URL(urlString)
                val connection: URLConnection = url.openConnection()
                connection.content
                val cookierJar = manager.cookieStore
                Log.d("Cookies: ", cookierJar.cookies.toString())

            } catch (e: Exception) {
                Log.d("Cookie", e.toString())

            }
        }


    }
}