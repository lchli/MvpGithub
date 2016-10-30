package com.lchli.angithub.common.netApi.apiService;

import com.lchli.angithub.features.bean.ReposResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by lchli on 2016/10/29.
 */

public interface GithubService {

    @GET("/search/repositories")
    Observable<ReposResponse> searchRepo(@QueryMap Map<String, String> params);
}
