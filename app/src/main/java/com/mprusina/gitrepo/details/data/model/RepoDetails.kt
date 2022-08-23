package com.mprusina.gitrepo.details.data.model

data class RepoDetails(
    val id: Long,
    val name: String? = null,
    val owner: String? = null,
    val ownerAvatar: String? = null,
    val description: String? = null,
    val language: String? = null,
    val stargazersCount: Int? = null,
    val forksCount: Int? = null,
    val openIssuesCount: Int? = null,
    val watchersCount: Int? = null,
    val defaultBranch: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val htmlUrl: String? = null,
    var favorite: Boolean? = false
)
