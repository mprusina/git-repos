package com.mprusina.gitrepo.repos

import androidx.paging.PagingData
import com.mprusina.gitrepo.common.model.Repo
import kotlinx.coroutines.flow.Flow

interface ReposContract {
    interface View {
        fun showRepos()
        fun showLoading(show: Boolean)
        fun showMessage(show: Boolean, message: String? = null)
        fun openRepoDetails(repo: Repo)
        fun handleFavoriteAction(repo: Repo)
    }
    interface Presenter {
        fun loadData() : Flow<PagingData<Repo>>
        fun searchRepos(query: String) : Flow<PagingData<Repo>>
        fun handleRepoFavoriteAction(repo: Repo)
    }
}