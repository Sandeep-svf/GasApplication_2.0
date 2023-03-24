package com.sam.gasapplication.RetrofitApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sam.gasapplication.RetrofitApi.RetroifitErrorResponse.LenientGsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class API_Client {
        public static final String BASE_URL = "https://itdevelopmentservices.com/thegaskiosk/api/";
        public static final String TEMP_BASE_URL = "https://mobileandwebsitedevelopment.com/LivelyPencil/api/";
        public static final String PAYMENT_GETWAY = "https://itdevelopmentservices.com/thegaskiosk/admin/payment?user_id=";


        private static Retrofit retrofit = null;
        private static Api api;
        static Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        public static Api getClient() {
            if (api == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        //.addConverterFactory(GsonConverterFactory.create(gson))
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(LenientGsonConverterFactory.create(gson))
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                Api api = retrofit.create(Api.class);
                return api;
            } else
                return api;
        }

    }


