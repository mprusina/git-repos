package com.mprusina.gitrepo.favorites.favrepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mprusina.gitrepo.common.DatabaseRepository
import com.mprusina.gitrepo.common.model.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteReposViewModel @Inject constructor(private val dbRepository: DatabaseRepository) : ViewModel() {
    lateinit var favRepos : LiveData<List<Repo>>

    init {
        loadFavRepos()
    }

    private fun loadFavRepos() {
        viewModelScope.launch {
            favRepos = dbRepository.getFavoriteRepos()
        }
    }

    fun removeRepoFromFavorites(repo: Repo) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.deleteRepoFromFavorites(repo)
        }
    }
}