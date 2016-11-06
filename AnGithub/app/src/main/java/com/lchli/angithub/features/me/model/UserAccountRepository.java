package com.lchli.angithub.features.me.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lchli.angithub.common.utils.PreferenceUtil;
import com.lchli.angithub.features.me.bean.AuthResponse;

/**
 * Created by lchli on 2016/10/31.
 */

public class UserAccountRepository {

    private static final String SP_ACCOUNT = "sp_account";

    private static final UserAccountRepository INSTANCE = new UserAccountRepository();

    public static UserAccountRepository get() {
        return INSTANCE;
    }

    public AuthResponse getAccount() {
        String accountJson = PreferenceUtil.getString(SP_ACCOUNT, null);
        if (TextUtils.isEmpty(accountJson)) {
            return null;
        }
        return new Gson().fromJson(accountJson, AuthResponse.class);
    }

    public void saveAccount(AuthResponse authResponse) {
        if (authResponse == null) {
            throw new IllegalArgumentException();
        }
        String accountJson = new Gson().toJson(authResponse);
        PreferenceUtil.putString(SP_ACCOUNT, accountJson);
    }

    public void clearAccount(){
        PreferenceUtil.putString(SP_ACCOUNT, "");
    }
}
