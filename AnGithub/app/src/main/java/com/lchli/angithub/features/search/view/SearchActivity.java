package com.lchli.angithub.features.search.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lchli.angithub.R;
import com.lchli.angithub.common.base.BaseAppCompatActivity;
import com.lchli.angithub.common.constants.ServerConst;
import com.lchli.angithub.common.utils.MapUtils;
import com.lchli.angithub.common.utils.ToastUtils;
import com.lchli.angithub.common.widget.CommonEmptyView;
import com.lchli.angithub.features.search.adapter.SearchReposAdapter;
import com.lchli.angithub.features.search.bean.ReposResponse;
import com.lchli.angithub.features.search.controller.SearchController;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseAppCompatActivity {

    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_list_view)
    PullToRefreshListView searchListView;

    private SearchReposAdapter mSearchReposAdapter;
    private String sortField = ServerConst.Repo.SORT_FIELD_DEFAULT;
    private String sortOrder = ServerConst.Repo.SORT_ORDER_ASC;
    private String language = ServerConst.Repo.LANG_ALL;
    private String keyword = null;
    private static final String KEYWORD_LANGUAGE_PATTERN = "%s+language:%s";
    private SearchController mSearchController;

    private SearchController.Callback searchCb = new SearchController.Callback() {
        @Override
        public void onSucess(List<ReposResponse.Repo> items) {
            searchListView.onRefreshComplete();
            mSearchReposAdapter.refresh(items);
        }

        @Override
        public void onFail(String msg) {
            searchListView.onRefreshComplete();
            ToastUtils.systemToast(msg);
        }
    };

    public static Intent getLaunchIntent(Context context) {
        Intent it = new Intent(context, SearchActivity.class);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (language.equals(ServerConst.Repo.LANG_ALL)) {
                    keyword = query;
                } else {
                    keyword = String.format(KEYWORD_LANGUAGE_PATTERN, query, language);
                }
                searchListView.setRefreshing(true);
                refresh();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        CommonEmptyView emptyView = new CommonEmptyView(this);
        emptyView.addEmptyText(getString(R.string.empty_data), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchListView.setRefreshing(true);
                refresh();
            }
        });

        searchListView.setEmptyView(emptyView);
        searchListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mSearchController.loadMore(buildSearchParams());
            }
        });
        mSearchReposAdapter = new SearchReposAdapter();
        searchListView.setAdapter(mSearchReposAdapter);

        mSearchController = new SearchController(searchCb);

    }

    private void refresh() {
        mSearchController.refresh(buildSearchParams());
    }

    private Map<String, String> buildSearchParams() {
        Map<String, String> params = MapUtils.stringMap();
        params.put("q", keyword);
        params.put("sort", sortField);
        params.put("order", sortOrder);
        return params;
    }

    @Override
    public void finish() {
        mSearchController.unsubscripe();
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sorter:
                showSortChoiceDialog();
                break;
            case R.id.action_language:
                showLanguageChoiceDialog();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLanguageChoiceDialog() {
        int currentLangIndex = ArrayUtils.indexOf(ServerConst.Repo.LANGS, language);
        AlertDialog languageDialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(ServerConst.Repo.LANGS, currentLangIndex,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                language = ServerConst.Repo.LANGS[which];
                                dialog.dismiss();
                            }
                        })
                .create();
        languageDialog.show();
    }

    private void showSortChoiceDialog() {
        View view = View.inflate(this, R.layout.search_sort_dialog, null);
        final RadioGroup sortFieldRadioGroup =
                (RadioGroup) view.findViewById(R.id.sort_field_radio_group);
        sortFieldRadioGroup.findViewById(R.id.sort_field_default_radio_button)
                .setTag(ServerConst.Repo.SORT_FIELD_DEFAULT);
        sortFieldRadioGroup.findViewById(R.id.sort_field_star_radio_button)
                .setTag(ServerConst.Repo.SORT_FIELD_STAR);
        sortFieldRadioGroup.findViewById(R.id.sort_field_forks_radio_button)
                .setTag(ServerConst.Repo.SORT_FIELD_FORKS);
        sortFieldRadioGroup.findViewById(R.id.sort_field_update_time_radio_button)
                .setTag(ServerConst.Repo.SORT_FIELD_UPDATED);

        sortFieldRadioGroup.check(sortFieldRadioGroup.findViewWithTag(sortField).getId());

        final RadioGroup sortOrderRadioGroup =
                (RadioGroup) view.findViewById(R.id.sort_order_radio_group);
        sortOrderRadioGroup.findViewById(R.id.sort_order_asc_radio_button)
                .setTag(ServerConst.Repo.SORT_ORDER_ASC);
        sortOrderRadioGroup.findViewById(R.id.sort_order_desc_radio_button)
                .setTag(ServerConst.Repo.SORT_ORDER_DESC);

        sortOrderRadioGroup.check(sortOrderRadioGroup.findViewWithTag(sortOrder).getId());

        AlertDialog sortDialog = new AlertDialog.Builder(this).setView(view)
                .setNegativeButton(R.string.cancel_button, null)
                .setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sortField = (String) sortFieldRadioGroup
                                .findViewById(sortFieldRadioGroup.getCheckedRadioButtonId()).getTag();
                        sortOrder = (String) sortOrderRadioGroup
                                .findViewById(sortOrderRadioGroup.getCheckedRadioButtonId()).getTag();
                    }
                })
                .create();
        sortDialog.show();
    }

}
