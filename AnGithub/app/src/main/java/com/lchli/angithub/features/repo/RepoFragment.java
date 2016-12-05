package com.lchli.angithub.features.repo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lchli.angithub.Navigator;
import com.lchli.angithub.R;
import com.lchli.angithub.common.base.ViewPagerFragment;
import com.lchli.angithub.common.constants.ServerConst;
import com.lchli.angithub.common.utils.MapUtils;
import com.lchli.angithub.common.utils.RefreshUtils;
import com.lchli.angithub.common.utils.ToastUtils;
import com.lchli.angithub.common.widget.CommonEmptyView;
import com.lchli.angithub.features.me.bean.CurrentUserInfoResponse;
import com.lchli.angithub.features.me.model.UserAccountRepository;
import com.lchli.angithub.features.search.adapter.SearchReposAdapter;
import com.lchli.angithub.features.search.bean.ReposResponse;
import com.lchli.angithub.features.search.controller.SearchController;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepoFragment extends ViewPagerFragment {


  @BindView(R.id.my_repos_list_view)
  PullToRefreshListView myReposListView;

  private SearchReposAdapter mSearchReposAdapter;
  private SearchController mSearchController;
  private boolean isDataAlreadyLoaded = false;

  private SearchController.Callback searchCb = new SearchController.Callback() {
    @Override
    public void onSucess(List<ReposResponse.Repo> items) {
      if (!isViewCreated) {
        return;
      }
      RefreshUtils.onRefreshComplete(myReposListView);
      mSearchReposAdapter.refresh(items);
    }

    @Override
    public void onFail(String msg) {
      if (!isViewCreated) {
        return;
      }
      RefreshUtils.onRefreshComplete(myReposListView);
      ToastUtils.systemToast(msg);
    }
  };

  public RepoFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSearchController = new SearchController();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_repo, container, false);
    ButterKnife.bind(this, view);

    CommonEmptyView emptyView = new CommonEmptyView(getActivity());
    emptyView.addEmptyText(getString(R.string.empty_data), new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RefreshUtils.setRefreshing(myReposListView, true);
      }
    });

    myReposListView.setEmptyView(emptyView);
    myReposListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
      @Override
      public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        refresh();
      }

      @Override
      public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        loadMore();
      }
    });
    mSearchReposAdapter = new SearchReposAdapter();
    myReposListView.setAdapter(mSearchReposAdapter);

    return view;
  }


  @Override
  protected void whenVisibleToUser() {
    if (isDataAlreadyLoaded) {
      return;
    }
    isDataAlreadyLoaded = true;
    // this will trigger onPullDownToRefresh to load data.
    RefreshUtils.setRefreshing(myReposListView, true);
  }

  private CurrentUserInfoResponse getAccount() {
    CurrentUserInfoResponse account = UserAccountRepository.get().getUserInfo();
    if (account == null) {
      myReposListView.onRefreshComplete();
      Navigator.toLogin(getActivity());
      return null;
    }
    return account;
  }

  private void refresh() {
    CurrentUserInfoResponse account = getAccount();
    if (account == null) {
      return;
    }
    mSearchController.refresh(buildSearchParams(account),searchCb);
  }

  private void loadMore() {
    CurrentUserInfoResponse account = getAccount();
    if (account == null) {
      return;
    }
    mSearchController.loadMore(buildSearchParams(account),searchCb);
  }

  private Map<String, String> buildSearchParams(CurrentUserInfoResponse account) {
    Map<String, String> params = MapUtils.stringMap();
    params.put("q", "user:" + account.login);
    params.put("sort", ServerConst.Repo.SORT_FIELD_UPDATED);
    params.put("order", ServerConst.Repo.SORT_ORDER_DESC);
    return params;
  }

  @Override
  public void onDestroyView() {
    mSearchController.unsubscripe();
    isDataAlreadyLoaded=false;
    super.onDestroyView();
  }
}
