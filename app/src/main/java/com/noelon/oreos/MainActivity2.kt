package com.noelon.oreos

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    val webManager: CookieManager = CookieManager.getInstance()
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        webManager.removeAllCookie()
        val url = "https://www.google.com"
        web_view3.settings.javaScriptEnabled = true
        web_view3.webViewClient = object : WebViewClient() {


        }

        web_view3.loadUrl(url)


        but.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}