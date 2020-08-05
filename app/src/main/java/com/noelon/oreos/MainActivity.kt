package com.noelon.oreos

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.*


class MainActivity : AppCompatActivity() {
    val manager = java.net.CookieManager()
    val webManager: CookieManager = CookieManager.getInstance()
    var cookieList: List<String>? = null
    var cookieList1: Array<String>? = null
    var manyCookies: List<HttpCookie>? = null
    var cookieString: String? = null
    var cookieStringTwo: String? = null
    var isOneFilled: Boolean = false
    var isTwoFilled: Boolean = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(manager)
        webManager.setAcceptCookie(true)


        val url = "https://www.google.com"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            webManager.setAcceptThirdPartyCookies(web_view, true)
        }


        web_view.settings.javaScriptEnabled = true
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (reload.text == "1" && isOneFilled == false) {
                    cookieString = webManager.getCookie(url)
                }


                if (reload.text == "2" && isTwoFilled == false) {
                    cookieStringTwo = webManager.getCookie(url)
                }


            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                Log.i("Requests", request?.requestHeaders.toString())

                return super.shouldInterceptRequest(view, request)

            }


        }
        web_view.loadUrl(url)

        clear.setOnClickListener {
            Log.i("CookieStringOne", cookieString!!)
            Log.i("CookieStringTwo", cookieStringTwo!!)

        }

        reload.setOnClickListener {
            val abc: MutableMap<String, String> = HashMap()
            val abc2: MutableMap<String, String> = HashMap()
            if (reload.text == "2") {
                isTwoFilled = true
                if (cookieString != null) {
                    Log.i("CookieString: ", cookieString!!)

                    abc["Cookie"] = cookieString!!
                    Log.i("ABC", abc.toString())

                }
                webManager.removeAllCookie()
                web_view.loadUrl(url, abc)
                reload.text = "1"

            } else {
                isOneFilled = true
                if (cookieStringTwo != null) {
                    Log.i("CookieStringTwo: ", cookieStringTwo!!)

                    abc2["Cookie"] = cookieStringTwo!!
                    Log.i("ABC2", abc2.toString())
                }
                webManager.removeAllCookie()
                web_view.loadUrl(url, abc2)
                reload.text = "2"
            }

        }


    }


    fun getCookies(urlString: String) {
        Log.i("Method: ", "Called")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(urlString)
                val connection: URLConnection = url.openConnection()
                val headerFields: Map<String, List<String>> =
                    connection.headerFields
                val cookiesHeader =
                    headerFields["Set-Cookie"]
                cookieList = cookiesHeader
                Log.i("Header: ", "$cookiesHeader")

                if (cookiesHeader != null) {
                    for (cookie in cookiesHeader) {
                        manager.cookieStore
                            .add(null, HttpCookie.parse(cookie)[0])
                    }
                }
                manyCookies = manager.cookieStore.cookies

            } catch (e: Exception) {
                Log.d("Cookie", e.toString())

            }
        }


    }

    fun getCookie(siteName: String?): Array<String>? {
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(siteName)
        return cookies.split(";".toRegex()).toTypedArray()
    }
}