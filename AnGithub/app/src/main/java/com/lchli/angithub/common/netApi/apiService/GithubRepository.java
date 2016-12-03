package com.lchli.angithub.common.netApi.apiService;

import com.lchli.angithub.common.constants.Urlconst;
import com.lchli.angithub.features.me.bean.AuthPostParam;
import com.lchli.angithub.features.me.bean.AuthResponse;
import com.lchli.angithub.features.me.bean.CurrentUserInfoResponse;
import com.lchli.angithub.features.search.bean.ReposResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by lchli on 2016/10/29.
 */

public interface GithubRepository {

  @GET(Urlconst.Github.PATH_SEARCH_REPOSITORIES)
  Call<ReposResponse> searchRepo(@QueryMap Map<String,String> params);

  @POST(Urlconst.Github.PATH_AUTHORIZATIONS)
  Call<AuthResponse> authorize(@Body AuthPostParam authPostParam,
      @Header("Authorization") String basicAuth);

  @GET(Urlconst.Github.PATH_USER)
  Call<CurrentUserInfoResponse> getCurrentUserInfo(@Query("access_token") String token);
}
