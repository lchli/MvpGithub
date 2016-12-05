package com.lchli.angithub.features.search.bean;

import com.google.gson.annotations.SerializedName;
import com.lchli.angithub.common.base.BaseGithubResponse;

import java.util.List;

/**
 * Created by lchli on 2016/10/29.
 */

public class ReposResponse extends BaseGithubResponse {


    /**
     * total_count : 40
     * incomplete_results : false
     * items : [{"id":3081286,"name":"Tetris","full_name":"dtrupenn/Tetris","owner":{"login":"dtrupenn","id":872147,"avatar_url":"https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png","gravatar_id":"","url":"https://api.github.com/users/dtrupenn","received_events_url":"https://api.github.com/users/dtrupenn/received_events","type":"User"},"private.private":false,"html_url":"https://github.com/dtrupenn/Tetris","description":"A C implementation of Tetris using Pennsim through LC4","fork":false,"url":"https://api.github.com/repos/dtrupenn/Tetris","created_at":"2012-01-01T00:31:50Z","updated_at":"2013-01-05T17:58:47Z","pushed_at":"2012-01-01T00:37:02Z","homepage":"","size":524,"stargazers_count":1,"watchers_count":1,"language":"Assembly","forks_count":0,"open_issues_count":0,"master_branch":"master","default_branch":"master","score":10.309712}]
     */

    @SerializedName("total_count")
    public int totalCount;
    @SerializedName("incomplete_results")
    public boolean incompleteResults;
    /**
     * id : 3081286
     * name : Tetris
     * full_name : dtrupenn/Tetris
     * owner : {"login":"dtrupenn","id":872147,"avatar_url":"https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png","gravatar_id":"","url":"https://api.github.com/users/dtrupenn","received_events_url":"https://api.github.com/users/dtrupenn/received_events","type":"User"}
     * private.private : false
     * html_url : https://github.com/dtrupenn/Tetris
     * description : A C implementation of Tetris using Pennsim through LC4
     * fork : false
     * url : https://api.github.com/repos/dtrupenn/Tetris
     * created_at : 2012-01-01T00:31:50Z
     * updated_at : 2013-01-05T17:58:47Z
     * pushed_at : 2012-01-01T00:37:02Z
     * homepage :
     * size : 524
     * stargazers_count : 1
     * watchers_count : 1
     * language : Assembly
     * forks_count : 0
     * open_issues_count : 0
     * master_branch : master
     * default_branch : master
     * score : 10.309712
     */

    @SerializedName("items")
    public List<Repo> items;

    public static class Repo {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("full_name")
        public String fullName;
        /**
         * login : dtrupenn
         * id : 872147
         * avatar_url : https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png
         * gravatar_id :
         * url : https://api.github.com/users/dtrupenn
         * received_events_url : https://api.github.com/users/dtrupenn/received_events
         * type : User
         */

        @SerializedName("owner")
        public OwnerBean owner;
        @SerializedName("private.private")
        public boolean privateX;
        @SerializedName("html_url")
        public String htmlUrl;
        @SerializedName("description")
        public String description;
        @SerializedName("fork")
        public boolean fork;
        @SerializedName("url")
        public String url;
        @SerializedName("created_at")
        public String createdAt;
        @SerializedName("updated_at")
        public String updatedAt;
        @SerializedName("pushed_at")
        public String pushedAt;
        @SerializedName("homepage")
        public String homepage;
        @SerializedName("size")
        public int size;
        @SerializedName("stargazers_count")
        public int stargazersCount;
        @SerializedName("watchers_count")
        public int watchersCount;
        @SerializedName("language")
        public String language;
        @SerializedName("forks_count")
        public int forksCount;
        @SerializedName("open_issues_count")
        public int openIssuesCount;
        @SerializedName("master_branch")
        public String masterBranch;
        @SerializedName("default_branch")
        public String defaultBranch;
        @SerializedName("score")
        public double score;

        public static class OwnerBean {
            @SerializedName("login")
            public String login;
            @SerializedName("id")
            public int id;
            @SerializedName("avatar_url")
            public String avatarUrl;
            @SerializedName("gravatar_id")
            public String gravatarId;
            @SerializedName("url")
            public String url;
            @SerializedName("received_events_url")
            public String receivedEventsUrl;
            @SerializedName("type")
            public String type;
        }
    }
}
