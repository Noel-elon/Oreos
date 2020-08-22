package com.noelon.oreos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    val webManager: CookieManager = CookieManager.getInstance()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val prefs = CookiePreference(this)
        val url = "https://www.google.com"
        web_view3.settings.javaScriptEnabled = true
        web_view3.settings.domStorageEnabled = false
        webManager.setAcceptCookie(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            webManager.setAcceptThirdPartyCookies(web_view3, true)
        }

        web_view3.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val cookieString = webManager.getCookie(url)
                if (!prefs.getCookieTwoState()) {
                    prefs.setCookieTwo(cookieString)
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (prefs.getCookieTwo() != null) {
                    val map: MutableMap<String, String> = HashMap()
                    map["Cookie"] = prefs.getCookieTwo()!!
                    webManager.removeAllCookie()
                    view?.loadUrl(request?.url.toString(), map)
                }
                return true
            }


        }
        if (prefs.getCookieTwo() != null) {
            val map: MutableMap<String, String> = HashMap()
            map["Cookie"] = prefs.getCookieTwo()!!
            webManager.removeAllCookie()
            web_view3.loadUrl(url, map)
        } else {
            webManager.removeAllCookie()
            web_view3.loadUrl(url)
        }



        but.setOnClickListener {
            prefs.setTwoCookieState(true)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}