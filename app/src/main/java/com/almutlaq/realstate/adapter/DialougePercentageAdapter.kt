package com.almutlaq.realstate.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.ServiceDetails
import com.almutlaq.realstate.dto.SdialoguePercentModel
import com.almutlaq.realstate.utility.StatusConstant
import kotlinx.android.synthetic.main.servicestatus_row.view.*

class DialougePercentageAdapter(private var list: ArrayList<SdialoguePercentModel>, private var ctxt: Context,
                                booking_complt_percentage: String?,
                                tv_res_cal_date: TextView?,
                                sp_res_cal_Time: AppCompatSpinner?) :
        RecyclerView.Adapter<DialougePercentageAdapter.PercentageAdapter>() {

    private var onBind: Boolean = false
    private var arrayList: ArrayList<SdialoguePercentModel> = list
    private var context: Context = ctxt
    var booking_percentage: String = booking_complt_percentage!!
    var tv_cal_date = tv_res_cal_date
    var sp_cal_time = sp_res_cal_Time
    var chk: BooleanArray? = BooleanArray(arrayList.size)

    init {
        if (booking_percentage.replace("%", "").isNotEmpty()){
            var pos: Int = 0
            for (j in arrayList.indices) {
                if (booking_percentage.equals( arrayList[j].statuspercentage.toString()))
                    pos = j
            }
            for (i in arrayList.indices)
                chk!![i] = i < pos + 1
        }else {
            for (j in arrayList.indices) {
                chk!![j] = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PercentageAdapter {
    //   return PercentageAdapter(LayoutInflater.from(context).inflate(R.layout.custom_rating_layout, null, true))

        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_rating_layout, null)

        val height = parent.measuredHeight / 4
        val width = parent.measuredWidth / arrayList.size

        view.layoutParams = RecyclerView.LayoutParams(width, 100)

        return PercentageAdapter(view)

    }

    override fun onBindViewHolder(holder: PercentageAdapter, position: Int) {

        holder.cvPercent!!.text = arrayList[position].statuspercentage.toString() + "%"
        holder.cvPercent!!.isChecked = chk!![position]

        holder.cvPercent!!.isEnabled = context !is ServiceDetails
        holder.cvPercent!!.setOnClickListener { checkstatus(holder, position)
            StatusConstant.statuspercent = arrayList[position].statuspercentage!!
            Log.e("status_percent", StatusConstant.statuspercent)

            if (StatusConstant.statuspercent.equals("100")){

                if (!tv_cal_date!!.equals(null))
                    tv_cal_date!!.isEnabled = false
                    sp_cal_time!!.isEnabled = false
                     tv_cal_date!!.hint = "Reschedule date"
                     tv_cal_date!!.text = ""
                     tv_cal_date!!.setBackgroundResource(R.drawable.edittxt_box_gray)
                     sp_cal_time!!.setBackgroundResource(R.drawable.edittxt_box_gray)

            }else{
                if (!tv_cal_date!!.equals(null))
                    tv_cal_date!!.isEnabled = true
                    sp_cal_time!!.isEnabled = true
                    tv_cal_date!!.setBackgroundResource(R.drawable.edittxt_box)
                    sp_cal_time!!.setBackgroundResource(R.drawable.edittxt_box)
                if (tv_cal_date!!.text.equals("")){
                    sp_cal_time!!.isEnabled = false
                    sp_cal_time!!.setBackgroundResource(R.drawable.edittxt_box_gray)
                }
            }

        }
    }

    private fun checkstatus(holder: PercentageAdapter, position: Int) {
        for (i in arrayList.indices)
            chk!![i] = i < position + 1
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }


    class PercentageAdapter(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var cvPercent: RadioButton? = null

        init {
            cvPercent = itemView!!.findViewById(R.id.cvPercent)
            val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
            cvPercent!!.buttonDrawable = transparentDrawable

        }
    }
}