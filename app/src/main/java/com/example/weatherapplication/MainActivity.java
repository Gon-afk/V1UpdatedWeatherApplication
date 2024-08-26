package com.example.weatherapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    interface RequestWeather{
        @Headers("x-api-key: " + BuildConfig.OPEN_WEATHER_API)
        @GET("data/2.5/forecast")
        Call<WeatherData> requestWeather(@Query("lat") String lat,
                                         @Query("lon") String lon,
                                         @Query("units") String units,
                                         @Query("cnt") String count);
    }
    TextView CityEditText;
    TextView cityEditText;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // FALTA MANDAR NOTIFCATIONS, E ALARM MANAGER

        cityEditText = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestWeather requestWeather = retrofit.create(MainActivity.RequestWeather.class);

        requestWeather.requestWeather(String.valueOf("40.2670498"), String.valueOf(-7.4775518), "metric", "10").enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {

                if (response.isSuccessful() && response.body() != null) {
                    WeatherData weatherData = response.body();
                    WeatherData.WeatherList.Main main = weatherData.getList().get(0).getMain();
                    cityEditText.setText(weatherData.getCity().getCountry().toString());
                    textView2.setText(weatherData.getCity().getName().toString());
                    textView3.setText("temp "+ main.getTemp().toString());
                    textView4.setText("max "+ main.getTempMax().toString());
                    textView5.setText("min "+ main.getTempMin().toString());
                    textView6.setText(weatherData.getList().get(0).getWeather().get(0).getMain().toString());
                    textView7.setText(weatherData.getList().get(0).getWeather().get(0).getDescription().toString());
                }
            }
            @Override
            public void onFailure(Call<WeatherData> call, Throwable throwable) {
                cityEditText.setText(throwable.getMessage());
            }
        });
    }

    public void UserData(View view) {
        Intent myIntent = new Intent(this, ViewUserActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void GetCityOnClick(View view) {
        CityEditText = findViewById(R.id.CityEditText);
        //String city = CityEditText.getText().toString();
    }
    public void sensors(View view) {
        Intent myIntent = new Intent(this, SensorClass.class);
        MainActivity.this.startActivity(myIntent);
    }
}