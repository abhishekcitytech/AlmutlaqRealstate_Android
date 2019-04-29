package com.almutlaq.realstate.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.almutlaq.realstate.activity.SelfsuserServicesDetails
import com.almutlaq.realstate.dto.CustomerListModel
import com.almutlaq.realstate.dto.SelfRescheduledListModel
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.servicestatus_row.view.*

internal class SelfReschduledListAdapter (private var arrayList: ArrayList<CustomerListModel>,
                                          private val context: Context,
                                          private val layout: Int) : RecyclerView.Adapter<SelfReschduledListAdapter.ViewHolder>()  {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfReschduledListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)

    }
    override fun onBindViewHolder(holder: SelfReschduledListAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        holder.itemView.setOnClickListener {
            holder.itemView.setOnClickListener {
                val intent = Intent(context, SelfsuserServicesDetails::class.java)
                intent.putExtra(StatusConstant.INTENT_CS_BOOKING_ID,arrayList[position].service_boooking_id)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }



  internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){

      fun bindItems(items: CustomerListModel) {
          itemView.tvStatus_title.text = items.service_name
          itemView.tvStatus_request.text = items.booking_notes
          itemView.tv_req_date.text = items.booking_requested_date
          itemView.tv_status_address.text = items.flat_no +", "+ items.building_no +", "+ items.building_address
      }

    }
}