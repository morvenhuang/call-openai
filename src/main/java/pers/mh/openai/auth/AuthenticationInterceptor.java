package pers.mh.openai.auth;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthenticationInterceptor implements Interceptor {

    private final String key;

    public AuthenticationInterceptor(String key) {
        this.key = key;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("Authorization", "Bearer " + key)
                .build();
        return chain.proceed(request);
    }
}
