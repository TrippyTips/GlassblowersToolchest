package com.trippytips.glassblowerstoolchest

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_anneal.*
import java.math.RoundingMode
import java.text.DecimalFormat
import android.widget.ArrayAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.lang.NumberFormatException


class AnnealActivity : AppCompatActivity() {

    lateinit var preset: Spinner
    val schedule = ArrayList<rvKilnScheduleStep>()
    private var glassthickness = 0.00
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
    var roomtemp:Float = 70F





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anneal)


        //Show a toast explaining future plans to testers :)
        Toast.makeText(this, "The load button is now selected automatically when you pick from the list.\nIt will be something else eventually.\nIgnore it for now :)",Toast.LENGTH_LONG).show()


        //Set up the spinner
        generateSpinner()


        cv_Load.performClick()


        //Send the Data to be Displayed in the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvKilnSchedule)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        rvKilnSchedule.adapter = rvKilnScheduleAdapter(schedule)


    }

    val context = this
    var db = ksDataBaseHandler(context)


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
            options.removeAt(options.size - 1)}
        if (id > options.size -1){
            id = options.size -1
        }

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        preset.adapter = arrayAdapter


        if (id>0){preset.setSelection(id)}

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

        fun newschedule(view: View) {
            val spinner = findViewById<Spinner>(R.id.spin_Schedules)
            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.new_schedule_dialog, null)
            val etnumber = dialogView.findViewById<EditText>(R.id.et_number_thickness)
            val etScheduleName = dialogView.findViewById<EditText>(R.id.etScheduleName)

            dialog.setView(dialogView)
            dialog.setCancelable(false)
            dialog.setPositiveButton("Specify") { dialogInterface: DialogInterface, i: Int ->
                val customThicknessString = etnumber.text.toString()
                glassthickness = etnumber.text.toString().toDouble()
                thickness = etnumber.text.toString()
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
            val GlassCOE = 33

            val AnnealCoursness = 250

            //Perform Calculations and assign them to vals

            //Ramp 1 Calculations
            ramp1 =
                    if (60 * ((1.08 * 1000) / ((GlassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))) <= 1798) {

                        df.format(60 * ((1.08 * 1000) / ((GlassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))))

                    } else {

                        "FULL"
                    }

            //Degrees 1 Calculation
            degrees1 =
                    when (GlassCOE) {
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
                    if (60 * ((.54 * 250) / ((GlassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))) <= 1798) {

                        df.format(60 * ((.54 * 250) / ((GlassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))))

                    } else {

                        "FULL"
                    }

            //Degrees 2 Calculation
            degrees2 =
                    when (GlassCOE) {
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
                    if (60 * ((.54 * 750) / ((GlassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))) <= 1798) {

                        df.format(60 * ((.54 * 750) / ((GlassCOE * (Math.pow(glassthickness.toDouble(), 2.00))))))

                    } else {

                        "FULL"
                    }

            degrees3 = "70"
            hold3 = "0"
            ramp4 = "0"
            degrees4 = "0"
            hold4 = "0"
            ramp5 = "0"
            degrees5 = "0"
            hold5 = "0"

            schedule.clear()

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

            rvKilnSchedule.adapter?.notifyDataSetChanged()

            //Generate the Chart
            generateChart()
        }

    //Generate a chart to represent the schedule
    fun generateChart() {

        try {ramp1.toFloat()} catch (e: NumberFormatException) {ramp1 = "1798"}
        try {ramp2.toFloat()} catch (e: NumberFormatException) {ramp2 = "1798"}
        try {ramp3.toFloat()} catch (e: NumberFormatException) {ramp3 = "1798"}
        try {ramp4.toFloat()} catch (e: NumberFormatException) {ramp4 = "1798"}
        try {ramp5.toFloat()} catch (e: NumberFormatException) {ramp5 = "1798"}


        val lineChartView = findViewById<LineChart>(R.id.line_chart)
        val dataVals = ArrayList<Entry>()


        val chartDescription: Description = lineChartView.description

        lineChartView.setNoDataText("Please Load a Schedule.")
        lineChartView.setNoDataTextColor(Color.WHITE)
        lineChartView.setDrawGridBackground(true)
        lineChartView.setDrawBorders(true)
//        lineChartView.setBorderColor(Color.RED)
        lineChartView.setBorderWidth(1F)
        chartDescription.text = "Kiln Schedule"
        lineChartView.description = chartDescription

        val chartx1 = 0F
        val charty1 = roomtemp
        val chartx2 = (60 / ramp1.toFloat()) * (degrees1.toFloat() - charty1)
        val charty2 = degrees1.toFloat()
        val chartx3 = chartx2 + hold1.toFloat()
        val charty3 = degrees1.toFloat()
        val chartx4 = chartx3 + (60 / ramp2.toFloat()) * (charty3 - degrees2.toFloat())
        val charty4 = degrees2.toFloat()
        val chartx5 = chartx4 + hold2.toFloat()
        val charty5 = degrees2.toFloat()
        val chartx6 = chartx5 + (60 / ramp3.toFloat()) * (charty5 - degrees3.toFloat())
        val charty6 = roomtemp
        val chartx7 = chartx6
        val charty7 = charty6

        dataVals.add(Entry(chartx1, charty1))
        dataVals.add(Entry(chartx2, charty2))
        dataVals.add(Entry(chartx3, charty3))
        dataVals.add(Entry(chartx4, charty4))
        dataVals.add(Entry(chartx5, charty5))
        dataVals.add(Entry(chartx6, charty6))
        dataVals.add(Entry(chartx7, charty7))

        val lineDataSet1 = LineDataSet(dataVals, name)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataSet1)



        if(degrees1.contains("---", true)){
            dataVals.clear()
            dataSets.clear()
            lineChartView.invalidate()
            lineChartView.clear()
        }else{
            val data = LineData(dataSets)
            lineChartView.data = data
            lineChartView.invalidate()
        }
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



    }

        //Save the data to the Database

        fun saveschedule() {

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
                hold5
            )
            //db.deleteData()
            db.insertData(dbschedule)
            db.updateData(dbschedule)
            //id = db.readData().size
            generateSpinner()

        }


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
                    hold5
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
                hold5
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

                generateChart()

            } else {
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

                lineChartView.clearValues()

            }

                //Clear the recyclerview
                schedule.clear()

                //Add the new information in to the recyclerview
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

                //tell the recyclerview adapter that the data has changed and needs to be refreshed.
                rvKilnSchedule.adapter?.notifyDataSetChanged()

                //Close the database
                db.close()
                           }


        fun shareSchedule(view: View) {

            var data = db.readData()
            var shareData = ""
            for (i in 0..(data.size - 1)) {
                shareData = "Kiln Schedule for $thickness in. work\n" +
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
            }

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, shareData)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Where would you like to share?"))


        }
    }

