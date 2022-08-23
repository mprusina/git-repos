package com.mprusina.gitrepo.common.api

import com.google.gson.annotations.SerializedName

data class RepoDTO(
    @SerializedName("id") val id: Long,
    @SerializedName("name") var name: String? = null,
    @SerializedName("owner") var owner: RepoOwnerDTO? = RepoOwnerDTO(),
    @SerializedName("description") var description: String? = null,
    @SerializedName("language") var language: String? = null,
    @SerializedName("stargazers_count") var stargazersCount: Int? = null,
    @SerializedName("forks_count") var forksCount: Int? = null,
    @SerializedName("open_issues_count") var openIssuesCount: Int? = null,
    @SerializedName("watchers_count") var watchersCount: Int? = null,
    @SerializedName("default_branch") var defaultBranch: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("html_url") var htmlUrl: String? = null
)