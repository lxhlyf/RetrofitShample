package com.jy.retrofitshample.retrofit;

import com.google.gson.Gson;
import com.jy.retrofitshample.retrofit.http.GET;
import com.jy.retrofitshample.retrofit.http.POST;
import com.jy.retrofitshample.retrofit.http.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * 封装 网络请求的方法和方法的参数
 */
public class ServiceMethod {

    private Retrofit mRetrofit;
    private Method mMethod;
    private String mHttpMethod;
    private String mRelativeString;
    private ParameterHandler<?>[] mParameterHandlers;

    public ServiceMethod(Builder builder) {
        this.mRetrofit = builder.retrofit;
        this.mMethod = builder.method;
        this.mHttpMethod = builder.httpMethod;
        this.mRelativeString = builder.relativeString;
        this.mParameterHandlers = builder.parameterHandlers;
    }

    public Call createNewCall(Object[] args) {
        RequestBuilder builder = new RequestBuilder(mRetrofit.mBaseUrl, mRelativeString, mHttpMethod, mParameterHandlers, args);
        return mRetrofit.mCallFactory.newCall(builder.build());
    }

    public <T> T parseBody(ResponseBody responseBody) {
        Type returnType = mMethod.getGenericReturnType();

        Class<T> dateClass = (Class<T>)((ParameterizedType) returnType).getActualTypeArguments()[0];
        Gson gson = new Gson();
        T body = gson.fromJson(responseBody.charStream(), dateClass);
        return body;
    }

    public static class Builder {
        private final Retrofit retrofit;
        private final Method method;
        private String httpMethod;
        private String relativeString;
        private final Annotation[] annotations;
        Annotation[][] parameterAnnotations;
        final ParameterHandler<?>[] parameterHandlers;

        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            //获取方法上的注解
            annotations = method.getAnnotations();
            //获取方法的参数注解
            parameterAnnotations = method.getParameterAnnotations();
            //对应于参数注解， 每个注解和参数被封装成 ParameterHandler
            parameterHandlers = new ParameterHandler[parameterAnnotations.length];
        }

        public ServiceMethod build() {
            //分析方法注解  请求方法
            for (Annotation annotation : annotations) {
                parseMethodAnnotation(annotation);
            }

            //分析方法参数注解
            int count  = parameterHandlers.length;
            for (int i = 0; i < count; i++) {
                //注解上的注解
                Annotation parameter = parameterAnnotations[i][0];

                //如果是Query注解
                if (parameter instanceof Query) {
                    parameterHandlers[i] = new ParameterHandler.Query<>(((Query) parameter).value());
                }
            }
            return new ServiceMethod(this);
        }

        /**
         * 解析方法注解
         * @param annotation
         */
        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof GET) {
                parseMethodAndPath("GET", ((GET) annotation).value());
            } else if (annotation instanceof POST) {
                parseMethodAndPath("POST", ((POST) annotation).value());
            }
        }

        /**
         * 每一次的请求只有可能是一种方法。
         * @param httpMethod
         * @param path
         */
        private void parseMethodAndPath(String httpMethod, String path) {
            this.httpMethod = httpMethod;
            this.relativeString = path;
        }
    }
}
