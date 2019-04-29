package com.almutlaq.realstate.api

import com.almutlaq.realstate.api.ApiUrls.ABOUT_US_API
import com.almutlaq.realstate.api.ApiUrls.ADD_COMPLAINT_API
import com.almutlaq.realstate.api.ApiUrls.COMPLAINT_LIST_API
import com.almutlaq.realstate.api.ApiUrls.CS_ACKNOWLEDGEMENT_SUBMIT_API
import com.almutlaq.realstate.api.ApiUrls.CS_BOOKING_SLOT_API
import com.almutlaq.realstate.api.ApiUrls.CS_CUSTOMER_SERVICE_BOOK_API
import com.almutlaq.realstate.api.ApiUrls.CS_CUSTOMER_SERVICE_CLOSED_API
import com.almutlaq.realstate.api.ApiUrls.CS_CUSTOMER_SERVICE_RESCHEDULE_API
import com.almutlaq.realstate.api.ApiUrls.CS_DASHBOARD_CALENDAR_DATE_API
import com.almutlaq.realstate.api.ApiUrls.CS_DASHBOARD_DATE_WISE_LIST_DATA_API
import com.almutlaq.realstate.api.ApiUrls.CS_ENGINEERLIST_API
import com.almutlaq.realstate.api.ApiUrls.CS_PERCENT_API
import com.almutlaq.realstate.api.ApiUrls.CS_RESCHEDULE_TIMESLOT_API
import com.almutlaq.realstate.api.ApiUrls.CS_SERVICE_DETAILS_API
import com.almutlaq.realstate.api.ApiUrls.CS_UPDATE_SERVICE_STATUS_API
import com.almutlaq.realstate.api.ApiUrls.FORCE_PWD_CHANGE
import com.almutlaq.realstate.api.ApiUrls.FORGOT_PWD_API
import com.almutlaq.realstate.api.ApiUrls.LOGIN_API
import com.almutlaq.realstate.api.ApiUrls.LOGOUT_API
import com.almutlaq.realstate.api.ApiUrls.NOTIFICATION_LIST_API
import com.almutlaq.realstate.api.ApiUrls.RATING_API
import com.almutlaq.realstate.api.ApiUrls.SERVICE_API
import com.almutlaq.realstate.api.ApiUrls.TANENT_BOOK_SERVICE_REQUEST
import com.almutlaq.realstate.api.ApiUrls.TENANT_BOOKING_ACCEPTED_API
import com.almutlaq.realstate.api.ApiUrls.TENANT_PWD_CHANGE
import com.almutlaq.realstate.api.ApiUrls.TENANT_REQUEST_SLOT_LINK
import com.almutlaq.realstate.api.ApiUrls.TENANT_VIEW_SERVICE
import com.almutlaq.realstate.api.ApiUrls.TENANT_VIEW_SUBSERVICES_API
import com.almutlaq.realstate.base.*
import com.almutlaq.realstate.dto.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiInterface {

    @POST(LOGIN_API)  //for send login data
    fun sendLoginData(@Body loginput : LoginInput): Observable<LoginResponse>

    @POST(LOGOUT_API)  //for send login data
    fun sendLogoutData(@Header("Authorization")  Authorization : String,@Body logoutInput: LogoutInput): Observable<LogOutResponse>

    @GET(SERVICE_API)  //to get service list
    fun getServiceList(@Header("Authorization")  Authorization : String): Observable<ServiceListResponse>

    @POST(FORGOT_PWD_API) //for tenant pwd change
    fun sendForgotPwdData(@Body forgotPasswordInput: ForgotPasswordInput) : Observable<ForgetPasswordResponse>


    @POST(TENANT_PWD_CHANGE) //for tenant pwd change
    fun sendChangePwdData(@Header("Authorization")  Authorization : String , @Body changePasswordInput: ChangePasswordInput) : Observable<ChangePasswordResponse>

    @GET(NOTIFICATION_LIST_API)  //to get notification list
    fun getallNotificationList(@Header("Authorization")  Authorization : String): Observable<NotificationListResponse>


    @GET(ABOUT_US_API)  //to get about us details
    fun getaboutusDetails(@Header("Authorization")  Authorization : String): Observable<AboutusResponse>


    /////////////////////////TENANT APIS///////////////////////////////////////////////////////////////////////////////////////////

    @POST(FORCE_PWD_CHANGE) //for pwd change
    fun sendforcechngPwdData(@Header("Authorization")  Authorization : String , @Body changePasswordInput: ChangePasswordInput) : Observable<ChangePasswordResponse>

    //Tenant
    @POST(ADD_COMPLAINT_API)  //to register complaint
    fun addComplaint(@Header("Authorization")  Authorization : String,
                     @Body  regComplaintInput: RegComplaintInput): Observable<RegComplaintResponse>

    @GET(COMPLAINT_LIST_API)  //to get complaint list
    fun getComplaintList(@Path("status") status : String,@Header("Authorization")  Authorization : String): Observable<ComplaintListResponse>

    @GET(TENANT_VIEW_SERVICE)  //to view tenant booked,rescheduled,closed service
    fun getTenantServiceList(/*@Path("servicetype") servicetype : String,*/@Header("Authorization")  Authorization : String): Observable<TenantServiceListResponse>

    @POST(RATING_API)  //for tenant rating and feedback
    fun addrating(@Path("serviceid") serviceid : Int,@Header("Authorization")  Authorization : String,
                  @Body  ratingInput: RatingInput): Observable<RatingResponse>


    @GET(TENANT_REQUEST_SLOT_LINK)  //to get tenant slot time for dropdown //TENANT_REQUEST_SLOT_LINK  --> change
    fun getTenantBookingSlot(@Path("dayname") dayname : String,
                             @Header("Authorization")  Authorization : String): Observable<RequestBookingSlotResponse>

    @POST (TANENT_BOOK_SERVICE_REQUEST)  //
    fun sendServiceRequest(@Header("Authorization")  Authorization : String,
                           @Body  bookServiceInput: BookServiceInput): Observable<RequestServiceResponse>


    @GET(TENANT_VIEW_SUBSERVICES_API)  //to get view_subservice_List for dropdown
    fun getTenantViewSevServiceList(@Path("serviceid") serviceid: Int,
                                    @Header("Authorization")  Authorization : String): Observable<ViewSubServiceListResponse>

    @POST(TENANT_BOOKING_ACCEPTED_API)  //for tenant to accept or decline booked service
    fun sendbookingaccept(@Path("bookingid") bookingid : String,@Header("Authorization")  Authorization : String,
                  @Body  tBookingAcceptInput: TBookingAcceptInput): Observable<TbookingAcceptResponse>



    /* @GET (TENANT_BOOKING_SLOT +"/"+ {day})
     fun getTenantBookingSlot(@Header("Authorization")  Authorization : String, @Path(StatusConstant.DAY = "day")String day): Observable<ServiceListResponse>*/


    //////CUSTOMER SERVICE APIS////////////////////////////////////////////////////////////////////////////////

    @GET(CS_DASHBOARD_CALENDAR_DATE_API)  //to get customer service dashboard calendar selected dates
    fun getCalendarSelecteddatelist(@Path("month") currentdate : String,
                                    @Header("Authorization")  Authorization : String): Observable<CsCalendardatesResponse>

    @GET(CS_DASHBOARD_DATE_WISE_LIST_DATA_API)  //to get customer service dashboard booked rescheduled list per date wise
    fun getCSbookedrescheduleddate(@Path("selecteddate") currentdate : String,
                                   @Header("Authorization")  Authorization : String): Observable<CSdashboarddateResponse>

    @GET(CS_SERVICE_DETAILS_API)  //to get cs service details
    fun getCSserviceDetails(@Path("bookingid") bookingid : Int,
                                   @Header("Authorization")  Authorization : String): Observable<CSserviceDetailsResponse>

    @GET(CS_BOOKING_SLOT_API)  //to get cs ACK booking slot dropdown
    fun getCSbookingSlot(/*@Path("bookingid") bookingid : Int,*/@Path("date") date : String,
                            @Header("Authorization")  Authorization : String): Observable<CSackSlottimeResponse>


    @POST (CS_ACKNOWLEDGEMENT_SUBMIT_API)  //cs acknowledgement submit
    fun sendCSAcknowledgedData(@Path("bookingid") bookingid : Int,@Header("Authorization")  Authorization : String,
                           @Body  cSacknowledgeInput: CSacknowledgeInput): Observable<CSackSubmitResponse>


    @GET(CS_PERCENT_API)  //to get percent list
    fun getCSpercentage(@Header("Authorization")  Authorization : String): Observable<CSpercentageResponse>


    @POST (CS_UPDATE_SERVICE_STATUS_API)  //cs update status submit
    fun sendCSUpdateservicestatus(@Path("bookingid") bookingid : Int,@Header("Authorization")  Authorization : String,
                               @Body  cSupdateServiceStatusInput: CSupdateServiceStatusInput): Observable<CSUpdateStatusResponse>


    @GET(CS_CUSTOMER_SERVICE_BOOK_API)  //to get search list
    fun getCustomerServiceList(@Path("search_val") search_val : String,@Header("Authorization")  Authorization : String): Observable<CustomerServiceListResponse>

    @GET(CS_CUSTOMER_SERVICE_RESCHEDULE_API)  //to get reschedule list
    fun getCustomerServiceRescheduleList(@Path("search_val") search_val : String,@Header("Authorization")  Authorization : String): Observable<CustomerServiceListResponse>

    @GET(CS_CUSTOMER_SERVICE_CLOSED_API)  //to get closed list
    fun getCustomerServiceClosedList(@Path("search_val") search_val : String,@Header("Authorization")  Authorization : String): Observable<CustomerServiceListResponse>

    @GET(CS_ENGINEERLIST_API)  //to get cs ENGINEER LIST dropdown
    fun getengineernamelist(@Header("Authorization")  Authorization : String): Observable<CSengineernameResponse>

    @GET(CS_RESCHEDULE_TIMESLOT_API)  //to get cs ACK booking slot dropdown
    fun getCSRescheduletime(@Path("date") day : String,
                         @Header("Authorization")  Authorization : String): Observable<CSrescheduledtimeResponse>
}