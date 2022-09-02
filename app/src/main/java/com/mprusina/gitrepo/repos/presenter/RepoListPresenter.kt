package com.mprusina.gitrepo.repos.presenter

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import com.mprusina.gitrepo.common.DatabaseRepository
import com.mprusina.gitrepo.common.api.GitHubService
import com.mprusina.gitrepo.common.model.Repo
import com.mprusina.gitrepo.repos.ReposContract
import com.mprusina.gitrepo.repos.data.RepoPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val REPO_PAGE_SIZE = 50

class RepoListPresenter @Inject constructor(private val dbRepository: DatabaseRepository, private val service: GitHubService) : ReposContract.Presenter {

    private lateinit var repoPagingData : Flow<PagingData<Repo>>

    override fun loadData(): Flow<PagingData<Repo>> {
        repoPagingData = Pager(
            config = PagingConfig(
                pageSize = REPO_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { RepoPagingSource(dbRepository, service) }
        ).flow
        return repoPagingData
    }

    override fun searchRepos(query: String): Flow<PagingData<Repo>> {
        val repoSearch = repoPagingData
        return repoSearch.map {
            it.filter { repo ->
                repo.name?.contains(query) == true || repo.owner?.contains(query) == true || repo.description?.contains(query) == true
            }
        }
    }

    override fun handleRepoFavoriteAction(repo: Repo) {
        repo.favorite = repo.favorite != true
        runBlocking {
            launch {
                if (repo.favorite == true) {
                    dbRepository.saveRepoToFavorites(repo)
                } else {
                    dbRepository.deleteRepoFromFavorites(repo)
                }
            }
        }
    }
}