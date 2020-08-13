package com.noelon.oreos

import android.annotation.SuppressLint
import android.content.Intent
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
        web_view.settings.domStorageEnabled = false

        web_view.settings.javaScriptEnabled = true
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                cookieString = webManager.getCookie(url)
                if (!prefs.getCookieState()) {
                    prefs.setCookieOne(cookieString)
                }






                if (reload.text == "2" && !isTwoFilled) {
                    cookieStringTwo = webManager.getCookie(url)
                }


            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {

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
            Log.i("Requestsssss", "Here")
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
}