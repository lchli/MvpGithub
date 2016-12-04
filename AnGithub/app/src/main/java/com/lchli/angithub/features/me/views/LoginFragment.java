package com.lchli.angithub.features.me.views;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lchli.angithub.R;
import com.lchli.angithub.common.utils.EventBusUtils;
import com.lchli.angithub.common.utils.ToastUtils;
import com.lchli.angithub.features.me.controller.UserController;
import com.lchli.angithub.features.me.bean.CurrentUserInfoResponse;
import com.lchli.angithub.features.me.bean.LoginEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

  @BindView(R.id.login_progress)
  ProgressBar loginProgress;
  @BindView(R.id.email_edit_text)
  AutoCompleteTextView emailEditText;
  @BindView(R.id.password_edit_text)
  EditText passwordEditText;
  @BindView(R.id.email_sign_in_button)
  Button emailSignInButton;
  @BindView(R.id.email_login_form)
  LinearLayout emailLoginForm;
  @BindView(R.id.login_form)
  ScrollView loginForm;

  private UserController mLoginController;

  private UserController.Callback loginCb = new UserController.Callback() {
    @Override
    public void onFail(String msg) {
      showProgress(false);
      ToastUtils.systemToast(msg);
    }

    @Override
    public void onSuccess(CurrentUserInfoResponse data) {
      showProgress(false);
      EventBusUtils.post(new LoginEvent(data));
      getActivity().finish();
    }
  };


  public LoginFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mLoginController = new UserController();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mLoginController.unsubscripe();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.login_fragment, container, false);
    ButterKnife.bind(this, view);

    passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
          attemptLogin();
          return true;
        }
        return false;
      }
    });
    return view;
  }



  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  private void attemptLogin() {

    emailEditText.setError(null);
    passwordEditText.setError(null);

    String userName = emailEditText.getText().toString();
    if (TextUtils.isEmpty(userName)) {
      emailEditText.setError(getString(R.string.error_invalid_email));
      emailEditText.requestFocus();
      return;
    }
    String password = passwordEditText.getText().toString();
    if (TextUtils.isEmpty(password)) {
      passwordEditText.setError(getString(R.string.error_invalid_password));
      passwordEditText.requestFocus();
      return;
    }

    showProgress(true);
    mLoginController.login(userName, password,loginCb);

  }


  /**
   * Shows the progress UI and hides the login form.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  private void showProgress(final boolean show) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
      loginForm.animate().setDuration(shortAnimTime).alpha(
          show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
          });

      loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
      loginProgress.animate().setDuration(shortAnimTime).alpha(
          show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            }
          });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
      loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
    }
  }


  @OnClick(R.id.email_sign_in_button)
  public void onClick() {
    attemptLogin();
  }

}
