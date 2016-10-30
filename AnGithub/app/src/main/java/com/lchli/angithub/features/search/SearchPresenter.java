package com.lchli.angithub.features.search;

import com.apkfuns.logutils.LogUtils;
import com.lchli.angithub.common.utils.ExceptionLoger;
import com.lchli.angithub.R;
import com.lchli.angithub.common.constants.ServerConst;
import com.lchli.angithub.common.mvp.MvpView;
import com.lchli.angithub.common.mvp.Presenter;
import com.lchli.angithub.common.netApi.RestClient;
import com.lchli.angithub.common.utils.ListUtils;
import com.lchli.angithub.common.utils.ResUtils;
import com.lchli.angithub.features.bean.ReposResponse;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by lchli on 2016/10/29.
 */

public class SearchPresenter implements Presenter<SearchPresenter.SearchMvpView> {

    private SearchMvpView mSearchMvpView;
    private Subscription subscription;
    private List<ReposResponse.Repo> repoList = new ArrayList<>();
    private int page = 1;
    private static final int PAGE_SIZE = 10;
    private boolean isHaveMore = true;

    @Override
    public void attachView(SearchMvpView view) {
        mSearchMvpView = view;
    }

    @Override
    public void detachView() {
        mSearchMvpView = null;
        if (subscription != null) {
            subscription.unsubscribe();
        }
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
            mSearchMvpView.toastMsg(ResUtils.parseString(R.string.search_key_cannot_null));
            mSearchMvpView.onLoadFinish();
            return;
        }
        assertTrue(ArrayUtils.indexOf(ServerConst.Repo.SORT_FIELDS, params.get("sort")) != -1);
        assertTrue(ArrayUtils.indexOf(ServerConst.Repo.SORT_ORDERS, params.get("order")) != -1);

        if (subscription != null) {
            subscription.unsubscribe();
        }
        params.put("page", page + "");
        params.put("per_page", PAGE_SIZE + "");
        subscription = RestClient.instance().getGithubService().searchRepo(params)
                .subscribeOn(RestClient.instance().defaultSubscribeScheduler())
                .observeOn(RestClient.instance().defaultObserverScheduler())
                .subscribe(new Observer<ReposResponse>() {
                    @Override
                    public void onCompleted() {
                        mSearchMvpView.onLoadFinish();
                        mSearchMvpView.onSucess(repoList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSearchMvpView.onLoadFinish();
                        ExceptionLoger.logException(e);
                        mSearchMvpView.toastMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(ReposResponse reposResponse) {
                        LogUtils.e(reposResponse);
                        if (reposResponse.items == null || reposResponse.items.size() < PAGE_SIZE) {
                            isHaveMore = false;
                        } else {
                            page++;
                        }
                        if (!ListUtils.isEmpty(reposResponse.items)) {
                            repoList.addAll(reposResponse.items);
                        }

                    }
                });
    }

    public void loadMore(Map<String, String> params) {
        if (!isHaveMore) {
            mSearchMvpView.toastMsg(ResUtils.parseString(R.string.load_no_more_data));
            mSearchMvpView.onLoadFinish();
            return;
        }
        search(params);
    }

    public interface SearchMvpView extends MvpView {


        void toastMsg(String msg);

        void onSucess(List<ReposResponse.Repo> items);

        void onLoadFinish();

    }
}
