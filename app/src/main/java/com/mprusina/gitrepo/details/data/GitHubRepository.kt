package com.mprusina.gitrepo.details.data

import com.mprusina.gitrepo.common.api.GitHubService
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.details.data.model.RepoDetails
import com.mprusina.gitrepo.utils.formatDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GitHubRepository @Inject constructor(private val gitHubService: GitHubService) {

    suspend fun getRepoDetails(owner: String, repo: String): Flow<RepoDetails> {
        val repoDetails: Flow<RepoDetails> = flow {
            val repoDetailsDTO = gitHubService.getRepoDetails(owner, repo)
            val details = with(repoDetailsDTO) {
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
            emit(details)
        }
        return repoDetails
    }

    suspend fun getContributors(owner: String, repo: String): Flow<List<Contributor>> {
        return flow {emit(gitHubService.getRepoContributors(owner, repo))}
    }
}