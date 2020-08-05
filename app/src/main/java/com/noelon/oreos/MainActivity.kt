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

        Log.i("ManagerCreate: ", "${webManager.hasCookies()}")

        web_view.settings.javaScriptEnabled = true
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (reload.text == "1") {
                    Log.i("CookieStringclearOne: ", "hereeee")
                    cookieString = webManager.getCookie(url)
                }

                cookieList1 = getCookie(url!!)
                Log.i("Array: ", cookieList1?.get(0).toString())


                if (reload.text == "2") {
                    Log.i("CookieStringclear: ", "hereeee")
                    cookieStringTwo = webManager.getCookie(url)
                }
//                getCookies(url!!)
//                Log.i("Jar: ", webManager.getCookie(url))


            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
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
            webManager.removeAllCookie()
            // web_view.reload()
            // Log.i("ManagerCookies: ", "$cookieList")


        }

        reload.setOnClickListener {
            // web_view.reload()
            // Log.i("CookieString: ", cookieString!!)
            //Log.i("CookieStringTwo: ", cookieStringTwo!!)
            val abc: MutableMap<String, String> = HashMap()
            val abc2: MutableMap<String, String> = HashMap()
            if (reload.text == "2") {
                if (cookieString != null) {
                    Log.i("CookieString: ", cookieString!!)

                    abc["Cookie"] = cookieString!!
//                    for (cookie in cookieList1!!) {
//                        webManager.setCookie(
//                            url,
//                            cookie
//                        )
//                    }
                    //  webManager.setCookie(url, cookieString)
                    //webManager.flush()
                    Log.i("ABC", abc.toString())

                }
                webManager.removeAllCookie()
                web_view.loadUrl(url, abc)

                reload.text = "1"

            } else {
                if (cookieStringTwo != null) {
                    Log.i("CookieStringTwo: ", cookieStringTwo!!)

                    abc2["Cookie"] = cookieStringTwo!!
//                    for (cookie in cookieList1!!) {
//                        webManager.setCookie(
//                            url,
//                            cookie
//                        )
//                    }
                    //  webManager.setCookie(url, cookieStringTwo)
                    // webManager.flush()
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
//                connection.content
//                val cookierJar = manager.cookieStore
//                Log.d("Cookies: ", cookierJar.cookies.toString())

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
                //Log.i("HeaderCook: ", "${manager.cookieStore.cookies}")

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