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
import android.widget.RelativeLayout;

import com.lchli.angithub.Navigator;
import com.lchli.angithub.R;
import com.lchli.angithub.common.base.BaseReactActivity;
import com.lchli.angithub.common.base.FragmentAdapter;
import com.lchli.angithub.common.netApi.FileClient;
import com.lchli.angithub.features.events.EventsFragment;
import com.lchli.angithub.features.me.views.MeFragment;
import com.lchli.angithub.features.repo.RepoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseReactActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.viewpager)
  ViewPager viewpager;
  @BindView(R.id.tabs)
  TabLayout tabs;
  @BindView(R.id.content_home)
  RelativeLayout contentHome;


  private FileClient.CancelableRunnable downloadRunnable;


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

    tabs.setupWithViewPager(viewpager);

  }

  @Override
  public void finish() {
    if (downloadRunnable != null) {
      downloadRunnable.cancel();
    }
    super.finish();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
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
               // Navigator.toRepoDetail(activity());
                break;
            }

          }
        })
        .create();
    dialog.show();
  }

}
