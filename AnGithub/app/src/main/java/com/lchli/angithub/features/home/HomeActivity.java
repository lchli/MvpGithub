package com.lchli.angithub.features.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lchli.angithub.Navigator;
import com.lchli.angithub.R;
import com.lchli.angithub.common.base.BaseAppCompatActivity;
import com.lchli.angithub.common.base.BaseFragment;
import com.lchli.angithub.common.base.FragmentAdapter;
import com.lchli.angithub.common.constants.LocalConst;
import com.lchli.angithub.common.netApi.FileClient;
import com.lchli.angithub.common.utils.ToastUtils;
import com.lchli.angithub.common.utils.UniversalLog;
import com.lchli.angithub.features.events.EventsFragment;
import com.lchli.angithub.features.me.views.MeFragment;
import com.lchli.angithub.features.repo.RepoFragment;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class HomeActivity extends BaseAppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.viewpager)
  ViewPager viewpager;
  @BindView(R.id.tabs)
  TabLayout tabs;
  @BindView(R.id.content_home)
  RelativeLayout contentHome;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    final FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
    adapter.addFragment(Fragment.instantiate(this, RepoFragment.class.getName()), "myRepos");
    adapter.addFragment(Fragment.instantiate(this, EventsFragment.class.getName()), "event");
    adapter.addFragment(Fragment.instantiate(this, MeFragment.class.getName()), "me");
    viewpager.setAdapter(adapter);
    viewpager.setOffscreenPageLimit(adapter.getCount());
    viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        Fragment current = adapter.getItem(position);
        if (current != null && current instanceof BaseFragment) {
          BaseFragment fragment = (BaseFragment) current;
          if (!fragment.isInitLoadDataCalled) {
            fragment.initLoadData();
          }
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    tabs.setupWithViewPager(viewpager);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_search) {
      showSearchChoiceDialog();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void showSearchChoiceDialog() {
    AlertDialog dialog = new AlertDialog.Builder(this)
        .setItems(R.array.search_types, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            switch (which) {
              case 0:
                Navigator.toSearchRepo(activity());
                break;
              case 1:
                final String url = "http://" + "192.168.1.2" + ":9090" + "/UserPortraitDir/1.mp4";
                FileClient.downloadFile(url, new FileCallBack(LocalConst.SD_PATH, "2.mp4") {
                  @Override
                  public void onError(Call call, Exception e, int id) {
                    UniversalLog.get().e(e);
                    ToastUtils.systemToast(e.getMessage());
                  }

                  @Override
                  public void onResponse(File response, int id) {
                    ToastUtils.systemToast("download onSuccess.");
                  }

                  @Override
                  public void inProgress(float progress, long total, int id) {
                    final int p= (int) (progress*100);
                    UniversalLog.get().e(p+":"+Thread.currentThread().getName());
                    progressBar.setProgress(p);
                  }
                });

                break;
            }

          }
        })
        .create();
    dialog.show();
  }

}
