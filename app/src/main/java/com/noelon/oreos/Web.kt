package com.noelon.oreos

import android.text.TextUtils
import android.util.Log
import java.io.*
import java.net.CookieManager
import java.net.HttpCookie
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


object WebService {
    private const val COOKIES_HEADER = "Set-Cookie"
    const val COOKIE = "Cookie"
    var msCookieManager: CookieManager = CookieManager()

    var responseCode = 0
        set(responseCode) {
            field = responseCode
            Log.i("Milad", "responseCode$responseCode")
        }

    fun sendPost(requestURL: String?, urlParameters: String?): String? {
        val url: URL
        var response: String? = ""
        try {
            url = URL(requestURL)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.setReadTimeout(15000)
            conn.setConnectTimeout(15000)
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            if (msCookieManager.getCookieStore().getCookies().size > 0) {
                conn.setRequestProperty(
                    COOKIE,
                    TextUtils.join(";", msCookieManager.cookieStore.cookies)
                )
            }
            conn.doInput = true
            conn.doOutput = true
            val os: OutputStream = conn.getOutputStream()
            val writer = BufferedWriter(
                OutputStreamWriter(os, "UTF-8")
            )
            if (urlParameters != null) {
                writer.write(urlParameters)
            }
            writer.flush()
            writer.close()
            os.close()
            val headerFields: Map<String, List<String>> =
                conn.headerFields
            val cookiesHeader =
                headerFields[COOKIES_HEADER]
            if (cookiesHeader != null) {
                for (cookie in cookiesHeader) {
                    msCookieManager.cookieStore
                        .add(null, HttpCookie.parse(cookie)[0])
                    //todo: Log and check for cookies
                }
            }
            responseCode = conn.responseCode
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                var line: String?
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                while (br.readLine().also { line = it } != null) {
                    response += line
                }
            } else {
                response = ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    // HTTP GET request
    @Throws(Exception::class)
    fun sendGet(url: String?): String {
        val obj = URL(url)
        val con: HttpURLConnection = obj.openConnection() as HttpURLConnection

        // optional default is GET
        con.requestMethod = "GET"

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla")
        /*
    * https://stackoverflow.com/questions/16150089/how-to-handle-cookies-in-httpurlconnection-using-cookiemanager
    * Get Cookies form cookieManager and load them to connection:
     */if (msCookieManager.cookieStore.cookies.size > 0) {
            //While joining the Cookies, use ',' or ';' as needed. Most of the server are using ';'
            con.setRequestProperty(
                COOKIE,
                TextUtils.join(";", msCookieManager.getCookieStore().getCookies())
            )
        }

        /*
    * https://stackoverflow.com/questions/16150089/how-to-handle-cookies-in-httpurlconnection-using-cookiemanager
    * Get Cookies form response header and load them to cookieManager:
     */
        val headerFields: Map<String, List<String>> =
            con.headerFields
        val cookiesHeader =
            headerFields[COOKIES_HEADER]
        if (cookiesHeader != null) {
            for (cookie in cookiesHeader) {
                msCookieManager.getCookieStore()
                    .add(null, HttpCookie.parse(cookie).get(0))
            }
        }
        val responseCode: Int = con.responseCode
        val `in` = BufferedReader(
            InputStreamReader(con.inputStream)
        )
        var inputLine: String? = null
        val response = StringBuffer()
        while (`in`.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        `in`.close()
        return responseCode.toString()
    }

}