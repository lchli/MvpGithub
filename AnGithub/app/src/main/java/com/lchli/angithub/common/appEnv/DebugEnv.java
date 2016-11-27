package com.lchli.angithub.common.appEnv;

import com.lchli.angithub.common.constants.Urlconst;

/**
 * Created by lchli on 2016/10/26.
 */

public class DebugEnv implements AppEnvironment {
    @Override
    public String getIP() {
        return Urlconst.Hosts.GITHUB_DEBUG;
    }

    @Override
    public boolean logFlag() {
        return true;
    }
}
