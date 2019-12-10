package com.jy.retrofitshample.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 封装 网络请求的方法和方法的参数
 */
public class ServiceMethod {


    public ServiceMethod(Builder builder) {

    }

    public static class Builder {
        private final Retrofit retrofit;
        private final Method method;
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
            return new ServiceMethod(this);
        }

        /**
         * 解析方法注解
         * @param annotation
         */
        private void parseMethodAnnotation(Annotation annotation) {
        }
    }
}
