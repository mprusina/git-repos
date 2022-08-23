package com.mprusina.gitrepo.common.api

import com.google.gson.annotations.SerializedName

data class RepoOwnerDTO(
    @SerializedName("login") var username: String? = null,
    @SerializedName("avatar_url") var avatarUrl: String? = null
)
