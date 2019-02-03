package com.trippytips.glassblowerstoolchest

import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendPosition.BELOW_CHART_CENTER
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.activity_anneal.*
import java.io.File
import java.io.FileOutputStream
import java.math.RoundingMode
import java.text.DecimalFormat


class AnnealActivity : AppCompatActivity() {

    lateinit var preset: Spinner
    val schedule = ArrayList<rvKilnScheduleStep>()
    private var glassthickness = 0.00
    var importThickness = 0.00
    var thickness = glassthickness.toString()
    var id = 0
    var idSelected = 0
    var name: String = "Kiln Schedule"
    var ramp1: String = "-"
    var degrees1: String = "-"
    var hold1: String = "-"
    var ramp2: String = "-"
    var degrees2: String = "-"
    var hold2: String = "-"
    var ramp3: String = "-"
    var degrees3: String = "-"
    var hold3: String = "-"
    var ramp4: String = "-"
    var degrees4: String = "-"
    var hold4: String = "-"
    var ramp5: String = "-"
    var degrees5: String = "-"
    var hold5: String = "-"
    var spinnerSize = 0
    var roomtemp: Float = 70F
    var glassCOE = 33
    var useStd = true
    var units = "Standard"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anneal)

        //Load the preferences
        getSharedPrefs()


        //Set whether to display the data in Standard or Metric
        useStd = units == "Standard"
        Toast.makeText(context, "The preferences are set to $units.", Toast.LENGTH_LONG).show()


        //Import the schedule from Marble Calculator if applicable
        importSchedule()

        //Set up the spinner
        generateSpinner()


        cv_Load.performClick()


        //Send the Data to be Displayed in the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvKilnSchedule)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        rvKilnSchedule.adapter = rvKilnScheduleAdapter(schedule)


    }

    //Load the values with Preferences
    fun getSharedPrefs(){
        //Get the SharedPreferences
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        //Set the COE from SharedPreferences
        val COE = prefs.getString("pref_coe", "33")
        glassCOE = COE.toInt()
        //Set the measurements to Standard or Metric
        val stdmet = prefs.getBoolean("stdmet", true)
        useStd = stdmet

        if (!useStd) {
            roomtemp = fToC(roomtemp.toDouble()).toFloat()
            units = "Metric"
        }else{
            roomtemp = 70F
            units = "Standard"
        }
    }

    val context = this
    var db = ksDataBaseHandler(context)

    //Generate the spinner data
    fun generateSpinner() {
        //set 'preset' as spinner
        preset = findViewById(R.id.spin_Schedules)

        //set the options for the spinner
        val options = arrayListOf("Please Add a Schedule")
        options.clear()
        val data = db.readData()
        for (i in 0..(data.size - 1)) {
            options.add(i, data.get(i).name)
            options[i] = data.get(i).name
        }

        spinnerSize = data.size

        //A Toast to help debug
        //Toast.makeText(this,"data.size = " + data.size.toString() + "\noptions.size = " + options.size.toString() + "\nspinnerSize = $spinnerSize", Toast.LENGTH_LONG).show()

        if (data.size != options.size) {
            options.removeAt(options.size - 1)
        }
        if (id > options.size - 1) {
            id = options.size - 1
        }

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        preset.adapter = arrayAdapter


        if (id > 0) {
            preset.setSelection(id)
        }

        preset.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                ramp1 = "-"
                degrees1 = "-"
                hold1 = "-"
                ramp2 = "-"
                degrees2 = "-"
                hold2 = "-"
                ramp3 = "-"
                degrees3 = "-"
                hold3 = "-"
                ramp4 = "-"
                degrees4 = "-"
                hold4 = "-"
                ramp5 = "-"
                degrees5 = "-"
                hold5 = "-"
                spinnerSize = 0
                cv_Load.performClick()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                idSelected = position

                //A Toast to help debug
                //Toast.makeText(context, options[position] + " Selected.  (idSelected = $idSelected)", Toast.LENGTH_SHORT).show()

                ramp1 = "-"
                degrees1 = "-"
                hold1 = "-"
                ramp2 = "-"
                degrees2 = "-"
                hold2 = "-"
                ramp3 = "-"
                degrees3 = "-"
                hold3 = "-"
                ramp4 = "-"
                degrees4 = "-"
                hold4 = "-"
                ramp5 = "-"
                degrees5 = "-"
                hold5 = "-"

                cv_Load.performClick()
            }
        }
    }


    //Import from Marble Calculator if Possible
    fun importSchedule() {
        //Format Data For Display
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP

        try {
            val bundle: Bundle? = intent.extras
            @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            importThickness = bundle!!.getString("GlassThickness").toDouble()
            thickness = df.format(importThickness)
            glassthickness = df.format(importThickness).toDouble()

            if(!useStd){
                name = df.format(thickness.toDouble()*2.54).toString() + " cm Marble [COE $glassCOE]"
            }else{
                name = "$thickness Inch Marble [COE $glassCOE]"
            }

            calculateschedule()
            id = db.readData().size
            saveschedule()
            Toast.makeText(context, "Successfully Imported Size From Marble Calculator", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    //Create a New Schedule
    fun newschedule(view: View) {
        //Load the preferences
        getSharedPrefs()

        //Format Data For Display
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP

        val spinner = findViewById<Spinner>(R.id.spin_Schedules)
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.new_schedule_dialog, null)
        val etnumber = dialogView.findViewById<EditText>(R.id.et_number_thickness)
        val etScheduleName = dialogView.findViewById<EditText>(R.id.etScheduleName)

        useStd = units == "Standard"
        if(!useStd){
            etnumber.hint = "Enter Desired Thickness (cm)"
        }
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Specify") { dialogInterface: DialogInterface, i: Int ->
            try {
                val customThicknessString:String
                if(!useStd){
                    customThicknessString = df.format(etnumber.text.toString().toDouble() / 2.54)
                    glassthickness = df.format(etnumber.text.toString().toDouble() / 2.54).toDouble()
                    thickness = df.format(etnumber.text.toString().toDouble() / 2.54)
                    units = "Metric"
                }else {
                    customThicknessString = etnumber.text.toString()
                    glassthickness = etnumber.text.toString().toDouble()
                    thickness = etnumber.text.toString()
                    units = "Standard"
                }
                name = etScheduleName.text.toString()
                Toast.makeText(
                    baseContext, "Custom Marble Diameter is now $customThicknessString\n" +
                            "Name of Schedule is now $name", Toast.LENGTH_LONG
                ).show()
                calculateschedule()
                if (spinnerSize >= db.readData().size) {
                    id = db.readData().size
                    //spinner.setSelection(id+1)
                }
                saveschedule()

                //If Nothing was Entered...
            } catch (e: Exception) {
                Toast.makeText(context, "Please Enter A Valid Name and Size.", Toast.LENGTH_LONG).show()
            }
        }
        val customDialog = dialog.create()
        customDialog.show()
    }


    //Derive the Data for the Annealing Schedule
    fun calculateschedule() {

        //Format Data For Display
        val df = DecimalFormat("#")
        df.roundingMode = RoundingMode.HALF_UP


        //Input Variables for Calculations

        val AnnealCoursness = 250

        //Perform Calculations and assign them to vals

        //Ramp 1 Calculations
        ramp1 =
            if (60 * ((1.08 * 1000) / ((glassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))) <= 1798) {

                df.format(60 * ((1.08 * 1000) / ((glassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))))

            } else {

                "FULL"
            }

        //Degrees 1 Calculation
        degrees1 =
            when (glassCOE) {
                33 -> "1058"
                90 -> "980"
                96 -> "950"
                104 -> "970"
                else -> "?"
            }

        //Hold 1 Calculation
        hold1 = df.format(30 + (4 * (Math.pow(glassthickness.toDouble(), 2.00))))

        //Ramp 2 Calculation
        ramp2 =
            if (60 * ((.54 * 250) / ((glassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))) <= 1798) {

                df.format(60 * ((.54 * 250) / ((glassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))))

            } else {

                "FULL"
            }

        //Degrees 2 Calculation
        degrees2 =
            when (glassCOE) {
                33 -> "950"
                90 -> "750"
                96 -> "700"
                104 -> "680"
                else -> "?"
            }

        //Hold 2 Calculation
        hold2 = df.format(15 + (2 * (Math.pow(glassthickness.toDouble(), 2.00))))

        //Ramp 3 Calculation
        ramp3 =
            if (60 * ((.54 * 750) / ((glassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))) <= 1798) {

                df.format(60 * ((.54 * 750) / ((glassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))))

            } else {

                "FULL"
            }

        degrees3 = df.format(roomtemp)
        hold3 = "0"
        ramp4 = "0"
        degrees4 = "0"
        hold4 = "0"
        ramp5 = "0"
        degrees5 = "0"
        hold5 = "0"


        //Set whether to display the data in Standard or Metric
        useStd = units == "Standard"
        Toast.makeText(context, "The preferences are set to $units.\n calculateSchedule()", Toast.LENGTH_LONG).show()



        //If the preference is metric, and numeric convert the hold temps to metric
        if(!useStd){
            if (isNumeric(degrees1)){degrees1 = fToC(degrees1)}
            try{if(degrees1.toDouble() < 0) {degrees1 = "0"}}catch (e: Exception){e.printStackTrace()}
            if (isNumeric(degrees2)){degrees2 = fToC(degrees2)}
            try{if(degrees2.toDouble() < 0) {degrees2 = "0"}}catch (e: Exception){e.printStackTrace()}
            try{if (isNumeric(degrees3)){degrees3 = fToC(degrees3)}
            if(degrees3.toDouble() < 0) {degrees3 = "0"}}catch (e: Exception){e.printStackTrace()}
            try{if (isNumeric(degrees4)){degrees4 = fToC(degrees4)}
            if(degrees4.toDouble() < 0) {degrees4 = "0"}}catch (e: Exception){e.printStackTrace()}
            try{if (isNumeric(degrees5)){degrees5 = fToC(degrees5)}
            if(degrees5.toDouble() < 0) {degrees5 = "0"}}catch (e: Exception){e.printStackTrace()}

            //Convert the ramps
            if (isNumeric(ramp1)){ramp1 = fRateToCRate(ramp1)}
            try{if (ramp1.toDouble() < 0){ramp1 = "0"}}catch (e: Exception){e.printStackTrace()}
            if (isNumeric(ramp2)){ramp2 = fRateToCRate(ramp2)}
            try{if (ramp2.toDouble() < 0){ramp2 = "0"}}catch (e: Exception){e.printStackTrace()}
            if (isNumeric(ramp3)){ramp3 = fRateToCRate(ramp3)}
            try{if (ramp3.toDouble() < 0){ramp3 = "0"}}catch (e: Exception){e.printStackTrace()}
            if (isNumeric(ramp4)){ramp4 = fRateToCRate(ramp4)}
            try{if (ramp4.toDouble() < 0){ramp4 = "0"}}catch (e: Exception){e.printStackTrace()}
            if (isNumeric(ramp5)){ramp5 = fRateToCRate(ramp5)}
            try{if (ramp5.toDouble() < 0){ramp5 = "0"}}catch (e: Exception){e.printStackTrace()}
        }

        schedule.clear()
        if(!useStd) {
            //C
            schedule.add(rvKilnScheduleStep("RA1", ramp1, "°/Hour"))
            schedule.add(rvKilnScheduleStep("°C 1", degrees1, "°"))
            schedule.add(rvKilnScheduleStep("HLD1", hold1, "minutes"))

            schedule.add(rvKilnScheduleStep("RA2", ramp2, "°/Hour"))
            schedule.add(rvKilnScheduleStep("°C 2", degrees2, "°"))
            schedule.add(rvKilnScheduleStep("HLD2", hold2, "minutes"))

            schedule.add(rvKilnScheduleStep("RA3", ramp3, "°/Hour"))
            schedule.add(rvKilnScheduleStep("°C 3", degrees3, "°"))
            schedule.add(rvKilnScheduleStep("HLD3", hold3, "minutes"))

            schedule.add(rvKilnScheduleStep("RA4", ramp4, "°/Hour"))
            schedule.add(rvKilnScheduleStep("°C 4", degrees4, "°"))
            schedule.add(rvKilnScheduleStep("HLD4", hold4, "minutes"))
        } else {
            //F
            schedule.add(rvKilnScheduleStep("RA1", ramp1, "°/Hour"))
            schedule.add(rvKilnScheduleStep("°F 1", degrees1, "°"))
            schedule.add(rvKilnScheduleStep("HLD1", hold1, "minutes"))

            schedule.add(rvKilnScheduleStep("RA2", ramp2, "°/Hour"))
            schedule.add(rvKilnScheduleStep("°F 2", degrees2, "°"))
            schedule.add(rvKilnScheduleStep("HLD2", hold2, "minutes"))

            schedule.add(rvKilnScheduleStep("RA3", ramp3, "°/Hour"))
            schedule.add(rvKilnScheduleStep("°F 3", degrees3, "°"))
            schedule.add(rvKilnScheduleStep("HLD3", hold3, "minutes"))

            schedule.add(rvKilnScheduleStep("RA4", ramp4, "°/Hour"))
            schedule.add(rvKilnScheduleStep("°F 4", degrees4, "°"))
            schedule.add(rvKilnScheduleStep("HLD4", hold4, "minutes"))
        }
        rvKilnSchedule.adapter?.notifyDataSetChanged()

        //Generate the Chart
        generateChart()


    }


    //Generate a chart to represent the schedule
    @Suppress("UsePropertyAccessSyntax")
    fun generateChart() {
        //If the ramps are set to "FULL" change them to "1798"
        try {
            ramp1.toFloat()
        } catch (e: NumberFormatException) {
            ramp1 = if(!useStd){
                fRateToCRate("1798")
            }else{
                "1798"
            }
        }
        try {
            ramp2.toFloat()
        } catch (e: NumberFormatException) {
            ramp2 = if(!useStd){
                fRateToCRate("1798")
            }else{
                "1798"
            }
        }
        try {
            ramp3.toFloat()
        } catch (e: NumberFormatException) {
            ramp3 = if(!useStd){
                fRateToCRate("1798")
            }else{
                "1798"
            }
        }
        try {
            ramp4.toFloat()
        } catch (e: NumberFormatException) {
            ramp4 = if(!useStd){
                fRateToCRate("1798")
            }else{
                "1798"
            }
        }
        try {
            ramp5.toFloat()
        } catch (e: NumberFormatException) {
            ramp5 = if(!useStd){
                fRateToCRate("1798")
            }else{
                "1798"
            }
        }

        //Assign the chart to lineChartView
        val lineChartView = findViewById<LineChart>(R.id.line_chart)
        val dataVals = ArrayList<Entry>()

        //Define how the chart will look

        //Chart Description
        val chartDescription: Description = lineChartView.description
        chartDescription.text = ""
        chartDescription.setPosition(lineChartView.width * .55F, lineChartView.height * .8F)
        lineChartView.description = chartDescription

        //Legend
        var legend: Legend = lineChartView.legend
        legend.setPosition(BELOW_CHART_CENTER)
        legend.setForm(Legend.LegendForm.CIRCLE)
        legend.setTextColor(Color.WHITE)
        //legend colors are a work in progress
        //var legendLabels = arrayOf(degrees1, degrees2, degrees3, degrees4, degrees5)
        //legend.setCustom(ColorTemplate.LIBERTY_COLORS, new String[]{legendLabels})


        //No Data Text
        lineChartView.setNoDataText("Please Create a New Schedule.")
        lineChartView.setNoDataTextColor(Color.WHITE)

        //Grid & Borders
        lineChartView.setDrawGridBackground(true)
        lineChartView.setDrawBorders(true)
//      lineChartView.setBorderColor(Color.RED)
        lineChartView.setBorderWidth(1F)

        //X-Axis (Time)
        val xAxis = lineChartView.xAxis
        xAxis.textColor = Color.WHITE
        //xAxis.setAxisMinimum(0.5F)
        xAxis.setGranularity(0.5F)
        xAxis.setValueFormatter(XAxisValueFormatter())

        //Y-Axis (Temp)
        val yAxisL = lineChartView.getAxis(YAxis.AxisDependency.LEFT)
        val yAxisR = lineChartView.getAxis(YAxis.AxisDependency.RIGHT)
        yAxisR.setEnabled(false)
        yAxisL.setTextColor(Color.WHITE)
        yAxisL.setAxisMinimum(0F)
        if(!useStd){
            yAxisL.setAxisMaximum(750F)
            yAxisL.setGranularity(250F)
        }else {
            yAxisL.setAxisMaximum(1700F)
            yAxisL.setGranularity(500F)
        }
        yAxisL.setValueFormatter(YAxisValueFormatter())

        //Animations
        //lineChartView.animateXY(3000,3000, Easing.EasingOption.EaseOutBounce, Easing.EasingOption.EaseInOutBounce)
        lineChartView.animateY(3500, Easing.EasingOption.EaseOutBounce)

        //Calculate the coordinates and assign them to vals
        val chartx1 = 0F
        val charty1 = roomtemp
        val chartx2 = ((60 / ramp1.toFloat()) * (degrees1.toFloat() - charty1)) / 60F
        val charty2 = degrees1.toFloat()
        val chartx3 = chartx2 + (hold1.toFloat() / 60F)
        val charty3 = degrees1.toFloat()
        val chartx4 = chartx3 + (((60 / ramp2.toFloat()) * (charty3 - degrees2.toFloat())) / 60F)
        val charty4 = degrees2.toFloat()
        val chartx5 = chartx4 + (hold2.toFloat() / 60F)
        val charty5 = degrees2.toFloat()
        val chartx6 = chartx5 + (((60 / ramp3.toFloat()) * (charty5 - degrees3.toFloat())) / 60F)
        val charty6 = roomtemp
        val chartx7 = chartx6 //placeholder for now
        val charty7 = charty6 //placeholder for now

        //Add the coordinates to the dataVals ArrayList
        dataVals.add(Entry(chartx1, charty1))
        dataVals.add(Entry(chartx2, charty2))
        dataVals.add(Entry(chartx3, charty3))
        dataVals.add(Entry(chartx4, charty4))
        dataVals.add(Entry(chartx5, charty5))
        dataVals.add(Entry(chartx6, charty6))
        dataVals.add(Entry(chartx7, charty7))

        //Assign dataVals ArrayList and a name to the first line data set.
        val lineDataSet1:LineDataSet
        if(!useStd){
            lineDataSet1 =
                LineDataSet(dataVals, "Schedule Calculated for " + (glassthickness * 2.54).toString() + "cm Thickness [COE $glassCOE]")
        }else {
            lineDataSet1 =
                LineDataSet(dataVals, "Schedule Calculated for $glassthickness\" Thickness [COE $glassCOE]")
        }

        //Creata an ArrayList for the data sets and add the line data sets.
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataSet1)


        //create a val that contains all the above data and load it in to the chart.
        val data = LineData(dataSets)
        lineChartView.data = data

        // set the filled area
        lineDataSet1.setDrawFilled(true)
        if (Utils.getSDKInt() >= 18) {
            //Drawables are only available after api level 18.
            val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
            lineDataSet1.setFillDrawable(drawable)
        }else{
            lineDataSet1.setFillColor(Color.RED)
        }

        //refresh the chart with the newly loaded data
        lineChartView.invalidate()


        val xAxisValues = ArrayList<String>()
        xAxisValues.add("xAxisValues0")
        xAxisValues.add("xAxisValues1")
        xAxisValues.add("xAxisValues2")
        xAxisValues.add("xAxisValues3")
        xAxisValues.add("xAxisValues4")
        xAxisValues.add("xAxisValues5")
        xAxisValues.add("xAxisValues6")
        xAxisValues.add("xAxisValues7")
        xAxisValues.add("xAxisValues8")
        xAxisValues.add("xAxisValues9")
        xAxisValues.add("xAxisValues10")

    //Set the ramps back to FULL if need be
    if (ramp1 == "1798"){ramp1 = "FULL"}
    if (ramp2 == "1798"){ramp2 = "FULL"}
    if (ramp3 == "1798"){ramp3 = "FULL"}
    if (ramp4 == "1798"){ramp4 = "FULL"}
    if (ramp5 == "1798"){ramp5 = "FULL"}

    if (ramp1 == fRateToCRate("1798")){ramp1 = "FULL"}
    if (ramp2 == fRateToCRate("1798")){ramp2 = "FULL"}
    if (ramp3 == fRateToCRate("1798")){ramp3 = "FULL"}
    if (ramp4 == fRateToCRate("1798")){ramp4 = "FULL"}
    if (ramp5 == fRateToCRate("1798")){ramp5 = "FULL"}

    }

    //Format the Values for the X Axis
    class XAxisValueFormatter: IAxisValueFormatter {

        val mFormat = DecimalFormat("###,###,###.0")

        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return mFormat.format(value) + " hr"
        }

    }

    //Format the Values for the Y Axis
    class YAxisValueFormatter: IAxisValueFormatter {

        val mFormat = DecimalFormat("###,###,##0")
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return mFormat.format(value)+"°"
        }

    }



    //Save the data to the Database
    fun saveschedule() {

        //Set whether to display the data in Standard or Metric
        useStd = units == "Standard"
        Toast.makeText(context, "The preferences are set to $units.", Toast.LENGTH_LONG).show()

        var dbschedule = Ks(
            id,
            name,
            thickness,
            ramp1,
            degrees1,
            hold1,
            ramp2,
            degrees2,
            hold2,
            ramp3,
            degrees3,
            hold3,
            ramp4,
            degrees4,
            hold4,
            ramp5,
            degrees5,
            hold5,
            glassCOE,
            units
        )
        //db.deleteData()
        db.insertData(dbschedule)
        db.updateData(dbschedule)
        //id = db.readData().size
        generateSpinner()

    }


    //Delete the selected schedule and reindex the db
    fun delSchedule(view: View){

        //Load the Database
        var data = db.readData()

        //A Toast to help debug
        /*Toast.makeText(this,
            "DB Size - " + db.readData().size.toString() +
                "\n Spinner Size - $spinnerSize \n"+
                "idSelected - $idSelected"+
                "id - $id",
            Toast.LENGTH_LONG).show()*/

        //If the selected option isn't the last row in the database,
        if ((idSelected+1) != (data.size)){
        //starting from one after idSelected to the end of the database...
        for (i in (idSelected+1)..(data.size-1)) {
            //decrease the ID of every row
            id = data.get(i - 1).id
            //keeping the rest of the data the same.
            name = data.get(i).name
            thickness = data.get(i).thickness
            ramp1 = data.get(i).step1
            degrees1 = data.get(i).step2
            hold1 = data.get(i).step3
            ramp2 = data.get(i).step4
            degrees2 = data.get(i).step5
            hold2 = data.get(i).step6
            ramp3 = data.get(i).step7
            degrees3 = data.get(i).step8
            hold3 = data.get(i).step9
            ramp4 = data.get(i).step10
            degrees4 = data.get(i).step11
            hold4 = data.get(i).step12
            ramp5 = data.get(i).step13
            degrees5 = data.get(i).step14
            hold5 = data.get(i).step15
            glassCOE = data.get(i).coe
            units = data.get(i).units
            //Update the DB with the new information
            var editschedule = Ks(
                id,
                name,
                thickness,
                ramp1,
                degrees1,
                hold1,
                ramp2,
                degrees2,
                hold2,
                ramp3,
                degrees3,
                hold3,
                ramp4,
                degrees4,
                hold4,
                ramp5,
                degrees5,
                hold5,
                glassCOE,
                units
            )
            db.updateData(editschedule)
        }

        }
        //id = data.get(idSelected).id

        //Delete the last row in the Database.
        id = data.size - 1
        var dbschedule = Ks(
            id,
            name,
            thickness,
            ramp1,
            degrees1,
            hold1,
            ramp2,
            degrees2,
            hold2,
            ramp3,
            degrees3,
            hold3,
            ramp4,
            degrees4,
            hold4,
            ramp5,
            degrees5,
            hold5,
            glassCOE,
            units
        )
        db.deleteSelected(dbschedule)
        db.close()

        //set id and idSelected to first position
        id = 0
        idSelected = 0

        //Refresh the spinner and load the selected schedule to the Recyclerview
        generateSpinner()
        cv_Load.performClick()

    }


    //Load a previously saved schedule if one exists
    fun loadSchedule(view: View) {
        //Load the Database
        var data = db.readData()

        //Initialize testdata
        var testdata = ""

        //If the database isn't empty, load the data from the selected program in to a string.
        if (data.size != 0) {

            testdata += data.get(idSelected).name + "\n" +
                    "(ID:" + data.get(idSelected).id.toString() + " in db)\n" +
                    "Thickness of " + data.get(idSelected).thickness + " Inches\n" +
                    "COE of " + data.get(idSelected).coe +
                    "RA1  " + data.get(idSelected).step1 + "°/Hour\n" +
                    "°F1  " + data.get(idSelected).step2 + "°\n" +
                    "HLD1 " + data.get(idSelected).step3 + " Minutes\n" +
                    "RA2  " + data.get(idSelected).step4 + "°/Hour\n" +
                    "°F2  " + data.get(idSelected).step5 + "°\n" +
                    "HLD2 " + data.get(idSelected).step6 + " Minutes\n" +
                    "RA3  " + data.get(idSelected).step7 + "°/Hour\n" +
                    "°F3  " + data.get(idSelected).step8 + "°\n" +
                    "HLD3 " + data.get(idSelected).step9 + " Minutes\n" +
                    "RA4  " + data.get(idSelected).step10 + "°/Hour\n" +
                    "°F4  " + data.get(idSelected).step11 + "°\n" +
                    "HLD4 " + data.get(idSelected).step12 + " Minutes\n" +
                    "RA5  " + data.get(idSelected).step13 + "°/Hour\n" +
                    "°F5  " + data.get(idSelected).step14 + "°\n" +
                    "HLD5 " + data.get(idSelected).step15 + " Minutes\n" +
                    "Generated using \n" +
                    "GlassBlower's Toolchest App\n" +
                    "by Bert Langan\n\n"

            //Show a toast of which information was loaded
            //Toast.makeText(this, testdata, Toast.LENGTH_SHORT).show()

            //Load the variables with the correct information from the database
            id = data.get(idSelected).id
            name = data.get(idSelected).name
            thickness = data.get(idSelected).thickness
            glassthickness = data.get(idSelected).thickness.toDouble()
            ramp1 = data.get(idSelected).step1
            degrees1 = data.get(idSelected).step2
            hold1 = data.get(idSelected).step3
            ramp2 = data.get(idSelected).step4
            degrees2 = data.get(idSelected).step5
            hold2 = data.get(idSelected).step6
            ramp3 = data.get(idSelected).step7
            degrees3 = data.get(idSelected).step8
            hold3 = data.get(idSelected).step9
            ramp4 = data.get(idSelected).step10
            degrees4 = data.get(idSelected).step11
            hold4 = data.get(idSelected).step12
            ramp5 = data.get(idSelected).step13
            degrees5 = data.get(idSelected).step14
            hold5 = data.get(idSelected).step15
            glassCOE = data.get(idSelected).coe
            units = data.get(idSelected).units

            //Set whether to display the data in Standard or Metric
            useStd = units == "Standard"

            generateChart()

        } else {
            //Assign the chart to a val
            val lineChartView = findViewById<LineChart>(R.id.line_chart)

            //If the database is empty, load dashes in to the variables.
            name = "---"
            ramp1 = "---"
            degrees1 = "---"
            hold1 = "---"
            ramp2 = "---"
            degrees2 = "---"
            hold2 = "---"
            ramp3 = "---"
            degrees3 = "---"
            hold3 = "---"
            ramp4 = "---"
            degrees4 = "---"
            hold4 = "---"
            ramp5 = "---"
            degrees5 = "---"
            hold5 = "---"

            //Reset the chart
            try{
                lineChartView.clearValues()
                lineChartView.clear()
            }catch(e: NullPointerException){Toast.makeText(context,"Please Create a New Schedule.", Toast.LENGTH_LONG).show()}

        }

            //Clear the recyclerview
            schedule.clear()

            //Add the new information in to the recyclerview
            if (!useStd){

                schedule.add(rvKilnScheduleStep("RA1", ramp1, "°/Hour"))
                schedule.add(rvKilnScheduleStep("°C 1", degrees1, "°"))
                schedule.add(rvKilnScheduleStep("HLD1", hold1, "minutes"))

                schedule.add(rvKilnScheduleStep("RA2", ramp2, "°/Hour"))
                schedule.add(rvKilnScheduleStep("°C 2", degrees2, "°"))
                schedule.add(rvKilnScheduleStep("HLD2", hold2, "minutes"))

                schedule.add(rvKilnScheduleStep("RA3", ramp3, "°/Hour"))
                schedule.add(rvKilnScheduleStep("°C 3", degrees3, "°"))
                schedule.add(rvKilnScheduleStep("HLD3", hold3, "minutes"))

                schedule.add(rvKilnScheduleStep("RA4", ramp4, "°/Hour"))
                schedule.add(rvKilnScheduleStep("°C 4", degrees4, "°"))
                schedule.add(rvKilnScheduleStep("HLD4", hold4, "minutes"))

            } else {
                schedule.add(rvKilnScheduleStep("RA1", ramp1, "°/Hour"))
                schedule.add(rvKilnScheduleStep("°F 1", degrees1, "°"))
                schedule.add(rvKilnScheduleStep("HLD1", hold1, "minutes"))

                schedule.add(rvKilnScheduleStep("RA2", ramp2, "°/Hour"))
                schedule.add(rvKilnScheduleStep("°F 2", degrees2, "°"))
                schedule.add(rvKilnScheduleStep("HLD2", hold2, "minutes"))

                schedule.add(rvKilnScheduleStep("RA3", ramp3, "°/Hour"))
                schedule.add(rvKilnScheduleStep("°F 3", degrees3, "°"))
                schedule.add(rvKilnScheduleStep("HLD3", hold3, "minutes"))

                schedule.add(rvKilnScheduleStep("RA4", ramp4, "°/Hour"))
                schedule.add(rvKilnScheduleStep("°F 4", degrees4, "°"))
                schedule.add(rvKilnScheduleStep("HLD4", hold4, "minutes"))
            }
            //tell the recyclerview adapter that the data has changed and needs to be refreshed.
            rvKilnSchedule.adapter?.notifyDataSetChanged()

            //Close the database
            db.close()
                       }


    //Share the currently selected schedule
    fun shareSchedule(view: View) {



        var data = db.readData()
        var shareData = ""
        for (i in 0..(data.size - 1)) {
            shareData = "Kiln Schedule for $thickness in. work\n" +
                    "COE \t" + data.get(i).coe +
                    "RA1 \t" + data.get(i).step1 + "°/Hour\n" +
                    "°F1 \t\t" + data.get(i).step2 + "°\n" +
                    "HLD1\t" + data.get(i).step3 + " Minutes\n" +
                    "RA2 \t" + data.get(i).step4 + "°/Hour\n" +
                    "°F2 \t\t" + data.get(i).step5 + "°\n" +
                    "HLD2\t" + data.get(i).step6 + " Minutes\n" +
                    "RA3 \t" + data.get(i).step7 + "°/Hour\n" +
                    "°F3 \t\t" + data.get(i).step8 + "°\n" +
                    "HLD3\t" + data.get(i).step9 + " Minutes\n" +
                    "RA4 \t" + data.get(i).step10 + "°/Hour\n" +
                    "°F4 \t\t" + data.get(i).step11 + "°\n" +
                    "HLD4\t" + data.get(i).step12 + " Minutes\n" +
                    "RA5 \t" + data.get(i).step13 + "°/Hour\n" +
                    "°F5 \t\t" + data.get(i).step14 + "°\n" +
                    "HLD5\t" + data.get(i).step15 + " Minutes\n\n" +
                    "Generated using \n" +
                    "GlassBlower's Toolchest App\n" +
                    "by Bert Langan\n\n"

            //A Toast to help debug
            //Toast.makeText(this, shareData, Toast.LENGTH_SHORT).show()

            //Share the Schedule & a bitmap of the chart

        try {
            //Save the Bitmap to ExternalCacheDir
            val lc = findViewById<LineChart>(R.id.line_chart)
            val bitmap = lc.chartBitmap
            val file = File(this.getExternalCacheDir(),"$name.png")
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()
            file.setReadable(true, false)
            val apkURI = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)

            //Set up the intent
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            //Add the Schedule Text
            //intent.type = "text/plain"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                var myClipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                var myClip: ClipData? = ClipData.newPlainText("Schedule", shareData)
                myClipboard.primaryClip = myClip

                Toast.makeText(context,"Schedule Copied to Clipboard",Toast.LENGTH_LONG).show()
            }

            intent.putExtra(Intent.EXTRA_TEXT, shareData)
            intent.putExtra(Intent.EXTRA_STREAM, apkURI)
            intent.type = "image/png"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            //Share the Intent
            startActivity(Intent.createChooser(intent, "Share schedule via"))

        } catch (e: Exception) {
            e.printStackTrace()
        }

        }
/*
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, shareData)
        intent.type = "text/plain"

        startActivity(Intent.createChooser(intent, "Where would you like to share?"))
*/

    }

    //Convert Temps between Metric and Standard
    fun cToF(c: Double) = (9/5.0 * c) + 32
    fun fToC(f: Double) = 5/9.0 * (f - 32)
    fun cToF(c: String) = Math.round(cToF(c.toDouble())).toString()
    fun fToC(f: String) = Math.round(fToC(f.toDouble())).toString()

    //Convert Temp Change Rates between Metric and Standard
    fun fRateToCRate(f: Double) = f / (9/5.0)
    fun fRateToCRate(f: String) = Math.round(fRateToCRate(f.toDouble())).toString()
    fun cRateToFRate(c: String) = Math.round(cRateToFRate(c.toDouble())).toString()
    fun cRateToFRate(c: Double) = c * (9/5.0)


    fun isNumeric(s: String): Boolean {
        try {
            s.toDouble()
        } catch (e: Exception) {
            return false
        }
        return true
    }
}

