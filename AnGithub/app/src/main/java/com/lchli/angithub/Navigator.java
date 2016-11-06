package com.lchli.angithub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.lchli.angithub.features.me.views.LoginActivity;
import com.lchli.angithub.features.search.view.SearchActivity;

/**
 * this is a medicator.
 * Created by lchli on 2016/10/29.
 */

public class Navigator {

    public static void toSearchRepo(Context context) {
        Intent it = SearchActivity.getLaunchIntent(context);
        if (!(context instanceof Activity)) {
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(it);
    }

    public static void toLogin(Context context) {
        Intent it = LoginActivity.getLaunchIntent(context);
        if (!(context instanceof Activity)) {
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(it);
    }
}
