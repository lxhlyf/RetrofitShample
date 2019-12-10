package com.jy.retrofitshample.retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;

/**
 * Retrofit 请求入口
 */
public class Retrofit {

    //根地址
    final String mBaseUrl;
    //OkHttp3的请求引擎
    final okhttp3.Call.Factory mCallFactory;
    //存放网络请求方法和方法的参数
    private Map<Method, ServiceMethod> mMethodServiceMethodMapCache = new ConcurrentHashMap<>();

    public Retrofit(Builder builder) {
        this.mBaseUrl = builder.baseUrl;
        this.mCallFactory = builder.callFactory;
    }

    public <T> T create(Class<T> service) {
        //检查接口，不能让它继承了子接口
        validateInterface(service);

        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //判断方法是否是Object方法， 从Object处继承来的方法
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }
                //解析方法，将方法和方法的参数 封装到serviceMethod 中
                ServiceMethod serviceMethod = loadServiceMethod(method);
                //封装成一个OkHttpCall然后返回
                OkHttpCall okHttpCall = new OkHttpCall(serviceMethod, args);
                return okHttpCall;
            }
        });
    }

    private <T> void validateInterface(Class<T> clazz) {
        //判断clazz是否是接口
        if (!clazz.isInterface()) {
            throw new RuntimeException("clazz is not interface");
        }
        //判断clazz是否继承了其他的接口
        if (clazz.getInterfaces().length > 0) {
            throw  new RuntimeException("clazz is not extends other interface");
        }
    }

    private ServiceMethod loadServiceMethod(Method method) {
        //从缓存 Map 中获取方法
        ServiceMethod serviceMethod = mMethodServiceMethodMapCache.get(method);
        if (serviceMethod == null) {
            serviceMethod = new ServiceMethod.Builder(this, method).build();
            mMethodServiceMethodMapCache.put(method, serviceMethod);
        }
        return serviceMethod;
    }

    /**
     * 通过 Build 模式 传递参数
     */
    public static class Builder {
        private String baseUrl;
        private okhttp3.Call.Factory callFactory;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder client(okhttp3.Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Retrofit build() {
            if (callFactory == null) {
                callFactory = new OkHttpClient();
            }
            return new Retrofit(this);
        }
    }
}
