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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val REPO_PAGE_SIZE = 50

class RepoListPresenter @Inject constructor(private val dbRepository: DatabaseRepository, private val service: GitHubService) : ReposContract.Presenter {

    private lateinit var repoPagingData : Flow<PagingData<Repo>>

    init {
        loadData()
    }

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
                repo.name?.contains(query) == true
                        || repo.owner?.contains(query) == true
                        || repo.description?.contains(query) == true
            }
        }.flowOn(Dispatchers.Default)
    }

    override fun saveToFavorites(repo: Repo) {
        repo.favorite = true
        runBlocking {
            launch(Dispatchers.IO) {
                dbRepository.saveRepoToFavorites(repo)
            }
        }
    }

    override fun removeFromFavorites(repo: Repo) {
        repo.favorite = false
        runBlocking {
            launch(Dispatchers.IO) {
                dbRepository.deleteRepoFromFavorites(repo)
            }
        }
    }
}