package com.javier.edukka.controllers;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL = "http://192.168.1.66/edukka/"; //"https://edukka.000webhostapp.com/"
    public static RestService RETROFIT_CLIENT;

    public static RestService getInstance() {
        if (RETROFIT_CLIENT == null) {
            setupRestClient();
        }
        return RETROFIT_CLIENT;
    }

    private static void setupRestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RETROFIT_CLIENT = retrofit.create(RestService.class);
    }
}