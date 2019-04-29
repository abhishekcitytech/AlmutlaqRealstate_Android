package com.almutlaq.realstate.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.ServiceRequest
import com.almutlaq.realstate.dto.Servicetypemodel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.servicelist_row.view.*

internal class ServicetypeAdapter (private var arrayList: ArrayList<Servicetypemodel>,
                                   private val context: Context,
                                   private val layout: Int) : RecyclerView.Adapter<ServicetypeAdapter.ViewHolder>()  {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicetypeAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(layout, parent, false)
        return ViewHolder(v)

    }
    override fun onBindViewHolder(holder: ServicetypeAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

        holder.itemView.setOnClickListener {
            //context.startActivity(Intent(context, ServiceRequest::class.java))
            val intent = Intent(context, ServiceRequest::class.java)
            intent.putExtra("service_id",arrayList[position].serviceid)
            intent.putExtra("service_name",arrayList[position].servicename)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


  internal inner class ViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){
      var imageview : ImageView? = null
      fun bindItems(items: Servicetypemodel) {
          itemView.tvServicename.text = items.servicename.toString()
          imageview = itemView.findViewById(R.id.img_service)
          val img  = items.serviceimg.toString()

          Picasso.get()
                  .load(img)
                  .placeholder(R.drawable.no_image)
                  .error(R.drawable.no_image)
                  .fit()
                  .into(imageview)

      }

    }
}