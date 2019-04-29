package com.almutlaq.realstate.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.almutlaq.realstate.R
import com.almutlaq.realstate.dto.SdialoguePercentModel
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.custom_rating_layout.view.*

internal class SdialoguePercentListAdapter (private var arrayList: ArrayList<SdialoguePercentModel>,
                                            private val context: Context,
                                            private val layout: Int) : RecyclerView.Adapter<SdialoguePercentListAdapter.ViewHolder>()  {
    var mContext: Context? = context
    var chk: BooleanArray? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SdialoguePercentListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: SdialoguePercentListAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
        totalselectstatus(false)

        holder.cvPercent!!.setOnCheckedChangeListener { buttonView, isChecked ->
         if (isChecked) {
             chk!![position] = true
         }
         else {
             chk!![position] = false
         }

            checkstatus(holder,position)
            StatusConstant.statuspercent = arrayList[position].statuspercentage!!
            Log.e("status_percent", StatusConstant.statuspercent)

         //   scanForActivity(this!!.mContext!!,position)
      //  (this!!.mContext!! as ServiceStatusdialogue).submitUpdateStatusdata(arrayList[position].statuspercentage!!)

        }
    }

   /* fun scanForActivity (cont: Context, position: Int): Activity? {  //for classcast as its a dialogue
        if (cont == null)
          return null
        else if (cont is  Activity)
            return cont as Activity
        else if (cont is ContextWrapper)
            return scanForActivity((cont as ContextWrapper).baseContext, position)
        return null
        (cont as ServiceStatusdialogue).submitUpdateStatusdata(arrayList[position].statuspercentage!!)

    }*/


    private fun changecolor(holder: ViewHolder, position: Int) {
        for (i in 0..arrayList.size) {
            if (chk!![position] === true) {

                holder.cvPercent!!.setBackgroundColor(ContextCompat.getColor(context, R.color.tool_blue))
                holder.itemView.cvPercent.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            else{
                holder.cvPercent!!.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                holder.itemView.cvPercent.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }

    }


    private fun checkstatus(holder: ViewHolder, position: Int){
       var  checkedprev : Boolean = false
       var  checkafter : Boolean = false

        for (a in chk!!.indices) {
            if (chk!![a] == false) {

                chk!![a] == true

                changecolor(holder, position)
                if (chk!![position] === chk!![a])
                    break
            }
        }
    }

    private fun totalselectstatus(status : Boolean){
        for (j in arrayList.indices) {
            chk!![j] = status

        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


  internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){
     var cvPercent : CheckBox? = null
      fun bindItems(items: SdialoguePercentModel) {
          cvPercent = itemView.findViewById(R.id.cvPercent)
          val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
          cvPercent!!.setButtonDrawable(transparentDrawable)
          itemView.cvPercent.text = items.statuspercentage.toString() +"%"

        chk  = BooleanArray(arrayList.size)

      }

    }

}