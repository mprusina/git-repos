package com.mprusina.gitrepo.favorites.favrepos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.common.model.Repo
import com.mprusina.gitrepo.databinding.FragmentFavoriteReposListBinding
import com.mprusina.gitrepo.favorites.favrepos.adapter.FavoriteReposListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteReposFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteReposListBinding
    private lateinit var favReposListAdapter: FavoriteReposListAdapter
    private val viewModel: FavoriteReposViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteReposListBinding.inflate(inflater, container, false)
        favReposListAdapter = FavoriteReposListAdapter { handleRepoFavoriteAction(it) }

        with(binding.favReposList) {
            layoutManager = LinearLayoutManager(context)
            adapter = favReposListAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        viewModel.favRepos.observe(viewLifecycleOwner) { data ->
            showLoading(false)
            showMessage(data.isEmpty())
            favReposListAdapter.submitList(data)
        }
    }

    private fun showLoading(show: Boolean) {
        binding.loadingIndicator.isVisible = show
    }

    private fun showMessage(show: Boolean) {
        if (show) {
            binding.infoMessage.text = getString(R.string.no_favorites_message)
        }
        binding.infoMessage.isVisible = show
    }

    private fun handleRepoFavoriteAction(repo: Repo) {
        viewModel.removeRepoFromFavorites(repo)
    }
}