package com.jy.retrofitshample.retrofit;

/**
 * 封装 OkHttp的 网络请求方式
 */
public interface Call<T> {
    /*
     * 异步请求网络
     */
    void enqueue(Callback<T> callback);
}
