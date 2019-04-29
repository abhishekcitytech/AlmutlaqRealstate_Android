package com.almutlaq.realstate.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import com.almutlaq.realstate.R
import com.almutlaq.realstate.activity.DashboardNavigation
import com.almutlaq.realstate.api.ApiClient
import com.almutlaq.realstate.api.ApiInterface
import com.almutlaq.realstate.base.AboutusResponse
import com.almutlaq.realstate.dto.LoginResponse
import com.almutlaq.realstate.utility.Commonfunctions
import com.almutlaq.realstate.utility.NetworkConnetionCheck
import com.almutlaq.realstate.utility.StatusConstant
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.about_us_fragment.*
import kotlinx.android.synthetic.main.about_us_fragment.view.*
import kotlinx.android.synthetic.main.activity_main_drawer.*
import java.lang.Exception
import java.net.URL

@SuppressLint("ValidFragment")
class AboutUsFragment @SuppressLint("ValidFragment") constructor
(act: Activity): Fragment() {

    val activity = act
    var login_response: LoginResponse? = null
    private var pd: Dialog? = null
    private var apiService: ApiInterface? = null
    private var connectioncheck: NetworkConnetionCheck? = null

    private var img_aboutus : SimpleDraweeView? = null
    private var img_comp: SimpleDraweeView? = null
    private var tv_comp_name : TextView? = null
    private var tv_comp_url : TextView? = null

   // private var tv_comp_description : TextView? = null
    private var tvAddress: TextView? = null
    private var llWebsite : LinearLayout? = null
    private var llvideo : LinearLayout? = null
    private var comp_video : VideoView? = null
    private var img_thumnail : ImageView? = null
    private  var wb_comp_description : WebView? = null

    var url : String? =  "http://almutlaq.info/"

    var videolink : String ? =  null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Fresco.initialize(activity)
        val rootview = inflater.inflate(R.layout.about_us_fragment, container, false)
        init(rootview)
        performclick()
        return rootview
    }

    @SuppressLint("ResourceAsColor")
    override fun onPause() {
        super.onPause()
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.drawable.toolbar_bg)//R.drawable.toolbar_bg
        //  (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(resources.getColor("ffffff"))
        (getActivity() as DashboardNavigation). actionBarDrawerToggle!!.drawerArrowDrawable.color = resources.getColor(R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white, getActivity()!!.theme))
        }else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white))
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onResume() {
        super.onResume()
        ///////// FOR BACKPRESS TO GO TO DASHBOARD FRAGMENT
        if (view == null) {
            return
        }

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                return if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

                    if (login_response!!.data.user_type == "T"){
                        (activity as DashboardNavigation).clickDashboardfragment()
                    }else if (login_response!!.data.user_type == "S"){
                        (activity as DashboardNavigation).clickSserviceHomefragment()
                    }
                    true
                } else false
            }
        })

        ///////
        //  if (activity is DashboardNavigation) {
        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.drawerArrowDrawable.color = resources.getColor(R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white, getActivity()!!.theme))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white))
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun init(rootview: View?) {
      //  _pref = Pref(activity)

        (getActivity() as DashboardNavigation).toolbar.setBackgroundResource(R.color.tool_blue)
        (getActivity() as DashboardNavigation).actionBarDrawerToggle!!.drawerArrowDrawable.color = resources.getColor(R.color.white)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white, getActivity()!!.theme))
        } else {
            (getActivity() as DashboardNavigation).toolbar.setTitleTextColor(getActivity()!!.resources.getColor(android.R.color.white))
        }

        login_response = Commonfunctions.get_login_data(activity)
        pd = Commonfunctions.custom_loader(activity, StatusConstant.wait_msg)
        connectioncheck =  NetworkConnetionCheck(activity)
        apiService = ApiClient.getClient().create(ApiInterface::class.java)

        img_aboutus =  rootview!!.findViewById(R.id.img_aboutus)
        img_comp = rootview!!.findViewById(R.id.img_comp)
        tv_comp_name = rootview.findViewById(R.id.tv_comp_name)
     //   tv_comp_description = rootview.findViewById(R.id.tv_comp_description)
        wb_comp_description = rootview.findViewById(R.id.wb_comp_description)
        wb_comp_description!!.getSettings().setLoadsImagesAutomatically(true);
        wb_comp_description!!.getSettings().setJavaScriptEnabled(true);
        wb_comp_description!!.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        tvAddress = rootview.findViewById(R.id.tvAddress)
        tv_comp_url = rootview.findViewById(R.id.tv_comp_url)

        llWebsite = rootview.findViewById(R.id.llWebsite)
        llvideo = rootview.findViewById(R.id.llvideo)
        comp_video = rootview.findViewById(R.id.comp_video)
        img_thumnail = rootview.findViewById(R.id.img_thumnail)

        getAboutusDetails()
    }

    private fun getAboutusDetails() {
        if(connectioncheck!!.isNetworkAvailable()) {
            pd!!.show()
            val aboutussuccessRespeObservable = apiService!!.getaboutusDetails("Bearer"+ login_response!!.data.token)

            aboutussuccessRespeObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<AboutusResponse> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(aboutusResponse: AboutusResponse) {

                            var ResponseMessage = aboutusResponse.message

                            if (aboutusResponse.message_code.equals("1")) {
                                Log.e("complaintlist_success", "success")

                                if (aboutusResponse.data.size > 0) {

                                    for (i in 0..aboutusResponse.data.size - 1) {

                                        tv_comp_name!!.text = aboutusResponse.data.get(i).company_name  // todo : dynamic
                                        url = aboutusResponse.data.get(i).company_website_url

                                        tv_comp_url!!.movementMethod = LinkMovementMethod.getInstance()
                                        var text  = "<a href='"+ aboutusResponse.data[i].company_website_url+"'> Website </a>"
                                      //  tv_comp_url!!.text = Html.fromHtml(text)
                                        if (Build.VERSION.SDK_INT >= 24) {
                                            tv_comp_url!!.text = Html.fromHtml(text,0)// for 24 api and more
                                        } else {
                                            tv_comp_url!!.text = Html.fromHtml(text) // or for older api
                                        }

                                        //removeunderline(text)

                                        videolink = aboutusResponse.data.get(i).company_video_link
                                      //  var uri  : Uri = Uri.parse(/*"rtsp:" +*/videolink)
                                       // comp_video!!.setVideoURI(uri)
                                      //  comp_video!!.setMediaController( MediaController(activity))
                                       // comp_video!!.start()

                                        /////video thumb///////////
                                        try{

                                            var videoId : String = extractYoutubeId(videolink!!)

                                            val img_url : String = "http://img.youtube.com/vi/$videoId/0.jpg"
                                            Picasso.get().load(img_url).into(img_thumnail)
                                        }
                                        catch (e: Exception){
                                            e.printStackTrace()
                                        }
                                        ////video thumb/////////////
                                         comp_video!!.seekTo(100)

                                        var mimeType : String = "text/html"
                                        var encoding : String = "UTF-8"
                                        var txtwb : String = aboutusResponse.data.get(i).company_description

                                        wb_comp_description!!.loadDataWithBaseURL("", txtwb, mimeType, encoding, "")

                                     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                           // tv_comp_description!!.text = Html.fromHtml(txtweb ,TextView.BufferType.SPANNABLE)// for 24 api and more
                                            tv_comp_description!!.setText(Html.fromHtml(aboutusResponse.data.get(i).company_description),TextView.BufferType.SPANNABLE)
                                        } else {
                                            tv_comp_description!!.text = Html.fromHtml(aboutusResponse.data.get(i).company_description) // or for older api
                                        }
                                     */

                                        tvAddress!!.text = aboutusResponse.data.get(i).company_address

                                        var img = aboutusResponse.data.get(i).company_logo
                                       // var img = ""   // todo : dynamic
                                        val controller = Fresco.newDraweeControllerBuilder()
                                                .setUri(Uri.parse(img))
                                                .setTapToRetryEnabled(true)
                                                .build()
                                        img_aboutus!!.controller = controller

                                        var imgcomp = aboutusResponse.data.get(i).company_image
                                        // var img = ""   // todo : dynamic
                                        val controllercomp = Fresco.newDraweeControllerBuilder()
                                                .setUri(Uri.parse(imgcomp))
                                                .setTapToRetryEnabled(true)
                                                .build()
                                        img_comp!!.controller = controllercomp

                                    }
                                }
                            } else {
                                Log.e("about_us_error", "error")
                                Commonfunctions.commonDialogue(activity, StatusConstant.ALERT, ResponseMessage.toString()).show()
                            }

                            pd!!.dismiss()
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }

                        override fun onComplete() {

                        }
                    })
        }else{
            connectioncheck!!.getNetworkActiveAlert().show()
        }

    }


    private fun searchFor(text: String,tvcomp : TextView) {

        val raw = SpannableString(tvcomp.text)
        val spans = raw.getSpans(
                0,
                raw.length,
                BackgroundColorSpan::class.java
        )

        for (span in spans) {
            raw.removeSpan(span)
        }

        var index = TextUtils.indexOf(raw, text)

        while (index >= 0) {
            raw.setSpan(BackgroundColorSpan(-0x74ff75), index, index + text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            index = TextUtils.indexOf(raw, text, index + text.length)
        }

        tvcomp.text = raw
    }

    private fun extractYoutubeId(videolink: String) : String  {  //throws MalformedURLException

        val query = URL(videolink).query
       // var param = query.split("&")
        val param = query.split("&".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        var id: String? = null
        for (row in param) {
            val param1 = row.split("=".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (param1[0] == "v") {
                id = param1[1]
            }
        }
        return id!!

    }

    private fun removeunderline(text: String) {

        /* var s : Spannable = (Spannable) Html.fromHtml(text)

        tv_comp_url!!.text = s*/

    }

    private fun performclick() {
      /*  tv_comp_url!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            activity.startActivity(intent)
        }*/

        llWebsite!!.setOnClickListener { tv_comp_url!!.performClick() }



        llvideo!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(videolink)
            activity.startActivity(intent)
        }


    }



}

