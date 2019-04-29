package com.almutlaq.realstate.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.Complaintdetails
import com.almutlaq.realstate.activity.SelfsuserServicesDetails
import com.almutlaq.realstate.dto.Complaintlistmodel
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.complaintlist_row.view.*

internal class ComplaintlistListAdapter (private var arrayList: ArrayList<Complaintlistmodel>,
                                         private val context: Context,
                                         private val layout: Int) : RecyclerView.Adapter<ComplaintlistListAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintlistListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)

    }
    override fun onBindViewHolder(holder: ComplaintlistListAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        /*if (arrayList[position].complaintstatus.equals("0")){
            holder.btn_open!!.visibility = View.VISIBLE

        }else if (arrayList[position].complaintstatus.equals("1")){
            holder.btn_closed!!.visibility = View.VISIBLE
        }*/

        holder.itemView.setOnClickListener {
            //context.startActivity(Intent(context, Complaintdetails::class.java))
            val intent = Intent(context, Complaintdetails::class.java)
            intent.putExtra("Title",arrayList[position].complainttitle)
            intent.putExtra("Problem",arrayList[position].complaintquote)
            intent.putExtra("Problem_Status",arrayList[position].complaintstatus)
            intent.putExtra("Closed_Note",arrayList[position].complaintclosednote)

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


  internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){
      var tv_open : TextView? = null
      var tv_closed : TextView? = null
      fun bindItems(items: Complaintlistmodel) {

          tv_open = itemView.findViewById(R.id.tv_open)
          tv_closed = itemView.findViewById(R.id.tv_closed)
          itemView.tv_complain_title.text = items.complainttitle
          itemView.tv_complain_name.text = items.complaintquote
          var complain_sttus = items.complaintstatus

          if (complain_sttus.equals("0")){
              tv_open!!.visibility = View.VISIBLE

          }else if (complain_sttus.equals("1")){
              tv_closed!!.visibility = View.VISIBLE
          }
      }

    }
}