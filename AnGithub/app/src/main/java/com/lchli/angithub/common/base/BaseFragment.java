package com.lchli.angithub.common.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;


/**
 * Created by lchli on 2016/8/10.
 */
public abstract class BaseFragment extends Fragment {

  protected boolean isViewCreated = false;
  private Handler handler = new Handler();
  private boolean mIsVisibleToUser = false;


  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    isViewCreated = true;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (mIsVisibleToUser) {
      visibleToUser();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    isViewCreated = false;
  }


  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    mIsVisibleToUser = isVisibleToUser;
    if (isVisibleToUser && isViewCreated) {
      visibleToUser();
    }
  }

  private void visibleToUser() {
    handler.post(new Runnable() {
      @Override
      public void run() {
        whenVisibleToUser();
      }
    });
  }

  protected void whenVisibleToUser() {

  }
}
