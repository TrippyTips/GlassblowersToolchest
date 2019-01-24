package com.trippytips.glassblowerstoolchest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_anneal.*
import kotlinx.android.synthetic.main.activity_polariscope.*

class PolariscopeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_polariscope)

        btnPolariscopeHelp.setOnClickListener {
        Toast.makeText(this, "Use the Screen as a back light for your glass while wearing polarised glasses to view stress.", Toast.LENGTH_LONG).show()
    }

    }
}
