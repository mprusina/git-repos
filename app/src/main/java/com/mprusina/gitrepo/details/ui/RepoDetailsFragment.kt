package com.mprusina.gitrepo.details.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.databinding.FragmentRepoDetailsBinding
import com.mprusina.gitrepo.details.ContributorsViewState
import com.mprusina.gitrepo.details.RepoDetailsViewModel
import com.mprusina.gitrepo.details.RepoDetailsViewState
import com.mprusina.gitrepo.details.data.model.RepoDetails
import com.mprusina.gitrepo.details.ui.adapter.ContributorsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepoDetailsFragment : Fragment() {

    private val args: RepoDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentRepoDetailsBinding
    private val viewModel: RepoDetailsViewModel by viewModels()
    private lateinit var contributorsAdapter: ContributorsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRepoDetailsBinding.inflate(inflater, container, false)
        contributorsAdapter = ContributorsListAdapter { handleContributorFavoriteAction(it) }

        with(binding.contributorsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = contributorsAdapter
        }

        viewModel.fetchRepoDetails(args.ownerName, args.repoName)
        viewModel.fetchRepoContributors(args.ownerName, args.repoName)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repoDetailsViewState.collectLatest { repoState ->
                    when(repoState) {
                        is RepoDetailsViewState.Loading -> {
                            showDetailsLoading(true)
                        }
                        is RepoDetailsViewState.Error -> {
                            showDetailsLoading(false)
                            showMessage(true, repoState.error)
                            showDetailsUi(false)
                        }
                        is RepoDetailsViewState.Success -> {
                            showDetailsLoading(false)
                            showDetailsUi(true, repoState.repoDetails)

                            viewModel.contributorsViewState.collectLatest { contributorState ->
                                when(contributorState) {
                                    is ContributorsViewState.Loading -> {
                                        showContributorsLoading(true)
                                    }
                                    is ContributorsViewState.Error -> {
                                        showContributorsLoading(false)
                                        showMessage(message = contributorState.error)
                                    }
                                    is ContributorsViewState.Success -> {
                                        showContributorsLoading(false)
                                        contributorsAdapter.submitList(contributorState.contributors)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDetailsUi(show: Boolean, repo: RepoDetails? = null) {
        if (repo != null) {
            with(binding) {
                openRepoUrl.setOnClickListener {
                    val url = repo.htmlUrl
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }
                Glide.with(binding.root.context)
                    .load(repo.ownerAvatar)
                    .centerCrop()
                    .into(profileImage)
                ownerRepoName.text = resources.getString(R.string.owner_repo_name, repo.owner, repo.name)
                defaultBranch.text = resources.getString(R.string.default_branch, repo.defaultBranch)
                language.text = resources.getString(R.string.language, repo.language)
                createdAt.text = resources.getString(R.string.created_at, repo.createdAt)
                updatedAt.text = resources.getString(R.string.updated_at, repo.updatedAt)
                description.text = repo.description
                startgazerCount.text = repo.stargazersCount.toString()
                forkCount.text = repo.forksCount.toString()
                openIssuesCount.text = repo.openIssuesCount.toString()
                watcherCount.text = repo.watchersCount.toString()
                if (repo.favorite == true) {
                    binding.favorite.setImageResource(R.drawable.favorite_yes)
                }
                binding.favorite.setOnClickListener { handleRepoFavoriteAction(repo) }
            }
        }
        binding.detailsContainer.isVisible = show
    }

    private fun showMessage(detailsError: Boolean = false, message: String? = null) {
        binding.listDivider.isVisible = !detailsError
        if (!message.isNullOrEmpty()) {
            binding.infoMessage.text = message
        }
        binding.infoMessage.isVisible = true
    }

    private fun showDetailsLoading(show: Boolean) {
        binding.detailsLoadingIndicator.isVisible = show
    }

    private fun showContributorsLoading(show: Boolean) {
        binding.contributorListLoadingIndicator.isVisible = show
    }

    private fun handleContributorFavoriteAction(contributor: Contributor) = viewModel.handleContributorFavorites(contributor)

    private fun handleRepoFavoriteAction(repoDetails: RepoDetails) {
        when (repoDetails.favorite) {
            true -> binding.favorite.setImageResource(R.drawable.favorite_no)
            else -> binding.favorite.setImageResource(R.drawable.favorite_yes)
        }
        viewModel.handleRepoFavorites(repoDetails)
    }
}