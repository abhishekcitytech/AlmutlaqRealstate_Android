package com.almutlaq.realstate.utility

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.support.v7.app.AlertDialog


public  class NetworkConnetionCheck {

    private val context: Context
    // flag for GPS status
    var isGPSEnabled = false

    constructor(context: Context) {
        this.context = context
    }


    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo

        return info != null && info.isConnectedOrConnecting
    }

    fun getNetworkActiveAlert(): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Network Status")
        builder.setMessage("Network connection not available. Please connect the network.")
                .setCancelable(false)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    dialogInterface.cancel()
                })
                .setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialogInterface, i ->
                    //dialogInterface.cancel();
                    dialogInterface.dismiss()
                })
        return builder.create()
    }




}