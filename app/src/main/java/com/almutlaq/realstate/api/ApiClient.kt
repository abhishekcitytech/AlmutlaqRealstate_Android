package com.almutlaq.realstate.api


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    companion object {

      //  HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
      //  loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        var okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
               /* .addInterceptor(loggingInterceptor)*/
                //.addNetworkInterceptor(networkInterceptor)
                .build()

        var retrofit: Retrofit? = null
        fun getClient(): Retrofit {

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(ApiUrls.BASE_URL)
                        .client(okHttpClient)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit!!
        }
    }



}