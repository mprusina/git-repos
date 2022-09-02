package com.mprusina.gitrepo.favorites.favcontributors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.databinding.FragmentFavoriteContributorsListBinding
import com.mprusina.gitrepo.favorites.favcontributors.adapter.FavoriteContributorsListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteContributorsFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteContributorsListBinding
    private lateinit var favContributorsListAdapter: FavoriteContributorsListAdapter
    private val viewModel: FavoriteContributorsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoriteContributorsListBinding.inflate(inflater, container, false)
        favContributorsListAdapter = FavoriteContributorsListAdapter { handleContributorFavoriteAction(it) }

        with(binding.favContributorsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = favContributorsListAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        viewModel.favContributors.observe(viewLifecycleOwner) { data ->
            showLoading(false)
            showMessage(data.isEmpty())
            favContributorsListAdapter.submitList(data)
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

    private fun handleContributorFavoriteAction(contributor: Contributor) = viewModel.removeContributorFromFavorites(contributor)
}