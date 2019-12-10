package com.jy.retrofitshample.Shample;


import com.jy.retrofitshample.retrofit.Call;
import com.jy.retrofitshample.retrofit.http.GET;

/**
 * Created by hcDarren on 2017/12/16.
 * 请求后台访问数据的 接口类
 */
public interface ServiceApi {

    @GET("")
    Call<GankTodayResult> baidu();
}
