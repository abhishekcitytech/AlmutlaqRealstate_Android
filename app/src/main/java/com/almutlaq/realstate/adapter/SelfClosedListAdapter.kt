package com.almutlaq.realstate.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.SelfsuserServicesDetails
import com.almutlaq.realstate.dto.CustomerListModel
import com.almutlaq.realstate.utility.Feedbackdialogue
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.servicestatus_row.view.*

internal class SelfClosedListAdapter (private var arrayList: ArrayList<CustomerListModel>,
                                      private val context: Context,
                                      private val layout: Int) : RecyclerView.Adapter<SelfClosedListAdapter.ViewHolder>()  {

    var rating_val : String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfClosedListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)

    }
    override fun onBindViewHolder(holder: SelfClosedListAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        holder.itemView.setOnClickListener {
           /* val intent = Intent(context, SelfsuserServicesDetails::class.java)
            //   intent.putExtra(StatusConstant.INTENT_CS_BOOKING_ID,arrayList[position].service_boooking_id)
            intent.putExtra(StatusConstant.INTENT_CS_BOOKING_ID,19)
            context.startActivity(intent) */
            val intent = Intent(context, SelfsuserServicesDetails::class.java)
            intent.putExtra(StatusConstant.INTENT_CS_BOOKING_ID,arrayList[position].service_boooking_id)
            context.startActivity(intent)
        }

        holder.imgstatusClose!!.setOnClickListener {
            //.toFloat()
            //var tenant_rating : Float? = (arrayList[position].tenant_rating) as Float
            rating_val = arrayList[position].tenant_rating.toString()
            val mydialog = Feedbackdialogue(context,"S",arrayList[position].service_boooking_id,rating_val!!.toFloat(),arrayList[position].tenant_rating_note)
            mydialog.show()
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }



  internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){
      var imgstatusClose : ImageView? = null

      fun bindItems(items: CustomerListModel) {
          imgstatusClose = itemView.findViewById(R.id.img_statusClose)

          itemView.tvStatus_title.text = items.service_name
          itemView.tvStatus_request.text = items.booking_notes
          itemView.tv_req_date.text = items.booking_requested_date
          itemView.tv_status_address.text = items.flat_no +", "+ items.building_no +", "+ items.building_address

          val rate = items.tenant_rating

          var book_status = items.booking_status
          if (book_status.equals("3") && rate!! > 0){

                 imgstatusClose!!.visibility = View.VISIBLE
          }else {
              imgstatusClose!!.visibility = View.GONE
          }

          //rating_val = items.tenant_rating.toString()
      }

    }
}