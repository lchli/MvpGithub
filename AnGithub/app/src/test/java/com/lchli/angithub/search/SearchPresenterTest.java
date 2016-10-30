package com.lchli.angithub.search;

import com.lchli.angithub.BuildConfig;
import com.lchli.angithub.GithubApp;
import com.lchli.angithub.R;
import com.lchli.angithub.common.constants.ServerConst;
import com.lchli.angithub.common.netApi.RestClient;
import com.lchli.angithub.common.netApi.apiService.GithubService;
import com.lchli.angithub.common.utils.ContextProvider;
import com.lchli.angithub.common.utils.MapUtils;
import com.lchli.angithub.features.bean.ReposResponse;
import com.lchli.angithub.features.search.SearchPresenter;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Created by lchli on 2016/10/30.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SearchPresenterTest {

    SearchPresenter mSearchPresenter;
    SearchPresenter.SearchMvpView mSearchMvpView;
    GithubService githubService;
    GithubApp app;

    @Before
    public void setUp() {
        app = (GithubApp) RuntimeEnvironment.application;
        ContextProvider.initContext(app);
        githubService = mock(GithubService.class);
        RestClient.instance().setTestGithubService(githubService);
        RestClient.instance().setTestSubscribeScheduler(Schedulers.immediate());
        RestClient.instance().setTestObserverScheduler(Schedulers.immediate());

        mSearchPresenter = new SearchPresenter();
        mSearchMvpView = mock(SearchPresenter.SearchMvpView.class);
        when(mSearchMvpView.getContext()).thenReturn(application);
        mSearchPresenter.attachView(mSearchMvpView);
    }

    @After
    public void tearDown() {
        mSearchPresenter.detachView();
    }

    @Test
    public void refreshTest() {
        Map<String, String> params = MapUtils.stringMap();
        params.put("q", null);
        params.put("sort", null);
        params.put("order", null);

        mSearchPresenter.refresh(params);
        verify(mSearchMvpView).toastMsg(app.getString(R.string.search_key_cannot_null));
        verify(mSearchMvpView).onLoadFinish();

        params.put("q", "123");
        try {
            mSearchPresenter.refresh(params);
            Assert.fail("expected exception,but no hanppen.");
        } catch (AssertionFailedError e) {

        }

        params.put("sort", "1");
        params.put("order", "3");
        try {
            mSearchPresenter.refresh(params);
            Assert.fail("expected exception,but no hanppen.");
        } catch (AssertionFailedError e) {

        }

        params.put("q", "123");
        params.put("sort", ServerConst.Repo.SORT_FIELD_DEFAULT);
        params.put("order", ServerConst.Repo.SORT_ORDER_ASC);
        final List<ReposResponse.Repo> list = new ArrayList<>();
        list.add(new ReposResponse.Repo());

        when(githubService.searchRepo(params))
                .thenReturn(Observable.create(new Observable.OnSubscribe<ReposResponse>() {
                    @Override
                    public void call(Subscriber<? super ReposResponse> subscriber) {
                        ReposResponse response = new ReposResponse();
                        response.items = list;
                        subscriber.onNext(response);
                        subscriber.onCompleted();
                    }
                }));
//        mSearchPresenter.refresh(params);
//        verify(mSearchMvpView).onLoadFinish();
//        verify(mSearchMvpView).onSucess(list);
    }


}
