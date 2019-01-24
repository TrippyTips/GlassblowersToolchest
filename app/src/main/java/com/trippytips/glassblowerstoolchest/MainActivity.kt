package com.trippytips.glassblowerstoolchest

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlin.Exception
class FeatureException(message: String) : Exception(message)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openPolariscope(view: View) {
        // Do something in response to button
        val intent = Intent(this, PolariscopeActivity::class.java)
        try {
            startActivity(intent)
        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Please Tell Bert the following error message: \n\n" + e.toString(),
                Toast.LENGTH_LONG).show()
        }
    }

    fun openMarbleCalc(view: View) {
        // Do something in response to button
        val intent = Intent(this, MarbleCalcActivity::class.java)
        try {
        startActivity(intent)
        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Please Tell Bert the following error message: \n\n" + e.toString(),
                Toast.LENGTH_LONG).show()
        }
    }

    fun openCertMaker(view: View) {
        // Do something in response to button
        val intent = Intent(this, CertificateActivity::class.java)
        try{
        startActivity(intent)
        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Please Tell Bert the following error message: \n\n" + e.toString(),
                Toast.LENGTH_LONG).show()
        }
    }

    //Open Instagram
    fun openIG(view: View) {
        //val uri = Uri.parse("http://instagram.com/_u/bertlangan")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/bertlangan"))
        val intent2 = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/bertlangan"))
        intent.setPackage("com.instagram.android")
        try{
            try {
                startActivity(intent)
            } catch (err: Exception) {
                startActivity(intent2)
            }
        } catch(e:Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Please Tell Bert the following error message: \n\n" + e.toString(),
                Toast.LENGTH_LONG).show()
        }

    }

    //Open Anneal-o-Matic
    fun openAnneal(view: View) {
        val intent = Intent(this, AnnealActivity::class.java)
        try{
            startActivity(intent)
            //throw FeatureException("Testing Testing, 1...2...3")

        } catch(e:Exception) {
            e.printStackTrace()
         //   Toast.makeText(this, "This feature has not yet been implemented.",
         //       Toast.LENGTH_LONG).show()
            Toast.makeText(this, "THIS FEATURE HAS NOT YET BEEN IMPLEMENTED: \n\n" + e.toString(),
                Toast.LENGTH_LONG).show()
        }

    }

    //Open Settings
    fun openSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        try{
            //startActivity(intent)
            throw FeatureException("Testing Testing, 1...2...3")

        } catch(e:Exception) {
            e.printStackTrace()
            //   Toast.makeText(this, "This feature has not yet been implemented.",
            //       Toast.LENGTH_LONG).show()
            Toast.makeText(this, "THIS FEATURE HAS NOT YET BEEN IMPLEMENTED: \n\n" + e.toString(),
                Toast.LENGTH_LONG).show()
        }

    }
}
