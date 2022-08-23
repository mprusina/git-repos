package com.mprusina.gitrepo.details.data

import com.mprusina.gitrepo.common.api.GitHubService
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.details.data.model.RepoDetails
import com.mprusina.gitrepo.utils.formatDate
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val gitHubService: GitHubService) {

    suspend fun getRepoDetails(owner: String, repo: String): RepoDetails {
        val repoDetails = with(gitHubService.getRepoDetails(owner, repo)) {
            RepoDetails(
                id = id,
                name = name,
                owner = this.owner?.username,
                ownerAvatar = this.owner?.avatarUrl,
                description = description,
                language = language,
                stargazersCount = stargazersCount,
                forksCount = forksCount,
                openIssuesCount = openIssuesCount,
                watchersCount = watchersCount,
                defaultBranch = defaultBranch,
                createdAt = formatDate(createdAt),
                updatedAt = formatDate(updatedAt),
                htmlUrl = htmlUrl
            )
        }
        return repoDetails
    }

    suspend fun getContributors(owner: String, repo: String): List<Contributor> {
        return gitHubService.getRepoContributors(owner, repo)
    }
}