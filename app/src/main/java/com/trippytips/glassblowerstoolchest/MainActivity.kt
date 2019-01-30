package com.trippytips.glassblowerstoolchest

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlin.Exception
//class FeatureException(message: String) : Exception(message)
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //Open the Polariscope Activity
    fun openPolariscope(view: View) {
        val intent = Intent(this, PolariscopeActivity::class.java)
        try {
            startActivity(intent)
        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    //Open the Marble Calculator Activity
    fun openMarbleCalc(view: View) {
        val intent = Intent(this, MarbleCalcActivity::class.java)
        try {
        startActivity(intent)
        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    //Open the COA Generator Activity
    fun openCertMaker(view: View) {
        val intent = Intent(this, CertificateActivity::class.java)
        try{
        startActivity(intent)
        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    //Open Instagram
    fun openIG(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/bertlangan"))
        val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/bertlangan"))
        intent.setPackage("com.instagram.android")
        try{
            try {
                startActivity(intent)
            } catch (e: Exception) {
                startActivity(intent2)
            }
        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }

    //Open the Anneal-o-Matic Activity
    fun openAnneal(view: View) {
        val intent = Intent(this, AnnealActivity::class.java)
        try{
            startActivity(intent)

        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }

    //Open Settings
    fun openSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        try{
            startActivity(intent)

        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }
}
