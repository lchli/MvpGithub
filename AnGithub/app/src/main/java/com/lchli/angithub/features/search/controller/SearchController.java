package com.lchli.angithub.features.search.controller;

import com.lchli.angithub.R;
import com.lchli.angithub.common.constants.ServerConst;
import com.lchli.angithub.common.netApi.RestClient;
import com.lchli.angithub.common.netApi.apiService.GithubRepository;
import com.lchli.angithub.common.utils.ListUtils;
import com.lchli.angithub.common.utils.Preconditions;
import com.lchli.angithub.common.utils.ResUtils;
import com.lchli.angithub.common.utils.UniversalLog;
import com.lchli.angithub.features.search.bean.ReposResponse;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by lchli on 2016/10/29.
 */

public class SearchController {

  private List<ReposResponse.Repo> repoList = new ArrayList<>();
  private int page = 1;
  private static final int PAGE_SIZE = 10;
  private boolean isHaveMore = true;
  private Callback mCallback;
  private final CompositeSubscription mCompositeSubscription;

  public SearchController(Callback mCallback) {
    this.mCallback = Preconditions.checkNotNull(mCallback);
    mCompositeSubscription = new CompositeSubscription();
  }

  public void refresh(Map<String, String> params) {
    page = 1;
    isHaveMore = true;
    repoList.clear();

    search(params);
  }

  private void search(Map<String, String> params) {
    assertNotNull(params);
    if (params.get("q") == null) {
      mCallback.onFail(ResUtils.parseString(R.string.search_key_cannot_null));
      return;
    }
    Preconditions
        .checkArgument(ArrayUtils.indexOf(ServerConst.Repo.SORT_FIELDS, params.get("sort")) != -1);
    Preconditions
        .checkArgument(ArrayUtils.indexOf(ServerConst.Repo.SORT_ORDERS, params.get("order")) != -1);

    params.put("page", page + "");
    params.put("per_page", PAGE_SIZE + "");

    unsubscripe();
    Subscription subscription =
        RestClient.instance().createService(GithubRepository.class).searchRepo(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ReposResponse>() {
              @Override
              public void onCompleted() {}

              @Override
              public void onError(Throwable e) {
                UniversalLog.get().e(e);
                mCallback.onFail(e.getMessage());
              }

              @Override
              public void onNext(ReposResponse reposResponse) {
                if (reposResponse == null) {
                  mCallback.onFail("response is null.");
                  return;
                }
                if (reposResponse.isHaveError()) {
                  mCallback.onFail(reposResponse.message);
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
                mCallback.onSucess(repoList);

              }
            });

    mCompositeSubscription.add(subscription);
  }

  public void loadMore(Map<String, String> params) {
    if (!isHaveMore) {
      mCallback.onFail(ResUtils.parseString(R.string.load_no_more_data));
      return;
    }
    search(params);
  }

  public void unsubscripe() {
    mCompositeSubscription.clear();
  }

  public interface Callback {

    void onSucess(List<ReposResponse.Repo> items);

    void onFail(String msg);

  }
}
