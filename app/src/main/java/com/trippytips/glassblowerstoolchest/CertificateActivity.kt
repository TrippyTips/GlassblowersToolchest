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
      //  setSupportActionBar(toolbar)


        btnCertGenerate.setOnClickListener {
          val Title = etCertTitle.text.toString()
          val Artist = etCertName.text.toString()
          val Date = etCertDateCreated.text.toString()
          val Size = etCertSize.text.toString()
          val Weight = etCertWeight.text.toString()
          val Technique = etCertTechnique.text.toString()
          val SignatureReadsAs = etCertSignatureReadsAs.text.toString()
          val OGOwner = etOriginalOwner.text.toString()
            Toast.makeText(this, "$Title $Artist $Date $Size $Weight $Technique $SignatureReadsAs $OGOwner", Toast.LENGTH_LONG).show()
            val htmlTable = "<style type=\"text/css\">\n" +
                    ".tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}\n" +
                    ".tg td{font-family:Arial, sans-serif;font-size:14px;padding:14px 20px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}\n" +
                    ".tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:14px 20px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}\n" +
                    ".tg .tg-ts09{background-color:#cccccc;border-color:#cccccc;text-align:left;vertical-align:top}\n" +
                    ".tg .tg-2be4{font-size:20px;font-family:Impact, Charcoal, sans-serif !important;;background-color:#c0c0c0;border-color:#c0c0c0;text-align:center;vertical-align:top}\n" +
                    ".tg .tg-pnhl{background-color:#c0c0c0;border-color:#c0c0c0;text-align:left;vertical-align:top}\n" +
                    ".tg .tg-5b55{background-color:#9b9b9b;border-color:#9b9b9b;text-align:left;vertical-align:top}\n" +
                    ".tg .tg-e20j{background-color:#9b9b9b;color:#000000;border-color:#9b9b9b;text-align:left;vertical-align:top}\n" +
                    ".tg .tg-gl8g{font-weight:bold;font-size:22px;font-family:\"Times New Roman\", Times, serif !important;;background-color:#e5e5e5;border-color:#656565;text-align:center;vertical-align:top}\n" +
                    ".tg .tg-wtjf{font-weight:bold;font-family:Georgia, serif !important;;background-color:#cccccc;border-color:#cccccc;text-align:left;vertical-align:top}\n" +
                    ".tg .tg-waxd{font-family:Georgia, serif !important;;background-color:#cccccc;border-color:#cccccc;text-align:right;vertical-align:top}\n" +
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
                    "    <td class=\"tg-gl8g\" colspan=\"2\">$Title</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-2be4\" colspan=\"2\">by $Artist</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-wtjf\">Created on </td>\n" +
                    "    <td class=\"tg-waxd\">$Date</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-wtjf\">Size</td>\n" +
                    "    <td class=\"tg-waxd\">$Size</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-wtjf\">Weight</td>\n" +
                    "    <td class=\"tg-waxd\">$Weight</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-wtjf\">Technique Used</td>\n" +
                    "    <td class=\"tg-waxd\">$Technique</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-wtjf\">Origin</td>\n" +
                    "    <td class=\"tg-waxd\">$OGOwner</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-wtjf\">Signature Reads</td>\n" +
                    "    <td class=\"tg-waxd\">$SignatureReadsAs<br></td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-ts09\" colspan=\"2\"><span style=\"font-weight:bold\">Signature _______________________________________________Date_______________</span></td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-pnhl\" colspan=\"2\"></td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"tg-5b55\" colspan=\"4\"></td>\n" +
                    "  </tr>\n" +
                    "</table>"
/*
            val intent = Intent(this, SocialActivity::class.java)
            intent.putExtra("htmlTable", htmlTable)
            startActivity(intent)*/

            try {
                printWebView()
            }catch (e:Exception){
                Toast.makeText(this, "This feature requires a newer version of Android.\nA more compatible version is under development.", Toast.LENGTH_LONG).show()
            }
        }


        btnDate.setOnClickListener {
            try{
                getDate()
            }catch (e:Exception){
                Toast.makeText(this, "This feature requires a newer version of Android.\nA more compatible version is under development.", Toast.LENGTH_LONG).show()
            }
        }
        //configureWebView()
    }
    /* fun configureWebView() {

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

    fun getDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            // Display Selected date in textbox
            val month = month + 1
            etCertDateCreated.setText("$month/$dayOfMonth/$year")
        }, year, month, day)
        dpd.show()
        }else{
            Toast.makeText(this, "This feature requires a newer version of Android.\nA more compatible version is under Development.", Toast.LENGTH_LONG).show()
        }
    }

    fun printWebView() {
        val webView = WebView(this)
        val title = etCertTitle.text.toString()
        val artist = etCertName.text.toString()
        val date = etCertDateCreated.text.toString()
        val size = etCertSize.text.toString()
        val weight = etCertWeight.text.toString()
        val technique = etCertTechnique.text.toString()
        val sigreadsas = etCertSignatureReadsAs.text.toString()
        val ogowner = etOriginalOwner.text.toString()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                    return false
                }

                override fun onPageFinished(view: WebView, url: String) {

                    createWebPrintJob(view)
                    myWebView = null
                }
            }
        val htmlDocument = "<html><body><h1>$title</h1><p>" +
                "<h2>by $artist</h2><p>" +
                "Created on $date<p>" +
                "Size: $size<p>" +
                "Weight: $weight<p>" +
                "Technique Used: $technique<p>" +
                "Signed: $sigreadsas<p>" +
                "Original Owner: $ogowner</body></html>"
        val htmlTable = "<style type=\"text/css\">\n" +
                ".tg  {border-collapse:collapse;border-spacing:0;border-color:#ccc;}\n" +
                ".tg td{font-family:Arial, sans-serif;font-size:14px;padding:14px 20px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff;}\n" +
                ".tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:14px 20px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0;}\n" +
                ".tg .tg-ts09{background-color:#cccccc;border-color:#cccccc;text-align:left;vertical-align:top}\n" +
                ".tg .tg-2be4{font-size:20px;font-family:Impact, Charcoal, sans-serif !important;;background-color:#c0c0c0;border-color:#c0c0c0;text-align:center;vertical-align:top}\n" +
                ".tg .tg-pnhl{background-color:#c0c0c0;border-color:#c0c0c0;text-align:left;vertical-align:top}\n" +
                ".tg .tg-5b55{background-color:#9b9b9b;border-color:#9b9b9b;text-align:left;vertical-align:top}\n" +
                ".tg .tg-e20j{background-color:#9b9b9b;color:#000000;border-color:#9b9b9b;text-align:left;vertical-align:top}\n" +
                ".tg .tg-gl8g{font-weight:bold;font-size:22px;font-family:\"Times New Roman\", Times, serif !important;;background-color:#e5e5e5;border-color:#656565;text-align:center;vertical-align:top}\n" +
                ".tg .tg-wtjf{font-weight:bold;font-family:Georgia, serif !important;;background-color:#cccccc;border-color:#cccccc;text-align:left;vertical-align:top}\n" +
                ".tg .tg-waxd{font-family:Georgia, serif !important;;background-color:#cccccc;border-color:#cccccc;text-align:right;vertical-align:top}\n" +
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
                "    <td class=\"tg-5b55\" colspan=\"4\"></td>\n" +
                "  </tr>\n" +
                "</table>"
        webView.loadDataWithBaseURL(null, htmlTable, "text/HTML", "UTF-8", null)
        myWebView = webView
        }



    private fun createWebPrintJob(webView: WebView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val printManager = this
            .getSystemService(Context.PRINT_SERVICE) as PrintManager
        val Title = etCertTitle.text.toString()
        val printAdapter = webView.createPrintDocumentAdapter("COA_$Title")

        val jobName = getString(R.string.app_name) + " COA"

            printManager.print(
                jobName, printAdapter,
                PrintAttributes.Builder().build()
            )
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
