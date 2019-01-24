package com.trippytips.glassblowerstoolchest

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class rvKilnScheduleAdapter(val schedule: ArrayList<rvKilnScheduleStep>) : RecyclerView.Adapter<rvKilnScheduleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): rvKilnScheduleAdapter.ViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.kiln_schedule, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = schedule.size



    override fun onBindViewHolder(p0: rvKilnScheduleAdapter.ViewHolder, p1: Int) {
        val kilnschedule = schedule[p1]

        p0?.scheduleInfo?.text = kilnschedule.step
        p0?.derivedInfo?.text = kilnschedule.calculation
        p0?.keyInfo?.text = kilnschedule.key

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scheduleInfo: TextView = itemView.findViewById(R.id.ScheduleInfo) as TextView
        val derivedInfo: TextView = itemView.findViewById(R.id.DerivedInfo) as TextView
        val keyInfo: TextView = itemView.findViewById(R.id.KeyInfo) as TextView
    }
}

