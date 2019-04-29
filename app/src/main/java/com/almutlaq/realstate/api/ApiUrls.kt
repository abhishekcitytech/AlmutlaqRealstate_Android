package com.almutlaq.realstate.api

object ApiUrls {

    ///API URLs
 //  val BASE_URL : String = "http://10.10.0.154/almutlaq/api/v1/"     //local
      val BASE_URL : String = "http://almutlaq.info/api/v1/"   // Live new

    //val BASE_URL : String = "http://almutlaqrealestate.com/servicemanager/api/v1/"       //Live old

    const val LOGIN_API : String = "login"

    const val LOGOUT_API : String = "logout"

    const val FORGOT_PWD_API : String ="forgot_password"

    const val SERVICE_API : String = "tenant/service-list"

    const val TENANT_PWD_CHANGE : String ="reset_password"

    const val FORCE_PWD_CHANGE : String ="force_change_password"

    const val NOTIFICATION_LIST_API : String = "send_message"

    const val ABOUT_US_API : String = "about_us"

    const  val ADD_COMPLAINT_API : String = "tenant/add_complain"

    const val COMPLAINT_LIST_API : String = "tenant/complain-list/{status}"

  //  const val TENANT_REQUEST_SLOT_LINK : String = "tenant/request_booking_slot/{dayname}"

    const val TENANT_REQUEST_SLOT_LINK : String = "tenant/booking_slot/{dayname}"

    const val TANENT_BOOK_SERVICE_REQUEST : String = "tenant/book_service"

    const val TENANT_VIEW_SERVICE : String = "tenant/view-services"  ///{servicetype}

    const val TENANT_VIEW_SUBSERVICES_API : String = "tenant/view-subservices/{serviceid}"

    const val RATING_API : String = "tenant/rate_service/{serviceid}"

    const val TENANT_BOOKING_ACCEPTED_API : String = "tenant/booking_accepted_decline/{bookingid}"


    //customer service

    const val CS_DASHBOARD_CALENDAR_DATE_API : String = "customer_service_center/booked-rescheduled-servicedate/{month}"

    const val CS_DASHBOARD_DATE_WISE_LIST_DATA_API : String = "customer_service_center/date-servicedetails/{selecteddate}"

    const val CS_SERVICE_DETAILS_API : String = "customer_service_center/selected-date-servicedetails/{bookingid}"

    const val CS_BOOKING_SLOT_API : String = "customer_service_center/request_booking_slot/{date}" //{bookingid}

    const val CS_ACKNOWLEDGEMENT_SUBMIT_API : String = "customer_service_center/acknowledge_booked_service/{bookingid}"

    const val CS_PERCENT_API : String = "customer_service_center/service-percentage"

    const val CS_UPDATE_SERVICE_STATUS_API : String = "customer_service_center/change_booking_status/{bookingid}"

    const val CS_CUSTOMER_SERVICE_BOOK_API : String = "customer_service_center/customer-servicesList/{search_val}/0"

    const val CS_CUSTOMER_SERVICE_RESCHEDULE_API : String = "customer_service_center/customer-servicesList/{search_val}/1"

    const val CS_CUSTOMER_SERVICE_CLOSED_API : String = "customer_service_center/customer-servicesList/{search_val}/3"

    const val CS_ENGINEERLIST_API : String = "customer_service_center/engineer-list"

    const val CS_RESCHEDULE_TIMESLOT_API : String = "customer_service_center/request_rescheduled_slot/{date}"

}