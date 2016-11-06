package com.lchli.angithub.common.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lchli on 2016/8/10.
 */
public class BaseAppCompatActivity extends AppCompatActivity {

  @Override
  public void finish() {
    cleanup();
    super.finish();
  }

  protected Activity activity() {
    return this;
  }

  /**
   * this is called when finish.
   */
  protected void cleanup() {

  }
}
