package com.lchli.angithub.features.me.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lchli.angithub.Navigator;
import com.lchli.angithub.R;
import com.lchli.angithub.common.utils.EventBusUtils;
import com.lchli.angithub.common.utils.ToastUtils;
import com.lchli.angithub.common.widget.CommonEmptyView;
import com.lchli.angithub.features.me.controller.UserController;
import com.lchli.angithub.features.me.bean.CurrentUserInfoResponse;
import com.lchli.angithub.features.me.bean.LoginEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MeFragment extends Fragment {

  @BindView(R.id.empty_view)
  CommonEmptyView emptyView;
  @BindView(R.id.user_portrait)
  ImageView userPortrait;
  @BindView(R.id.user_nick)
  TextView userNick;
  @BindView(R.id.repositories_text_view)
  TextView repositoriesTextView;
  @BindView(R.id.stars_text_view)
  TextView starsTextView;
  @BindView(R.id.followers_text_view)
  TextView followersTextView;
  @BindView(R.id.following_text_view)
  TextView followingTextView;
  @BindView(R.id.logout_widget)
  Button logoutWidget;
  @BindView(R.id.pull_refresh_view)
  PullToRefreshScrollView pullRefreshView;

  private UserController mUserController;

  private UserController.Callback userCb = new UserController.Callback() {
    @Override
    public void onFail(String msg) {
      pullRefreshView.onRefreshComplete();
      ToastUtils.systemToast(msg);
    }

    @Override
    public void onSuccess(CurrentUserInfoResponse data) {
      pullRefreshView.onRefreshComplete();
      refreshUi(data);
    }
  };


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mUserController = new UserController(userCb);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mUserController.unsubscripe();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_me, container, false);
    ButterKnife.bind(this, view);
    EventBusUtils.register(this);

    emptyView.addEmptyText("not login", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Navigator.toLogin(getActivity());
      }
    });
    pullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
      @Override
      public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
          mUserController.loadUserInfo();
      }

      @Override
      public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

      }
    });

    pullRefreshView.setRefreshing(true);
    mUserController.loadUserInfo();
    return view;
  }



  @Override
  public void onDestroyView() {
    super.onDestroyView();
    EventBusUtils.unregister(this);
  }

  private void refreshUi(CurrentUserInfoResponse userInfo) {
    if (userInfo == null) {
      emptyView.show();
    } else {
      emptyView.hide();
      userNick.setText(userInfo.name);
      repositoriesTextView
          .setText(String.format(Locale.ENGLISH, "Repositories %d", userInfo.publicRepos));
      followersTextView.setText(String.format(Locale.ENGLISH, "followers %d", userInfo.followers));
      followingTextView.setText(String.format(Locale.ENGLISH, "following %d", userInfo.following));
      Glide
          .with(this)
          .load(userInfo.avatarUrl)
          .centerCrop()
          .placeholder(R.drawable.indicator_bg_bottom)
          .crossFade()
          .into(userPortrait);

    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onLoginEvent(LoginEvent event) {
    if (event.authInfo != null) {
      refreshUi(event.authInfo);
    }
  }


}
