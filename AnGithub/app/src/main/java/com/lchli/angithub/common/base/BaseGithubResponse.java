package com.lchli.angithub.common.base;

import com.google.gson.annotations.SerializedName;
import com.lchli.angithub.common.utils.ListUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lchli on 2016/11/12.
 */

public class BaseGithubResponse implements Serializable {


  /**
   * message : Validation Failed
   * errors : [{"message":"The listed users and repositories cannot be searched either because the
   * resources do not exist or you do not have permission to view
   * them.","resource":"Search","field":"q","code":"invalid"}]
   * documentation_url : https://developer.github.com/v3/search/
   */

  @SerializedName("message")
  public String message;
  @SerializedName("documentation_url")
  public String documentationUrl;
  /**
   * message : The listed users and repositories cannot be searched either because the resources do
   * not exist or you do not have permission to view them.
   * resource : Search
   * field : q
   * code : invalid
   */

  @SerializedName("errors")
  public List<ErrorsBean> errors;

  public boolean isHaveError() {
    return !ListUtils.isEmpty(errors);
  }

  public static class ErrorsBean {
    @SerializedName("message")
    public String message;
    @SerializedName("resource")
    public String resource;
    @SerializedName("field")
    public String field;
    @SerializedName("code")
    public String code;
  }
}
