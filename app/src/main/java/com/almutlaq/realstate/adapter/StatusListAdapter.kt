package com.almutlaq.realstate.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.ServiceDetails
import com.almutlaq.realstate.dto.StatusListModel
import com.almutlaq.realstate.utility.Feedbackdialogue
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.selfservice_calendrlist_row.view.*
import kotlinx.android.synthetic.main.servicestatus_row.view.*

internal class StatusListAdapter (private var arrayList: ArrayList<StatusListModel>,
                                  private val context: Context,
                                  private val layout: Int) : RecyclerView.Adapter<StatusListAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: StatusListAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

       /* if (arrayList[position].bookingAcknowledge.equals("Y")){
             holder.imgsAcknowledged!!.visibility = View.VISIBLE
        }

        if (arrayList[position].servicetype.equals("3")){
            holder.img_statusClose!!.visibility = View.VISIBLE
        }*/

        holder.itemView.setOnClickListener {

            val intent = Intent(context, ServiceDetails::class.java)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_BOOKINGID,arrayList[position].service_boooking_id.toString())
            intent.putExtra(StatusConstant.INTENT_TSERVICE_SERVICEID,arrayList[position].serviceid.toString())
            intent.putExtra(StatusConstant.INTENT_TSERVICE_SUBSERVICE_ID,arrayList[position].subservice_id.toString())
            intent.putExtra(StatusConstant.INTENT_TSERVICE_TYPE,arrayList[position].servicetype)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_NAME,arrayList[position].statustitle)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_IMG,arrayList[position].bookingImage)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_DATE,arrayList[position].statusdate)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_ACK,arrayList[position].bookingAcknowledge)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_BOOK_ACCEPT,arrayList[position].booking_accepted)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_ACK_SLOT_DATE,arrayList[position].acknowledge_slot_date)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_ACK_REQ_DATE,arrayList[position].acknowledge_requested_date)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_TENANTTIME,arrayList[position].tenant_slot_time)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_TENANTDATE,arrayList[position].tenant_slot_date)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_CSKTIME,arrayList[position].csk_slot_time)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_BOOKNOTE,arrayList[position].booking_acknowledge_note)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_FLATNO,arrayList[position].statusflatno)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_BUILDINGNO,arrayList[position].statusbuildigno)
            intent.putExtra(StatusConstant.INTENT_TSERVICE_BUILDINGADDRESS,arrayList[position].statusaddress)
            intent.putExtra(StatusConstant.INTENT_T_BOOKING_PERCENT,arrayList[position].booking_completion_percentage)
            intent.putExtra(StatusConstant.INTENT_T_STATUS_REQUEST,arrayList[position].statusrequest)
            intent.putExtra(StatusConstant.INTENT_CS_HISTORY_LIST, arrayList[position].booking_update_note)
            intent.putExtra(StatusConstant.INTENT_SUBSERVICE, arrayList[position].subservice)
            Log.e("historylistsize1",arrayList[position].booking_update_note.size.toString())
            context.startActivity(intent)

        }

        holder.img_statusClose!!.setOnClickListener {
            val mydialog = Feedbackdialogue(context,"T",arrayList[position].service_boooking_id,arrayList[position].tenant_rating,arrayList[position].tenant_rating_note)
            mydialog.show()
        }

        holder.img_status_NotClose!!.setOnClickListener {
            val mydialog = Feedbackdialogue(context,"T",arrayList[position].service_boooking_id,arrayList[position].tenant_rating,arrayList[position].tenant_rating_note)
            mydialog.show()
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

  internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){
      var  imgsAcknowledged : ImageView? = null
      var img_statusClose  :ImageView?  = null
      var img_status_NotClose  :ImageView?  = null
      var tv_service_status : TextView? = null

      fun bindItems(items: StatusListModel) {
          imgsAcknowledged = itemView.findViewById(R.id.imgsAcknowledged)
          img_statusClose = itemView.findViewById(R.id.img_statusClose)
          img_status_NotClose = itemView.findViewById(R.id.img_status_NotClose)
          tv_service_status = itemView.findViewById(R.id.tv_service_status)

          var book_ack = items.bookingAcknowledge

          var service_type = items.servicetype

          if(service_type.equals("0")){
              tv_service_status!!.text = "Booked"
              itemView.tv_service_status.setTextColor(ContextCompat.getColor(context, R.color.tool_blue))
              img_statusClose!!.visibility = View.GONE
              img_status_NotClose!!.visibility = View.GONE

              if(book_ack.equals("Y")){
                  imgsAcknowledged!!.visibility = View.VISIBLE
              }else{
                  imgsAcknowledged!!.visibility = View.GONE
              }
          } else if(service_type.equals("1")){
              tv_service_status!!.text = "Rescheduled"
              itemView.tv_service_status.setTextColor(ContextCompat.getColor(context, R.color.Goldenrod))
              img_statusClose!!.visibility = View.GONE
              img_status_NotClose!!.visibility = View.GONE
              imgsAcknowledged!!.visibility = View.GONE
          }
          else if(service_type.equals("3")){
              tv_service_status!!.text = "Closed"
              itemView.tv_service_status.setTextColor(ContextCompat.getColor(context, R.color.salary_green))
              imgsAcknowledged!!.visibility = View.GONE

              var rate  = arrayList[position].tenant_rating

              if (rate!! > 0.0F) img_statusClose!!.visibility = View.VISIBLE else {
                  this.img_status_NotClose!!.visibility = View.VISIBLE
              }
          } else if(service_type.equals("4")){
              tv_service_status!!.text = "Cancelled"
              itemView.tv_service_status.setTextColor(ContextCompat.getColor(context, R.color.Red))
              img_statusClose!!.visibility = View.GONE
              imgsAcknowledged!!.visibility = View.GONE
              img_status_NotClose!!.visibility = View.GONE
          }

          itemView.tvStatus_title.text = items.statustitle
          itemView.tvStatus_request.text = items.statusrequest
          itemView.tv_req_date.text = items.tenant_slot_date
          itemView.tv_status_address.text =items.statusflatno + ", " + items.statusbuildigno + ", "+items.statusaddress
      }

    }
}
