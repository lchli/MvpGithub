package com.lchli.angithub.common.netApi;

import com.lchli.angithub.common.config.AppEnvironmentFactory;
import com.lchli.angithub.common.netApi.apiService.GithubService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lchli on 2016/10/29.
 */

public class RestClient {

    private GithubService mGithubService;
    private HostSelectionInterceptor hostSelectionInterceptor;
    private Scheduler defaultSubscribeScheduler;
    private Scheduler defaultObserverScheduler;

    private static class RestClientHolder {
        private static final RestClient CLIENT = new RestClient();
    }

    private RestClient() {

    }

    public static final RestClient instance() {
        return RestClientHolder.CLIENT;
    }

    private void createGithubService() {
        hostSelectionInterceptor = new HostSelectionInterceptor();
        OkHttpClient okHttpClient = OKClientProvider.getHttpClientBuilder()
                .addInterceptor(hostSelectionInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpUrl.parse(AppEnvironmentFactory.getEnv().getIP()))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .callFactory(okHttpClient)
                .build();
        mGithubService = retrofit.create(GithubService.class);
    }

    public GithubService getGithubService(String baseUrl) {
        if (mGithubService == null) {
            createGithubService();
        }
        if (baseUrl != null && hostSelectionInterceptor != null) {
            hostSelectionInterceptor.setHost(baseUrl);
        }
        return mGithubService;
    }

    public GithubService getGithubService() {
        return getGithubService(null);
    }

    /**
     * It is used to junit test.
     *
     * @param testGithubService
     */
    public void setTestGithubService(GithubService testGithubService) {
        mGithubService = testGithubService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    public Scheduler defaultObserverScheduler() {
        if (defaultObserverScheduler == null) {
            defaultObserverScheduler = AndroidSchedulers.mainThread();
        }
        return defaultObserverScheduler;
    }

    /**
     * It is used to junit test.
     */
    public void setTestObserverScheduler(Scheduler defaultObserverScheduler) {
        this.defaultObserverScheduler = defaultObserverScheduler;
    }

    /**
     * It is used to junit test.
     */
    public void setTestSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }

    private static final class HostSelectionInterceptor implements Interceptor {

        private volatile String host;

        public void setHost(String host) {
            this.host = host;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String host = this.host;
            if (host != null) {
                HttpUrl newUrl = request.url().newBuilder()
                        .host(host)
                        .build();
                request = request.newBuilder()
                        .url(newUrl)
                        .build();
            }
            return chain.proceed(request);
        }
    }
}
