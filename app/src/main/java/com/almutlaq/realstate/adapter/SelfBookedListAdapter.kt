package com.almutlaq.realstate.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.SelfsuserServicesDetails
import com.almutlaq.realstate.dto.CustomerListModel
import com.almutlaq.realstate.dto.SelfBookedListModel
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.selfservice_calendrlist_row.view.*
import kotlinx.android.synthetic.main.servicestatus_row.view.*

internal class SelfBookedListAdapter (private var arrayList: ArrayList<CustomerListModel>,
                                      private val context: Context,
                                      private val layout: Int) : RecyclerView.Adapter<SelfBookedListAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfBookedListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)

    }
    override fun onBindViewHolder(holder: SelfBookedListAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SelfsuserServicesDetails::class.java)
            intent.putExtra(StatusConstant.INTENT_CS_BOOKING_ID,arrayList[position].service_boooking_id)
            context.startActivity(intent) }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }



    internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){
        var imgsAcknowledged : ImageView? = null
        fun bindItems(items: CustomerListModel) {

            imgsAcknowledged = itemView.findViewById(R.id.imgsAcknowledged)
            itemView.tvStatus_title.text = items.service_name
            itemView.tvStatus_request.text = items.booking_notes
            itemView.tv_req_date.text = items.booking_requested_date
            itemView.tv_status_address.text = items.flat_no +", "+ items.building_no +", "+ items.building_address

            var book_ack = items.booking_acknowledge
            if (book_ack.equals("N")){
                imgsAcknowledged!!.visibility = View.GONE
            }else if (book_ack.equals("Y")){
                imgsAcknowledged!!.visibility = View.VISIBLE
            }



        }

    }
}