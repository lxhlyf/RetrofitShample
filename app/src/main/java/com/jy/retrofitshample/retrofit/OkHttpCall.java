package com.jy.retrofitshample.retrofit;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;


/**
 * 具体请求方式的 封装装
 */
public class OkHttpCall<T> implements Call<T>{

    private ServiceMethod mServiceMethod;
    private Object[] args;
    /**
     * 通过代理 返回这个实例， 这个实例会封装一个请求的call
     * @param serviceMethod
     * @param args
     */
    public OkHttpCall(ServiceMethod serviceMethod, Object[] args) {
        this.mServiceMethod = serviceMethod;
        this.args = args;
    }


    @Override
    public void enqueue(Callback<T> callback) {
        //真正的网络请求在这里
        Log.e("TAG","正式发起请求");
        okhttp3.Call newCall = mServiceMethod.createNewCall(args);
        newCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) {
                Response rResponse = new Response();
                rResponse.body = mServiceMethod.parseBody(response.body());
                if (callback != null) {
                    callback.onResponse(OkHttpCall.this, rResponse);
                }
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                if (callback != null) {
                    callback.onFailure(OkHttpCall.this, e);
                }
            }
        });
    }
}
