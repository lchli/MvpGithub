package com.lchli.angithub.features.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lchli.angithub.Navigator;
import com.lchli.angithub.R;
import com.lchli.angithub.common.base.AbsAdapter;
import com.lchli.angithub.features.search.bean.ReposResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lchli on 2016/10/30.
 */

public class SearchReposAdapter extends AbsAdapter<ReposResponse.Repo> {

  @Override
  public void onBindViewHolder(AbsViewHolder holder, int position) {
    final ReposResponse.Repo repo = getItem(position);
    ViewHolder vh = (ViewHolder) holder;
    vh.repoFullNameTextView.setText(repo.fullName + "");
    vh.repoDesTextView.setText(repo.description + "");
    vh.repoUpdateTimeTextView.setText(repo.updatedAt + "");
    vh.starsCountTextView.setText(repo.stargazersCount + "");
    vh.forkCountTextView.setText(repo.forksCount + "");
    vh.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Navigator.toWebview(v.getContext(), repo.htmlUrl);
      }
    });
  }

  @Override
  public AbsAdapter.AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.repo_list_item, parent, false);
    return new ViewHolder(viewType, view);
  }

  static class ViewHolder extends AbsAdapter.AbsViewHolder {

    @BindView(R.id.repo_full_name_text_view)
    TextView repoFullNameTextView;
    @BindView(R.id.repo_des_text_view)
    TextView repoDesTextView;
    @BindView(R.id.repo_update_time_text_view)
    TextView repoUpdateTimeTextView;
    @BindView(R.id.stars_count_text_view)
    TextView starsCountTextView;
    @BindView(R.id.fork_count_text_view)
    TextView forkCountTextView;

    public View itemView;

    public ViewHolder(int viewType, View itemView) {
      super(viewType);
      this.itemView = itemView;
      ButterKnife.bind(this, itemView);
    }

    @Override
    protected View getItemView() {
      return itemView;
    }
  }
}
