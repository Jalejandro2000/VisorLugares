package com.examensupletorio.visorlugares.helpers;

import com.examensupletorio.visorlugares.service.IGoogleMapsService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    public static final String GOOGLE_SERVICE_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/";

    private static Retrofit googleMapsServiceRetrofit;


    private static Retrofit CreateRetrofit(String baseUrl) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static <TService> TService getService(Class<TService> service) throws Exception {
        if (service == IGoogleMapsService.class) {
            if (googleMapsServiceRetrofit == null) {
                googleMapsServiceRetrofit = CreateRetrofit(GOOGLE_SERVICE_BASE_URL);
            }
            return googleMapsServiceRetrofit.create(service);
        }
        throw new Exception("Not implemented:" + service.getCanonicalName());
    }




}