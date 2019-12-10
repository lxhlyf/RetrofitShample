package com.jy.retrofitshample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jy.retrofitshample.Shample.GankTodayResult;
import com.jy.retrofitshample.Shample.RetrofitClient;
import com.jy.retrofitshample.retrofit.Call;
import com.jy.retrofitshample.retrofit.Callback;
import com.jy.retrofitshample.retrofit.Response;
import com.jy.retrofitshample.retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitClient.getServiceApi().baidu().enqueue(new Callback<GankTodayResult>() {
            @Override
            public void onResponse(Call<GankTodayResult> call, Response<GankTodayResult> response) {
                Log("" + response.body.getResults().toString());
            }

            @Override
            public void onFailure(Call<GankTodayResult> call, Throwable t) {

            }
        });
    }

    public void Log(String str) {
        Log.i("mainactivity", str);
    }
}
