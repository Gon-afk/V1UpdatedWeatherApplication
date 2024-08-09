package com.example.weatherapplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class getWeatherDetails {
    public void getWeatherDetails(double lat, double longi)
    {
        String url = "http://api.openweathermap.org/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()) // convert lib in POJO
                .build();


    }
}
