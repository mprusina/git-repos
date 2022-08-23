package com.mprusina.gitrepo.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mprusina.gitrepo.common.DatabaseRepository
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.common.model.Repo
import com.mprusina.gitrepo.details.data.GitHubRepository
import com.mprusina.gitrepo.details.data.model.RepoDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(private val gitHubRepository: GitHubRepository, private val dbRepository: DatabaseRepository) : ViewModel() {

    private val _repoDetailsViewState = MutableStateFlow<RepoDetailsViewState>(RepoDetailsViewState.Loading)
    val repoDetailsViewState: StateFlow<RepoDetailsViewState> = _repoDetailsViewState

    private val _contributorsViewState = MutableStateFlow<ContributorsViewState>(ContributorsViewState.Loading)
    val contributorsViewState: StateFlow<ContributorsViewState> = _contributorsViewState

    fun fetchRepoDetails(owner: String, repo: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                gitHubRepository.getRepoDetails(owner, repo)
            }.onFailure {
                _repoDetailsViewState.value = RepoDetailsViewState.Error(it.message)
            }.onSuccess { repoDetails ->
                val favReposList = dbRepository.getRepoList()
                val favRepo = favReposList.find {
                    it.id == repoDetails.id
                }
                repoDetails.favorite = favRepo?.favorite ?: false
                _repoDetailsViewState.value = RepoDetailsViewState.Success(repoDetails)
            }
        }
    }

    fun fetchRepoContributors(owner: String, repo: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                gitHubRepository.getContributors(owner, repo)
            }.onFailure {
                _contributorsViewState.value = ContributorsViewState.Error(it.message)
            }.onSuccess { contributors ->
                val favContributorsList = dbRepository.getContributorList()
                favContributorsList.forEach{ dbContributor ->
                    contributors.forEach {
                        if (it.id == dbContributor.id) {
                            it.favorite = true
                        }
                    }
                }
                _contributorsViewState.value = ContributorsViewState.Success(contributors)
            }
        }
    }

    fun addContributorToFavorites(contributor: Contributor) {
        contributor.favorite = true
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.saveContributorToFavorites(contributor)
        }
    }

    fun removeContributorFromFavorites(contributor: Contributor) {
        contributor.favorite = false
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.deleteContributorFromFavorites(contributor)
        }
    }

    fun addRepoToFavorites(repoDetails: RepoDetails) {
        repoDetails.favorite = true
        val repo = with(repoDetails){Repo(
            id, name, owner, ownerAvatar, description, language, stargazersCount, forksCount, openIssuesCount, watchersCount, favorite
        )}
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.saveRepoToFavorites(repo)
        }
    }

    fun removeRepoFromFavorites(repoDetails: RepoDetails) {
        repoDetails.favorite = false
        val repo = with(repoDetails){Repo(
            id, name, owner, ownerAvatar, description, language, stargazersCount, forksCount, openIssuesCount, watchersCount, favorite
        )}
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.deleteRepoFromFavorites(repo)
        }
    }
}

sealed class RepoDetailsViewState {
    object Loading: RepoDetailsViewState()
    data class Error(val error: String?): RepoDetailsViewState()
    data class Success(val repoDetails: RepoDetails): RepoDetailsViewState()
}

sealed class ContributorsViewState {
    object Loading: ContributorsViewState()
    data class Error(val error: String?): ContributorsViewState()
    data class Success(val contributors: List<Contributor>): ContributorsViewState()
}