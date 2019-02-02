package com.trippytips.glassblowerstoolchest

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_marble_calc.*
import kotlinx.android.synthetic.main.marble_calc_child.view.*
import kotlinx.android.synthetic.main.specify_marble_dialog.*
import kotlinx.android.synthetic.main.new_schedule_dialog.*
import java.lang.NullPointerException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt

class MarbleCalcActivity : AppCompatActivity() {

    lateinit var rvCalculationResults:RecyclerView
    var marblecalculations:MutableList<String> = ArrayList()

    lateinit var marbleslider : SeekBar
    lateinit var marblevalue : TextView

    lateinit var rodslider : SeekBar
    lateinit var rodvalue : TextView
    var thickness = ""
    var useStd = true
    var glassCOE = 33


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marble_calc)

        //Set the context
        val context = this

        //Assign a value to the Marble Value TV
        marblevalue = findViewById(R.id.tvMarbleValue) as TextView

        //Get the SharedPreferences
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        //Set the COE from SharedPreferences
        val COE = prefs.getString("pref_coe","33")
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        glassCOE = COE.toInt()

        //Set the measurements to Standard or Metric
        val stdmet = prefs.getBoolean("stdmet", true)
        useStd = stdmet
        if(!useStd){
            marblevalue.text = getString(R.string.MCMetricStarter)
            Toast.makeText(context,"The preferences are set to Metric.",Toast.LENGTH_LONG).show()
        }

        loadData()

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP

        //Open Anneal-o-Matic and Pass the Thickness Value
        btn_Anneal.setOnClickListener {
            val intent = Intent(this, AnnealActivity::class.java)
            intent.putExtra("GlassThickness", thickness)
            try{
                startActivity(intent)
                } catch(e:Exception) {
                e.printStackTrace()
            }

        }

        //Set up the RecyclerView to display the results
        rvCalculationResults = findViewById(R.id.rvCalculationResults) as RecyclerView
        //rvCalculationResults.layoutManager = LinearLayoutManager(this)
        rvCalculationResults.layoutManager = GridLayoutManager(this,2)
        rvCalculationResults.adapter = MarbleCalculationsAdapter(marblecalculations,this)


        //Calculate and display the marble diameter in the TextView, tell the data to load,
        // and tell the RecyclerView to refresh when the progress moves.
        marbleslider = findViewById(R.id.sb_MarbleSlider) as SeekBar
        marblevalue = findViewById(R.id.tvMarbleValue) as TextView


        //Set up the Marble Slider
        marbleslider.max = 100 //default is 100
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                marbleslider.min = 1
            }

        //Tell the Marble Slider what to do when it is changed.
        marbleslider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val formatProgress  = progress.toDouble() / 10

            if (!useStd){
                marblevalue.text = df.format((formatProgress * 2.54)) + " cm Marble Size"
            }else {
                marblevalue.text = formatProgress.toString() + " inch Marble Size"
            }
            marblecalculations.clear()
            loadData()
            rvCalculationResults.adapter?.notifyDataSetChanged()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            marbleslider.max = 100
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
        })


         //Set up the Rod Size Slider
        rodslider = findViewById(R.id.sb_RodSlider) as SeekBar
        rodvalue = findViewById(R.id.tvRodValue) as TextView
        rodslider.max = 27 //Starts at 0, there are 28 standard rod sizes.

        //Set the list of readily available rod diameters to be displayed in the TextView,
        val rodSizeList = listOf("3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "12.7", "13", "14", "15", "15.9", "16", "18", "19", "20", "22", "24", "25.4", "26", "28", "30", "31.7", "38", "44")

        //When the Rod Size Slider Changes Set the textbox, calculate the data, and refresh the RecyclerView Adapter
        rodslider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rodvalue.text = rodSizeList[progress] + " mm Rod Size"
                marblecalculations.clear()
                loadData()
                rvCalculationResults.adapter?.notifyDataSetChanged()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        //Open Specify Marble Size Dialog When the Specify button is Clicked.
        btn_Specify_Marble.setOnClickListener {

            //Specify view values for the Dialog
            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.specify_marble_dialog, null)
            val etnumber = dialogView.findViewById<EditText>(R.id.et_number_marble)
            var customMarbleDiameterString = "0.00"
            var customMarbleDiameterInt: Double

            //Set up the decimal formatter
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.HALF_UP

            //Change the custom diameter hint if preferences are set to metric
            if(!useStd){
                etnumber.hint = "Enter Desired Diameter (cm)"
            }

            //Set up the Dialog
            dialog.setView(dialogView)
            dialog.setCancelable(false)

            //What Happens When the Dialog's Specify Button is Pressed
            dialog.setPositiveButton("Specify") { dialogInterface: DialogInterface, i: Int ->

                //Get the diameter from the dialog if one is specified.
                try {
                    if(!useStd){
                        customMarbleDiameterString = df.format(etnumber.text.toString().toDouble() / 2.54).toString()
                        customMarbleDiameterInt = df.format(etnumber.text.toString().toDouble() / 2.54).toDouble()
                        Toast.makeText(baseContext,"Custom Marble Diameter is now $customMarbleDiameterString centimeters",Toast.LENGTH_SHORT).show()
                    }else{
                        customMarbleDiameterString = etnumber.text.toString()
                        customMarbleDiameterInt = etnumber.text.toString().toDouble()
                        Toast.makeText(baseContext,"Custom Marble Diameter is now $customMarbleDiameterString inches",Toast.LENGTH_SHORT).show()
                    }


                    sb_MarbleSlider.max = (customMarbleDiameterInt * 10).toInt()
                    sb_MarbleSlider.progress = (customMarbleDiameterInt * 10).toInt()

                //If No Value is Specified, Tell the user to Enter One.
                }catch (e:Exception){
                    Toast.makeText(context,"Please Enter a Valid Value.",Toast.LENGTH_SHORT).show()
                }
            }

            //Show The Dialog
            val customDialog = dialog.create()
            customDialog.show()
        }

    }

    //RecyclerView Adapter for Marble Calculations
    class MarbleCalculationsAdapter(items : List<String>,ctx:Context) : RecyclerView.Adapter<MarbleCalculationsAdapter.ViewHolder>(){

        var list = items
        var context = ctx

        override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.marble_calc_child,p0,false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(p0: ViewHolder, position: Int) {
            p0.name?.text = list[position]
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
            val name = v.marble_calc_result
        }
    }




    //Process the data, format for sharing, and add to the RecyclerView
    fun loadData() {

        //Set up an array of readily available rod sizes as Doubles for calculations.
        val rodSizeList = arrayOf(3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 12.7, 13.0, 14.0, 15.0, 15.9, 16.0, 18.0, 19.0, 20.0, 22.0, 24.0, 25.4, 26.0, 28.0, 30.0, 31.7, 38.0, 44.0)

        //Calculate the Standard & Metric information derived from the Marble & Rod size entered by the user.
        val marbleRadiusMetric = sb_MarbleSlider.progress.toDouble().div(20) * 25.4 //sb_MarbleSlider has a range of 1-100 and must be divided by 10 to get increments between .25 and 10.  Divide in half to get the proper radius (Thus .div(20))
        val rodRadiusMetric =rodSizeList[sb_RodSlider.progress] / 2.0
        val marbleVolumeMetric = (Math.pow(marbleRadiusMetric, 3.00) * 4/3 * Math.PI) //mm^3
        val marbleVolumeStandard = marbleVolumeMetric / 16387.064
        val lengthNeededFeet = marbleVolumeMetric / (Math.pow(rodRadiusMetric, 2.0) *  Math.PI) / 25.4 / 12.0
        val marbleWeightStandard = 0.0222153 * marbleVolumeMetric / 10 * 0.0022046
        val marbleWeightMetric = marbleWeightStandard * 453.592

        //Shorten the calculations for readability
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP

        //Make string outputs of calculations
        val marblesize = (sb_MarbleSlider.progress / 10.000).toString()
        thickness = marblesize
        val rodsize =  rodSizeList[sb_RodSlider.progress].toString()
        val strLengthNeededFeet = df.format(lengthNeededFeet).toString()
        val strLengthNeededInches = df.format(lengthNeededFeet * 12).toString()
        val strMarbleWeightStandard = df.format(marbleWeightStandard).toString()
        val strMarbleWeightMetric = df.format(marbleWeightMetric).toString()
        val strMarbleVolumeStandard = df.format(marbleVolumeStandard).toString()
        val strMarbleVolumeMetric = df.format(marbleVolumeMetric).toString()

        //Display the information derived on the RecyclerView


        if(!useStd){
            //Metric

            //Calculating For...
            marblecalculations.add("Calculating for\n" + df.format(marblesize.toDouble() * 2.54).toString()  + " cm Marble")

            //Made from...
            marblecalculations.add( "Made from\n" + rodsize + "mm Diameter Rod")

            //Length Needed...
            marblecalculations.add("Length Needed")
            marblecalculations.add(df.format(strLengthNeededInches.toDouble() *2.54) + " cm")

            //Weight...
            marblecalculations.add("Weight")
            marblecalculations.add(df.format(strMarbleWeightStandard.toDouble() * .453592) + " kg\n$strMarbleWeightMetric grams")

            //Volume...
            marblecalculations.add("Volume")
            marblecalculations.add(df.format(strMarbleVolumeMetric.toDouble()/1000) + " mL" )

        }else {
            //Standard

            //Calculating For...
            marblecalculations.add("Calculating for\n$marblesize Inch Marble")

            //Made from...
            marblecalculations.add( "Made from\n" + rodsize + "mm Diameter Rod")

            //Length Needed...
            marblecalculations.add("Length Needed")
            marblecalculations.add("$strLengthNeededFeet Feet \n($strLengthNeededInches Inches)")

            //Weight...
            marblecalculations.add("Weight")
            marblecalculations.add("$strMarbleWeightStandard lb\n$strMarbleWeightMetric grams")

            //Volume...
            marblecalculations.add("Volume")
            marblecalculations.add(df.format(strMarbleVolumeStandard.toDouble() * 0.55411) + " us fl oz" )
        }

        //Change the Size of the Marble Images When the Marble slider moves
        val ivMarbleImage : ImageView = findViewById(R.id.iv_Marble)
        val paramsMarble = ivMarbleImage.getLayoutParams() as ViewGroup.LayoutParams
        paramsMarble.width = marbleRadiusMetric.roundToInt() * 7
        paramsMarble.height = marbleRadiusMetric.roundToInt() * 7
        ivMarbleImage.setLayoutParams(paramsMarble)

        //Change the Size of the Rod Image when the Rod Size slider moves
        val ivRodImage : ImageView = findViewById(R.id.iv_Rod)
        val paramsRod = iv_Rod.getLayoutParams() as ViewGroup.LayoutParams
        paramsRod.width = rodRadiusMetric.roundToInt() * 7
        paramsRod.height = rodRadiusMetric.roundToInt() * 7
        ivRodImage.setLayoutParams(paramsRod)

        //Create the string that will be shared when the user clicks the share button
        val readableMarbleInfo =
                        """
            MARBLE SIZE
            $marblesize inch

            ROD SIZE
            $rodsize mm

            You will need
            $strLengthNeededFeet feet
            or...
            $strLengthNeededInches inches
            of rod.

            The weight will be
            $strMarbleWeightStandard lb
            or...
            $strMarbleWeightMetric grams

            The volume will be
            $strMarbleVolumeStandard inches³
            or...
            $strMarbleVolumeMetric mm³

            Information Derived from the
            Glassblower's Toolchest App (Beta)
            By Bert Langan"""


        //Use an implicit intent to share the above string to other apps when the button is pressed
        btnShare.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, readableMarbleInfo)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Where would you like to share?"))
        }

    }

}
