package com.mprusina.gitrepo.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mprusina.gitrepo.common.DatabaseRepository
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.common.model.Repo
import com.mprusina.gitrepo.details.data.GitHubRepository
import com.mprusina.gitrepo.details.data.model.RepoDetails
import com.mprusina.gitrepo.details.data.model.RepoDetailsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(private val gitHubRepository: GitHubRepository, private val dbRepository: DatabaseRepository, private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _repoDetailsViewState = MutableStateFlow<RepoDetailsViewState>(RepoDetailsViewState.Loading)
    val repoDetailsViewState: StateFlow<RepoDetailsViewState> = _repoDetailsViewState

    private val owner = savedStateHandle.get<String>("ownerName")
    private val repo = savedStateHandle.get<String>("repoName")

    init {
        fetchDetailsData()
    }

    private fun fetchDetailsData() {
        if (!owner.isNullOrEmpty() && !repo.isNullOrEmpty()) {
            viewModelScope.launch {
                gitHubRepository.getRepoDetails(owner, repo)
                    .zip(gitHubRepository.getContributors(owner, repo)) { details, contributors ->
                        val favReposList = dbRepository.getRepoList()
                        val favRepo = favReposList.find {
                            it.id == details.id
                        }
                        details.favorite = favRepo?.favorite ?: false

                        val favContributorsList = dbRepository.getContributorList()
                        favContributorsList.forEach{ dbContributor ->
                            contributors.forEach {
                                if (it.id == dbContributor.id) {
                                    it.favorite = true
                                }
                            }
                        }

                        return@zip RepoDetailsResult(details, contributors)
                    }
                    .catch { e ->
                        _repoDetailsViewState.value = RepoDetailsViewState.Error(e.toString())
                    }
                    .collect {
                        _repoDetailsViewState.value = RepoDetailsViewState.Success(it)
                    }
            }
        }
    }

    fun handleContributorFavorites(contributor: Contributor) {
        contributor.favorite = contributor.favorite != true
        viewModelScope.launch {
            if (contributor.favorite) {
                dbRepository.saveContributorToFavorites(contributor)
            } else {
                dbRepository.deleteContributorFromFavorites(contributor)
            }
        }
    }

    fun handleRepoFavorites(repoDetails: RepoDetails) {
        repoDetails.favorite = repoDetails.favorite != true
        val repo = with(repoDetails){Repo(
            id, name, owner, ownerAvatar, description, language, stargazersCount, forksCount, openIssuesCount, watchersCount, favorite
        )}
        viewModelScope.launch {
            if (repoDetails.favorite) {
                dbRepository.saveRepoToFavorites(repo)
            } else {
                dbRepository.deleteRepoFromFavorites(repo)
            }
        }
    }
}

sealed class RepoDetailsViewState {
    object Loading: RepoDetailsViewState()
    data class Error(val error: String?): RepoDetailsViewState()
    data class Success(val repoData: RepoDetailsResult): RepoDetailsViewState()
}