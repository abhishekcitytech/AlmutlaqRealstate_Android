package com.almutlaq.realstate.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.almutlaq.realstate.dto.ServiceHistorymodel
import kotlinx.android.synthetic.main.service_history_row.view.*

internal class ServiceHistoryAdapter (private var arrayList: ArrayList<ServiceHistorymodel>,
                                      private val context: Context,
                                      private val layout: Int) : RecyclerView.Adapter<ServiceHistoryAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceHistoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)

    }
    override fun onBindViewHolder(holder: ServiceHistoryAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

  internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){

      fun bindItems(items: ServiceHistorymodel) {
          itemView.tvHistoryrow.text =  items.servicenote.toString()
          itemView.tvHistorydate.text  = items.booking_update_date.toString()
          itemView.tvEngineerName.text =  items.labourname.toString()
          itemView.tv_job_percent.text =  items.job_completion.toString() + "%"


      }

    }
}