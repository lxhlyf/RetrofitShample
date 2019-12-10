package com.jy.retrofitshample.retrofit;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class RequestBuilder {

    ParameterHandler<Object>[] parameterHandlers;
    Object[] args;
    HttpUrl.Builder httpUrl;

    public RequestBuilder(String baseUrl, String relativeUrl, String httpMethod, ParameterHandler<?>[] parameterHandlers, Object[] args) {
        this.parameterHandlers = (ParameterHandler<Object>[]) parameterHandlers;
        this.args = args;
        httpUrl = HttpUrl.parse(baseUrl+relativeUrl).newBuilder();
    }

    public Request build() {
        if (args != null) {
            int count = args.length;
            for (int i=0; i<count; i++) {
                parameterHandlers[i].apply(this, args[i]);
            }
        }
        return new Request.Builder().url(httpUrl.build()).build();
    }

    public void addQueryParameter(String key, String value) {
        httpUrl.addQueryParameter(key, value);
    }
}
