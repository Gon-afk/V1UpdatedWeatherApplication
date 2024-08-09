package com.example.weatherapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ViewUserActivity extends AppCompatActivity {
    interface RequestUser{
        @GET("api/users/{uid}")
        Call<UserData> getUser(@Path("uid") String uid);

        @POST("api/users/{uid}")
        Call<ResponsePost> postUser(@Body RequestPost requestPost);

    }
    TextView UidTextView;
    TextView FirstNameTextView;
    TextView LastNameTextView;
    TextView Email;
    ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.viewuserdata);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        UidTextView = findViewById(R.id.Uid);
        FirstNameTextView = findViewById(R.id.FirstName);
        LastNameTextView = findViewById(R.id.LastName);
        Email = findViewById(R.id.Email);
        avatar = findViewById(R.id.imageView);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestUser requestUser = retrofit.create(RequestUser.class);
        //POST
//        requestUser.postUser(new RequestPost("morpheus", "leader")).enqueue(new Callback<ResponsePost>() {
//            @Override
//            public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
//                LastNameTextView.setText(response.body().name);
//            }
//
//            @Override
//            public void onFailure(Call<ResponsePost> call, Throwable throwable) {
//                LastNameTextView.setText(throwable.getMessage());
//            }
//        });
        //GET
        requestUser.getUser("2").enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                UidTextView.setText(response.body().data.id);
                FirstNameTextView.setText(response.body().data.first_name);
                LastNameTextView.setText(response.body().data.last_name);
                Email.setText(response.body().data.email);
                Picasso.get().load(response.body().data.avatar).into(avatar);
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                UidTextView.setText(t.getMessage());
            }
        });


    }
    public void NotifyOnClick(View view) {
        Notification.createNotificationChannel(this);
        Notification.showNotification(this, "ALERT", "Hello, I'm just testing!");
    }
    public void CreateAlarmOnClick(View view) {
        Intent intent = new Intent(this, Alarm.class);
        ViewUserActivity.this.startActivity(intent);
    }
}