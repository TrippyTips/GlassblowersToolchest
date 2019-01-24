package com.trippytips.glassblowerstoolchest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class SocialActivity : AppCompatActivity() {
    var bundle :Bundle ?=intent.extras
    var htmlTable: String = intent.getStringExtra("htmlTable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social)

        var wv = findViewById<WebView>(R.id.wv_Cert)

        wv.loadDataWithBaseURL(null, htmlTable, "text/HTML", "UTF-8", null)

    }
}
