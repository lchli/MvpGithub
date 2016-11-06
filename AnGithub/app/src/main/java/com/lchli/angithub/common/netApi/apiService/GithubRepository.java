package com.lchli.angithub.common.netApi.apiService;

import com.lchli.angithub.features.me.bean.AuthPostParam;
import com.lchli.angithub.features.me.bean.AuthResponse;
import com.lchli.angithub.features.me.bean.CurrentUserInfoResponse;
import com.lchli.angithub.features.search.bean.ReposResponse;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by lchli on 2016/10/29.
 */

public interface GithubRepository {

  @GET("/search/repositories")
  Observable<ReposResponse> searchRepo(@QueryMap Map<String, String> params);

  @POST("/authorizations")
  Observable<AuthResponse> authorize(@Body AuthPostParam authPostParam,
                               @Header("Authorization") String basicAuth);

  @GET("/user")
  Observable<CurrentUserInfoResponse> getCurrentUserInfo(@Query("access_token") String token);
}
