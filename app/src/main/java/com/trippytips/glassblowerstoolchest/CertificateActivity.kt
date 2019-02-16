package com.trippytips.glassblowerstoolchest
import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebResourceRequest
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_certificate.*

class CertificateActivity : AppCompatActivity() {

    private var myWebView: WebView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate)

        //When the Generate Button is Clicked
        btnCertGenerate.setOnClickListener {

            //Set values to what is written in the EditTexts
            val Title = etCertTitle.text.toString()
            val Artist = etCertName.text.toString()
            val Date = etCertDateCreated.text.toString()
            val Size = etCertSize.text.toString()
            val Weight = etCertWeight.text.toString()
            val Technique = etCertTechnique.text.toString()
            val SignatureReadsAs = etCertSignatureReadsAs.text.toString()
            val OGOwner = etOriginalOwner.text.toString()


            //Call the printWebView Function or explain why
            // it cannot be called to users without compatible Android Versions
            try {
                printWebView()
            }catch (e:Exception){
                Toast.makeText(this, "This feature requires a newer version of Android.\nA more compatible version is under development.", Toast.LENGTH_LONG).show()
            }
        }

        //When the Date Button is Clicked call the getDate function or
        //explain why it cannot be called to users without compatible Android Versions
        btnDate.setOnClickListener {
            try{
                getDate()
            }catch (e:Exception){
                Toast.makeText(this, "This feature requires a newer version of Android.\nA more compatible version is under development.", Toast.LENGTH_LONG).show()
            }
        }


        //configureWebView()
    }


    //Possible future Workaround for users with lower Android Versions
    /*
    fun configureWebView() {

        myWebView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest): Boolean {
                return super.shouldOverrideUrlLoading(
                    view, request)
            }
        }
        myWebView?.settings?.javaScriptEnabled = true
        myWebView?.loadUrl(
            "https://developer.android.com/google/index.html")

    }
    */

    //Get the date when called.
    fun getDate() {
        //Make sure user has Android N or Higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //Set the Values for the Calender
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            //Set up the Date Picker Dialog
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                val month = month + 1 //Add one to the month because they start at 0

                // Display Selected date in textbox
                etCertDateCreated.setText("$month/$dayOfMonth/$year")
            }, year, month, day)

            //Show the Date Picker Dialog
            dpd.show()

         //If the user has a version older than Android N
        }else{
            Toast.makeText(this, "This feature requires a newer version of Android.\nA more compatible version is under Development.", Toast.LENGTH_LONG).show()
        }
    }


    //Create a WebView to be printed from the values entered in the EditTexts
    fun printWebView() {

        //Assign a Value the WebView to be created
        val webView = WebView(this)

        //Assign Values to the EditTexts Contents
        val title = etCertTitle.text.toString()
        val artist = etCertName.text.toString()
        val date = etCertDateCreated.text.toString()
        val size = etCertSize.text.toString()
        val weight = etCertWeight.text.toString()
        val technique = etCertTechnique.text.toString()
        val sigreadsas = etCertSignatureReadsAs.text.toString()
        val ogowner = etOriginalOwner.text.toString()

        //When the page is finished call the createWebPrintJob
        // function and afterwards nullify the myWebView Value
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                    return false
                }

                override fun onPageFinished(view: WebView, url: String) {

                    createWebPrintJob(view)

                    myWebView = null
                }
            }


        //Assign a value to Formatted Certificate
        val htmlTable = "<html>" +
                "<body>" +
                "<center>" +
                "<style type=\"text/css\">\n" +
                ".tg  {border-collapse:collapse;border-spacing:1;border-color:#80c0c0c0;}\n" +
                ".tg td{font-family:Arial, sans-serif;font-size:14px;padding:14px 20px;overflow:hidden;word-break:normal;color:#333;background-color:#fff;}\n" +
                //Top/Side Block Style
                ".tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:14px 20px;overflow:hidden;word-break:normal}\n" +
                //Signature Style
                ".tg .tg-ts09{background-color:#e5e5e5;border-color:#cccccc;text-align:left;vertical-align:top}\n" +
                //By Style
                ".tg .tg-2be4{font-size:20px;font-family:Impact, Charcoal, sans-serif !important;;background-color:#80c0c0c0;border-color:#80c0c0c0;text-align:center;vertical-align:top}\n" +
                ".tg .tg-pnhl{background-color:#80c0c0c0;border-color:#80c0c0c0;text-align:left;vertical-align:top}\n" +
    //            ".tg .tg-5b55{background-color:#809b9b9b;border-color:#9b9b9b;text-align:left;vertical-align:top}\n" +
//                ".tg .tg-e20j{background-color:#809b9b9b;color:#000000;border-color:#9b9b9b;text-align:left;vertical-align:top}\n" +
                //Title style
                ".tg .tg-gl8g{font-weight:bold;font-size:22px;font-family:\"Times New Roman\", Times, serif !important;;background-color:#e5e5e5;border-color:#656565;text-align:center;vertical-align:top}\n" +
//                ".tg .tg-wtjf{font-weight:bold;font-family:Georgia, serif !important;;background-color:#cccccc;border-color:#cccccc;text-align:left;vertical-align:top}\n" +
//                ".tg .tg-waxd{font-family:Georgia, serif !important;;background-color:#cccccc;border-color:#cccccc;text-align:right;vertical-align:top}\n" +
                ".tg{background:url(file:///android_asset/Golden1.png);background-repeat:no-repeat;background-size:100% 100%;}" +
                "</style>\n" +
                "<table class=\"tg\">\n" +
                "  <tr>\n" +
                "    <th class=\"tg-e20j\" rowspan=\"13\"></th>\n" +
                "    <th class=\"tg-5b55\" colspan=\"4\"></th>\n" +
                "    <th class=\"tg-5b55\" rowspan=\"13\"></th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-pnhl\" rowspan=\"11\"></td>\n" +
                "    <td class=\"tg-pnhl\" colspan=\"2\"></td>\n" +
                "    <td class=\"tg-pnhl\" rowspan=\"11\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-gl8g\" colspan=\"2\">$title</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-2be4\" colspan=\"2\">by $artist</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-wtjf\">Created on </td>\n" +
                "    <td class=\"tg-waxd\">$date</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-wtjf\">Size</td>\n" +
                "    <td class=\"tg-waxd\">$size</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-wtjf\">Weight</td>\n" +
                "    <td class=\"tg-waxd\">$weight</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-wtjf\">Technique Used</td>\n" +
                "    <td class=\"tg-waxd\">$technique</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-wtjf\">Origin</td>\n" +
                "    <td class=\"tg-waxd\">$ogowner</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-wtjf\">Signature Reads</td>\n" +
                "    <td class=\"tg-waxd\">$sigreadsas<br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-ts09\" colspan=\"2\"><span style=\"font-weight:bold\">Signature _______________________________________________Date_______________</span></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td class=\"tg-pnhl\" colspan=\"2\"></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <th class=\"tg-e20j\" colspan=\"4\"></td>\n" +
                "  </tr>\n" +
                "</table>" +
                "</body>" +
                "</html>"

        //Load the data in to webView
        webView.loadDataWithBaseURL(null, htmlTable, "text/HTML", "UTF-8", null)

        //Make sure the createWebPrintJob can read the loaded webview
        myWebView = webView
    }


    //Process myWebView as a Print Job
    private fun createWebPrintJob(webView: WebView) {

        //Make sure the user is using Android L or greater
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            //Set a Value to the PrintManager
            val printManager = this.getSystemService(Context.PRINT_SERVICE) as PrintManager

            //Set up the print
            val Title = etCertTitle.text.toString()
            val printAdapter = webView.createPrintDocumentAdapter("COA_$Title")
            val jobName = getString(R.string.app_name) + " COA"

            //Print
            printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())

        //Show the User that they cannot print due to an Android version of M or Lower
        }else{
            Toast.makeText(this, "This feature requires a newer version of Android.\nA more compatible version is under Development.", Toast.LENGTH_LONG).show()
        }
    }





















    //companion object {
/*
        fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(0, 0, v.layoutParams.width, v.layoutParams.height)
            v.draw(c)
            return b
        }

        fun saveImage(bitmap: Bitmap) {
            val root = Environment.getExternalStorageDirectory().toString()
            val myDir = File(root + "/certificates")
            myDir.mkdirs()
            val generator = Random()
            var n = 10000
            n = generator.nextInt(n)
            val fname = "Certificate-$n.jpg"
            val file = File(myDir, fname)
            // Log.i(TAG, "" + file);
            if (file.exists())
                file.delete()
            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }*/
}
