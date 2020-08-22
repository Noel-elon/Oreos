package com.noelon.oreos

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException


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


        val url = "https://www.google.com"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            webManager.setAcceptThirdPartyCookies(web_view, true)
        }
        // web_view.settings.domStorageEnabled = true

        web_view.settings.javaScriptEnabled = true
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.i("RequestsssssList", url!!)
                val st = getDomain(url)
                val map = prefs.getCookieMap(st)
                if (!prefs.getCookieState()) {
                    Log.i("RequestsssssMap", "Inside hereeee")
                    prefs.setCookieMap(st, webManager.getCookie(url))

                }


                cookieString = webManager.getCookie(url)
                if (!prefs.getCookieState()) {
                    prefs.setCookieOne(cookieString)
                }

                if (reload.text == "2" && !isTwoFilled) {
                    cookieStringTwo = webManager.getCookie(url)
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                val dom = getDomain(url!!)
                val map = prefs.getCookieMap(dom)
                if (map != null) {
                    val arr =
                        map.toString().split("=".toRegex(), 2).toTypedArray()
                    val cutCookie = arr[1]
                    val cookie = removeLastChars(cutCookie,1)
                    Log.i("RequestsssssCutCookie", cookie!!)

                }


            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                prefs.setCookieState(false)

                if (prefs.getCookieOne() != null && prefs.getCookieState()) {
                    val map: MutableMap<String, String> = HashMap()
                    map["Cookie"] = prefs.getCookieOne()!!
                    Log.i("Requestsssss", request?.url.toString())
                    webManager.removeAllCookie()
                    view?.loadUrl(request?.url.toString(), map)
                }
                return true
            }


        }
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
        }

        reload.setOnClickListener {
        }

    }

    fun okHelper(url: String, cookie: String) {
        val client = OkHttpClient().newBuilder()
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)

                override fun intercept(chain: Interceptor.Chain): Response {
                    val original: Request = chain.request()
                    val authorized = original.newBuilder()
                        .addHeader("Cookie", cookie)
                        .build()
                    return chain.proceed(authorized)
                }
            })
            .build()

        val oRequest: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(oRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.i("ResponseSuccess: ", "${response.code}")
                } else {
                    Log.i("ResponseFail: ", "${response.code}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.i("ResponseTotalFail: ", "${e.message}")
            }
        })

    }

    fun getDomain(url: String): String {
        val arr =
            url.split("//".toRegex(), 2).toTypedArray()
        val firstWord = arr[0]
        val theRest = arr[1]
        Log.i("RequestsssssFirst", firstWord)
        Log.i("RequestsssssSecond", theRest)

        val arr2 =
            theRest.split("/".toRegex(), 2).toTypedArray()
        val domainName = arr2[0]

        Log.i("Requestsssss2", domainName)

        if (url.contains(domainName, true)) {
            Log.i("RequestssIt does", "Yess")
        } else {
            Log.i("RequestssIt doesnt", "no")
        }
        return domainName
    }

    fun removeLastChars(str: String, chars: Int): String? {
        return str.substring(0, str.length - chars)
    }
}