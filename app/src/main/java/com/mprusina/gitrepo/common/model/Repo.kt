package com.mprusina.gitrepo.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_repos")
data class Repo(
    @PrimaryKey val id: Long,
    val name: String? = null,
    val owner: String? = null,
    val ownerAvatar: String? = null,
    val description: String? = null,
    val language: String? = null,
    val stargazersCount: Int? = null,
    val forksCount: Int? = null,
    val openIssuesCount: Int? = null,
    val watchersCount: Int? = null,
    var favorite: Boolean = false
)