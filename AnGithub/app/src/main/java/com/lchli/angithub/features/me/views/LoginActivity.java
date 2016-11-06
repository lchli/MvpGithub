package com.lchli.angithub.features.me.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.lchli.angithub.R;
import com.lchli.angithub.common.base.BaseAppCompatActivity;

import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseAppCompatActivity {



  public static Intent getLaunchIntent(Context context) {
    Intent it = new Intent(context, LoginActivity.class);
    return it;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);

    LoginFragment loginFragment =
        (LoginFragment) LoginFragment.instantiate(this, LoginFragment.class.getName());
    FragmentTransaction tranc = getSupportFragmentManager().beginTransaction();
    tranc.replace(R.id.contentFrame, loginFragment);
    tranc.commit();

  }

  @Override
  public void finish() {
    super.finish();
  }

}

