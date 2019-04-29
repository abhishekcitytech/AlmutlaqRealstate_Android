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
import com.almutlaq.realstate.dto.NotifyListModel
import com.almutlaq.realstate.dto.StatusListModel
import com.almutlaq.realstate.utility.Feedbackdialogue
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.notification_row.view.*
import kotlinx.android.synthetic.main.selfservice_calendrlist_row.view.*
import kotlinx.android.synthetic.main.servicestatus_row.view.*

internal class NotifyListAdapter (private var arrayList: ArrayList<NotifyListModel>,
                                  private val context: Context,
                                  private val layout: Int) : RecyclerView.Adapter<NotifyListAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NotifyListAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

  internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){

      var img_Notify : ImageView? = null
      var tv_n_date : TextView? = null
      var tv_n_subject : TextView? = null
      var tv_n_msg : TextView? = null
      var tv_n_time : TextView? = null

      fun bindItems(items: NotifyListModel) {
          img_Notify = itemView.findViewById(R.id.imgNotify)
          tv_n_date = itemView.findViewById(R.id.tv_n_date)
          tv_n_subject = itemView.findViewById(R.id.tv_n_subject)
          tv_n_msg = itemView.findViewById(R.id.tv_n_msg)
          tv_n_time = itemView.findViewById(R.id.tv_n_time)

          itemView.tv_n_date.text = items.notidate
          itemView.tv_n_time.text = items.notitime
          itemView.tv_n_subject.text =  items.notisub
          itemView.tv_n_msg.text =  items.notimsg

      }

    }
}
