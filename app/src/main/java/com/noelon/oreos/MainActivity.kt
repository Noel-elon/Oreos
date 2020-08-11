package com.noelon.oreos

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.*


class MainActivity : AppCompatActivity() {
    val webManager: CookieManager = CookieManager.getInstance()
    var cookieString: String? = null
    var cookieStringTwo: String? = null
    var isOneFilled: Boolean = false
    var isTwoFilled: Boolean = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = CookiePreference(this)
        webManager.setAcceptCookie(true)

        if (prefs.getCookieOne() != null) {
            Log.i("CookieOnePref: ", prefs.getCookieOne()!!)
        }


        val url = "https://www.google.com"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            webManager.setAcceptThirdPartyCookies(web_view, true)
        }
        web_view2.settings.javaScriptEnabled = true

        web_view.settings.javaScriptEnabled = true
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                cookieString = webManager.getCookie(url)
                if (!prefs.getCookieState()) {
                    Log.i("CookieOneHere: ", "Here")
                    prefs.setCookieOne(cookieString)
                }



                if (reload.text == "2" && !isTwoFilled) {
                    cookieStringTwo = webManager.getCookie(url)
                }


            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.i("PageStarted: ", "$url")
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                Log.i("Requests", request?.requestHeaders.toString())

                return super.shouldInterceptRequest(view, request)

            }


        }
        web_view2.loadUrl(url)
        if (prefs.getCookieOne() != null) {
            val map: MutableMap<String, String> = HashMap()
            map["Cookie"] = prefs.getCookieOne()!!
            webManager.removeAllCookie()
            web_view.loadUrl(url, map)
        } else {
            web_view.loadUrl(url)
        }


        clear.setOnClickListener {
            prefs.setCookieState(true)
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
//            web_view.visibility = View.INVISIBLE
//            web_view2.visibility = View.VISIBLE

        }

        reload.setOnClickListener {

            web_view2.visibility = View.INVISIBLE
            web_view.visibility = View.VISIBLE


//            val abc: MutableMap<String, String> = HashMap()
//            val abc2: MutableMap<String, String> = HashMap()
//            if (reload.text == "2") {
//                isTwoFilled = true
//                if (cookieString != null) {
//                    Log.i("CookieString: ", cookieString!!)
//
//                    abc["Cookie"] = cookieString!!
//                    Log.i("ABC", abc.toString())
//
//                }
//                webManager.removeAllCookie()
//                web_view.loadUrl(url, abc)
//                reload.text = "1"
//
//            } else {
//                isOneFilled = true
//                if (cookieStringTwo != null) {
//                    Log.i("CookieStringTwo: ", cookieStringTwo!!)
//
//                    abc2["Cookie"] = cookieStringTwo!!
//                    Log.i("ABC2", abc2.toString())
//                }
//                webManager.removeAllCookie()
//                web_view.loadUrl(url, abc2)
//                reload.text = "2"
//            }

        }


    }


}