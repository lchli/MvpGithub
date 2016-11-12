package com.lchli.angithub.features.me.bean;

import com.google.gson.annotations.SerializedName;
import com.lchli.angithub.common.base.BaseGithubResponse;

import java.util.List;

/**
 * Created by lchli on 2016/11/1.
 */

public class AuthResponse extends BaseGithubResponse {


    /**
     * id : 1
     * url : https://api.github.com/authorizations/1
     * scopes : ["public_repo"]
     * token : abcdefgh12345678
     * token_last_eight : 12345678
     * hashed_token : 25f94a2a5c7fbaf499c665bc73d67c1c87e496da8985131633ee0a95819db2e8
     * app : {"url":"http://my-github-app.com","name":"my github app","client_id":"abcde12345fghij67890"}
     * note : optional note
     * note_url : http://optional/note/url
     * updated_at : 2011-09-06T20:39:23Z
     * created_at : 2011-09-06T17:26:27Z
     * fingerprint :
     */

    @SerializedName("id")
    public int id;
    @SerializedName("url")
    public String url;
    @SerializedName("token")
    public String token;
    @SerializedName("token_last_eight")
    public String tokenLastEight;
    @SerializedName("hashed_token")
    public String hashedToken;
    public String userAccount;
    /**
     * url : http://my-github-app.com
     * name : my github app
     * client_id : abcde12345fghij67890
     */

    @SerializedName("app")
    public AppBean app;
    @SerializedName("note")
    public String note;
    @SerializedName("note_url")
    public String noteUrl;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("fingerprint")
    public String fingerprint;
    @SerializedName("scopes")
    public List<String> scopes;

    public static class AppBean {
        @SerializedName("url")
        public String url;
        @SerializedName("name")
        public String name;
        @SerializedName("client_id")
        public String clientId;
    }
}
