package com.mprusina.gitrepo.common.api

import com.google.gson.annotations.SerializedName

data class RepoResponseDTO(
    @SerializedName("items") var repoList: List<RepoDTO>
)
