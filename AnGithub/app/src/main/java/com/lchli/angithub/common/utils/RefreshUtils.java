package com.lchli.angithub.common.utils;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by lchli on 2016/11/12.
 */

public class RefreshUtils {

  public static void setRefreshing(final PullToRefreshBase pullToRefreshBase, final boolean b) {
    UiHandler.post(new Runnable() {
      @Override
      public void run() {
        pullToRefreshBase.setRefreshing(b);
      }
    });
  }

  public static void onRefreshComplete(final PullToRefreshBase pullToRefreshBase) {
    UiHandler.post(new Runnable() {
      @Override
      public void run() {
        pullToRefreshBase.onRefreshComplete();
      }
    });
  }
}
