package com.lchli.angithub.features.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lchli.angithub.R;
import com.lchli.angithub.common.base.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lchli on 2016/11/7.
 */

public class WebviewActivity extends BaseAppCompatActivity {

  private static final String EXTRA_URL = "url";

  @BindView(R.id.webview)
  WebView webview;

  public static Intent getLaunchIntent(Context context, String url) {
    Intent it = new Intent(context, WebviewActivity.class);
    it.putExtra(EXTRA_URL, url);
    return it;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrow_left);
    }

    setContentView(R.layout.webview_act);
    ButterKnife.bind(this);

    configWebview();

    String url = getIntent().getStringExtra(EXTRA_URL);
    webview.loadUrl(url);

  }

  private void configWebview() {
    webview.getSettings().setJavaScriptEnabled(true);
    webview.getSettings().setSupportZoom(true);
    webview.getSettings().setBuiltInZoomControls(true);
    webview.getSettings().setUseWideViewPort(true);
    // 自适应屏幕
    webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    webview.getSettings().setLoadWithOverviewMode(true);
    webview.setWebViewClient(new WebViewClientImpl());

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (webview.canGoBack()) {
      webview.goBack();
      return;
    }
    super.onBackPressed();
  }

  @Override
  public void finish() {
    super.finish();
    destroyWebview();
  }

  private void destroyWebview() {
    if (webview.getParent() != null) {
      ((ViewGroup) webview.getParent()).removeView(webview);
      webview.destroy();
    }
  }

  private class WebViewClientImpl extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      return true;
    }

    @TargetApi(21)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
      view.loadUrl(request.getUrl().toString());
      return true;
    }
  }
}
