package com.lchli.angithub.features.me.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lchli on 2016/11/2.
 */

public class CurrentUserInfoResponse {


    /**
     * login : octocat
     * id : 1
     * avatar_url : https://github.com/images/error/octocat_happy.gif
     * gravatar_id :
     * url : https://api.github.com/users/octocat
     * html_url : https://github.com/octocat
     * followers_url : https://api.github.com/users/octocat/followers
     * following_url : https://api.github.com/users/octocat/following{/other_user}
     * gists_url : https://api.github.com/users/octocat/gists{/gist_id}
     * starred_url : https://api.github.com/users/octocat/starred{/owner}{/repo}
     * subscriptions_url : https://api.github.com/users/octocat/subscriptions
     * organizations_url : https://api.github.com/users/octocat/orgs
     * repos_url : https://api.github.com/users/octocat/repos
     * events_url : https://api.github.com/users/octocat/events{/privacy}
     * received_events_url : https://api.github.com/users/octocat/received_events
     * type : User
     * site_admin : false
     * name : monalisa octocat
     * company : GitHub
     * blog : https://github.com/blog
     * location : San Francisco
     * email : octocat@github.com
     * hireable : false
     * bio : There once was...
     * public_repos : 2
     * public_gists : 1
     * followers : 20
     * following : 0
     * created_at : 2008-01-14T04:33:35Z
     * updated_at : 2008-01-14T04:33:35Z
     * total_private_repos : 100
     * owned_private_repos : 100
     * private_gists : 81
     * disk_usage : 10000
     * collaborators : 8
     * plan : {"name":"Medium","space":400,"private_repos":20,"collaborators":0}
     */

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
    @SerializedName("html_url")
    public String htmlUrl;
    @SerializedName("followers_url")
    public String followersUrl;
    @SerializedName("following_url")
    public String followingUrl;
    @SerializedName("gists_url")
    public String gistsUrl;
    @SerializedName("starred_url")
    public String starredUrl;
    @SerializedName("subscriptions_url")
    public String subscriptionsUrl;
    @SerializedName("organizations_url")
    public String organizationsUrl;
    @SerializedName("repos_url")
    public String reposUrl;
    @SerializedName("events_url")
    public String eventsUrl;
    @SerializedName("received_events_url")
    public String receivedEventsUrl;
    @SerializedName("type")
    public String type;
    @SerializedName("site_admin")
    public boolean siteAdmin;
    @SerializedName("name")
    public String name;
    @SerializedName("company")
    public String company;
    @SerializedName("blog")
    public String blog;
    @SerializedName("location")
    public String location;
    @SerializedName("email")
    public String email;
    @SerializedName("hireable")
    public boolean hireable;
    @SerializedName("bio")
    public String bio;
    @SerializedName("public_repos")
    public int publicRepos;
    @SerializedName("public_gists")
    public int publicGists;
    @SerializedName("followers")
    public int followers;
    @SerializedName("following")
    public int following;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;
    @SerializedName("total_private_repos")
    public int totalPrivateRepos;
    @SerializedName("owned_private_repos")
    public int ownedPrivateRepos;
    @SerializedName("private_gists")
    public int privateGists;
    @SerializedName("disk_usage")
    public int diskUsage;
    @SerializedName("collaborators")
    public int collaborators;
    /**
     * name : Medium
     * space : 400
     * private_repos : 20
     * collaborators : 0
     */

    @SerializedName("plan")
    public PlanBean plan;

    public static class PlanBean {
        @SerializedName("name")
        public String name;
        @SerializedName("space")
        public int space;
        @SerializedName("private_repos")
        public int privateRepos;
        @SerializedName("collaborators")
        public int collaborators;
    }
}
