package com.mprusina.gitrepo.details.data.model

import com.mprusina.gitrepo.common.model.Contributor

data class RepoDetailsResult(
    val repo: RepoDetails,
    val contributors: List<Contributor>
)
