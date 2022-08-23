package com.mprusina.gitrepo.repos.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mprusina.gitrepo.common.DatabaseRepository
import com.mprusina.gitrepo.common.api.GitHubService
import com.mprusina.gitrepo.common.model.Repo
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val REPO_START_PAGE = 1

class RepoPagingSource @Inject constructor(private val dbRepository: DatabaseRepository, private val gitHubService: GitHubService) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val page = params.key ?: REPO_START_PAGE
        return try {
            val response = gitHubService.getRepos(page, params.loadSize)
            val repos = response.repoList.map {
                with(it) {
                    Repo(
                        id = id,
                        name = name,
                        owner = owner?.username,
                        ownerAvatar = owner?.avatarUrl,
                        description = description,
                        language = language,
                        stargazersCount = stargazersCount,
                        forksCount = forksCount,
                        openIssuesCount = openIssuesCount,
                        watchersCount = watchersCount
                    )
                }
            }
            // Map favorites so that favorite icon is displayed correctly in repo list
            val favRepos = dbRepository.getRepoList()
            favRepos.forEach { favRepo ->
                repos.forEach {
                    if (it.id == favRepo.id) {
                        it.favorite = true
                    }
                }
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (page == REPO_START_PAGE) null else page - 1,
                nextKey = if (repos.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}