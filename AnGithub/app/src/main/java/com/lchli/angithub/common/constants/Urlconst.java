package com.lchli.angithub.common.constants;

import com.lchli.angithub.common.appEnv.AppEnvironmentFactory;

/**
 * Created by lchli on 2016/11/12.
 */

public class Urlconst {

  public interface Hosts {
    String GITHUB_DEBUG = "https://api.github.com";
    String GITHUB_RELEASE = "https://api.github.com";
  }

  public interface Github {

    String HOST = AppEnvironmentFactory.ins().getIP();
    /**
     * 搜索仓库。
     */
    String PATH_SEARCH_REPOSITORIES = "/search/repositories";
    /**
     * 用户登录验证。
     */
    String PATH_AUTHORIZATIONS = "/authorizations";
    /**
     * 获取用户信息。
     */
    String PATH_USER = "/user";
  }
}
