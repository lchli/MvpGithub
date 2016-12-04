package com.lchli.angithub.features.search.controller;

import com.lchli.angithub.R;
import com.lchli.angithub.common.base.BaseLoader;
import com.lchli.angithub.common.constants.ServerConst;
import com.lchli.angithub.common.netApi.RestClient;
import com.lchli.angithub.common.netApi.apiService.GithubRepository;
import com.lchli.angithub.common.netApi.callbacks.RetrofitCallback;
import com.lchli.angithub.common.utils.ListUtils;
import com.lchli.angithub.common.utils.Preconditions;
import com.lchli.angithub.common.utils.ResUtils;
import com.lchli.angithub.features.search.bean.ReposResponse;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by lchli on 2016/10/29.
 */

public class SearchController extends BaseLoader {

  private List<ReposResponse.Repo> repoList = new ArrayList<>();
  private int page = 1;
  private static final int PAGE_SIZE = 10;
  private boolean isHaveMore = true;
  private Call<ReposResponse> searchCall;


  public void refresh(Map<String, String> params, Callback mCallback) {

    page = 1;
    isHaveMore = true;
    repoList.clear();

    search(params, mCallback);
  }

  private void search(Map<String, String> params, Callback callback) {

    assertNotNull(params);
    if (params.get("q") == null) {
        callback.onFail(ResUtils.parseString(R.string.search_key_cannot_null));
      return;
    }
    Preconditions
        .checkArgument(ArrayUtils.indexOf(ServerConst.Repo.SORT_FIELDS, params.get("sort")) != -1);
    Preconditions
        .checkArgument(ArrayUtils.indexOf(ServerConst.Repo.SORT_ORDERS, params.get("order")) != -1);

    params.put("page", page + "");
    params.put("per_page", PAGE_SIZE + "");

    if (searchCall != null) {
      searchCall.cancel();
      searchCall = null;
    }

    Preconditions.checkNotNull(callback);
    final WeakReference<Callback> cbRef = weakRefCallback(callback);

    searchCall = RestClient.instance().createService(GithubRepository.class).searchRepo(params);
    searchCall.enqueue(new RetrofitCallback<ReposResponse>() {
      @Override
      public void onSuccess(ReposResponse reposResponse) {

        if (reposResponse.isHaveError()) {
          Callback cb = cbRef.get();
          if (cb != null) {
            cb.onFail(reposResponse.message);
          }
          return;
        }
        if (reposResponse.items == null || reposResponse.items.size() < PAGE_SIZE) {
          isHaveMore = false;
        } else {
          page++;
        }
        if (!ListUtils.isEmpty(reposResponse.items)) {
          repoList.addAll(reposResponse.items);
        }
        Callback cb = cbRef.get();
        if (cb != null) {
          cb.onSucess(repoList);
        }
      }

      @Override
      public void onFail(Throwable error) {
        Callback cb = cbRef.get();
        if (cb != null) {
          cb.onFail(error.getMessage());
        }
      }
    });


  }

  public void loadMore(Map<String, String> params, Callback mCallback) {
    if (!isHaveMore) {
      mCallback.onFail(ResUtils.parseString(R.string.load_no_more_data));
      return;
    }
    search(params, mCallback);
  }

  public void unsubscripe() {

    if (searchCall != null) {
      searchCall.cancel();
      searchCall = null;
    }

  }

  public interface Callback {

    void onSucess(List<ReposResponse.Repo> items);

    void onFail(String msg);

  }
}
