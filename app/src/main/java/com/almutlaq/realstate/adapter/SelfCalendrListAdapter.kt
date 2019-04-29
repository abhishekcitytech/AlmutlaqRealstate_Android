package com.almutlaq.realstate.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.SelfsuserServicesDetails
import com.almutlaq.realstate.dto.SelfCalendrlistModel
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.selfservice_calendrlist_row.view.*

internal class SelfCalendrListAdapter (private var arrayList: ArrayList<SelfCalendrlistModel>,
                                       private val context: Context,
                                       private val layout: Int) : RecyclerView.Adapter<SelfCalendrListAdapter.ViewHolder>()  {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfCalendrListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)

    }
    override fun onBindViewHolder(holder: SelfCalendrListAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

       /* if (arrayList[position].booking_acknowledge.equals("N")){
            holder.imgAcknowledged!!.visibility = View.GONE
        }else if (arrayList[position].booking_acknowledge.equals("Y")){
            holder.imgAcknowledged!!.visibility = View.VISIBLE
        }

        if (arrayList[position].booking_status == 0){
            holder.ll_booked!!.setText("Booked")
            holder.itemView.ll_booked.setBackgroundResource(R.drawable.btn_booked_bg)
            holder.itemView.ll_booked.setTextColor(ContextCompat.getColor(context, R.color.btn_grey))
        }
        else if (arrayList[position].booking_status == 1){
            holder.ll_booked!!.setText("Rescheduled")
            holder.itemView.ll_booked.setBackgroundResource(R.drawable.btn_rescheduled_bg)
            holder.itemView.ll_booked.setTextColor(ContextCompat.getColor(context, R.color.Orange))
        }
        else if (arrayList[position].booking_status == 3){
            holder.ll_booked!!.setText("Closed")
            holder.itemView.ll_booked.setBackgroundResource(R.drawable.btn_booked_bg)
            holder.itemView.ll_booked.setTextColor(ContextCompat.getColor(context, R.color.btn_grey))
        }*/  // close on 22/11/2018


        holder.itemView.setOnClickListener {
            val intent = Intent(context, SelfsuserServicesDetails::class.java)
            intent.putExtra(StatusConstant.INTENT_CS_BOOKING_ID,arrayList[position].service_boooking_id)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

  internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){
    var imgAcknowledged : ImageView? = null
      var  ll_booked : TextView? = null
      fun bindItems(items: SelfCalendrlistModel) {

          imgAcknowledged = itemView.findViewById(R.id.imgAcknowledged)
          ll_booked  =itemView.findViewById(R.id.ll_booked)
          itemView.tvcal_Status_title.text = items.calstatustitle
          itemView.tvcal_req_date.text = items.calstatusdate
          itemView.tvcal_status_address.text = items.calstatusaddress

          var book_ack = items.booking_acknowledge
          var book_status = items.booking_status

          if (book_status == 0){
              ll_booked!!.setText("Booked")
              itemView.ll_booked.setBackgroundResource(R.drawable.btn_booked_bg)
              itemView.ll_booked.setTextColor(ContextCompat.getColor(context, R.color.btn_grey))

              if (book_ack.equals("N")){
                  imgAcknowledged!!.visibility = View.GONE
              }else if (book_ack.equals("Y")){
                  imgAcknowledged!!.visibility = View.VISIBLE
              }
          }
          else if (book_status == 1){
              ll_booked!!.setText("Rescheduled")
              itemView.ll_booked.setBackgroundResource(R.drawable.btn_rescheduled_bg)
              itemView.ll_booked.setTextColor(ContextCompat.getColor(context, R.color.Orange))
              imgAcknowledged!!.visibility = View.GONE
          }
          else if (book_status == 3){
              ll_booked!!.setText("Closed")
              itemView.ll_booked.setBackgroundResource(R.drawable.btn_booked_bg)
              itemView.ll_booked.setTextColor(ContextCompat.getColor(context, R.color.btn_grey))
              imgAcknowledged!!.visibility = View.GONE
          }
          else if (book_status == 4){
              ll_booked!!.setText("Cancelled")
              itemView.ll_booked.setBackgroundResource(R.drawable.btn_closed_bg_new)
              itemView.ll_booked.setTextColor(ContextCompat.getColor(context, R.color.Red))
              imgAcknowledged!!.visibility = View.GONE
          }


      }

    }
}