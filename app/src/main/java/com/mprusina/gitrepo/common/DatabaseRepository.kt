package com.mprusina.gitrepo.common

import androidx.lifecycle.LiveData
import com.mprusina.gitrepo.common.db.GitHubDatabase
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.common.model.Repo
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val database: GitHubDatabase) {
    suspend fun saveContributorToFavorites(contributor: Contributor) {
        database.contributorDao().insertContributor(contributor)
    }
    suspend fun deleteContributorFromFavorites(contributor: Contributor) {
        database.contributorDao().deleteContributor(contributor)
    }
    fun getFavoriteContributors(): LiveData<List<Contributor>> {
        return database.contributorDao().getFavContributors()
    }

    suspend fun saveRepoToFavorites(repo: Repo) {
        database.repoDao().insertRepo(repo)
    }
    suspend fun deleteRepoFromFavorites(repo: Repo) {
        database.repoDao().deleteRepo(repo)
    }
    fun getFavoriteRepos(): LiveData<List<Repo>> {
        return database.repoDao().getFavRepos()
    }

    suspend fun getRepoList(): List<Repo> {
        return database.repoDao().getRepoList()
    }

    suspend fun getContributorList(): List<Contributor> {
        return database.contributorDao().getContributorList()
    }
}