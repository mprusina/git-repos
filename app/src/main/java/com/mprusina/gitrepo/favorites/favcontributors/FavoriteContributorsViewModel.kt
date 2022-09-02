package com.mprusina.gitrepo.favorites.favcontributors

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mprusina.gitrepo.common.DatabaseRepository
import com.mprusina.gitrepo.common.model.Contributor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteContributorsViewModel @Inject constructor(private val dbRepository: DatabaseRepository) : ViewModel() {
    lateinit var favContributors : LiveData<List<Contributor>>

    init {
        loadFavContributors()
    }

    private fun loadFavContributors() {
        viewModelScope.launch {
            favContributors = dbRepository.getFavoriteContributors()
        }
    }

    fun removeContributorFromFavorites(contributor: Contributor) {
        viewModelScope.launch {
            dbRepository.deleteContributorFromFavorites(contributor)
        }
    }
}