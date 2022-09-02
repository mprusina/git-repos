package com.mprusina.gitrepo.repos.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.common.model.Repo
import com.mprusina.gitrepo.databinding.FragmentRepoListBinding
import com.mprusina.gitrepo.repos.ReposContract
import com.mprusina.gitrepo.repos.ui.adapter.RepoLoadStateAdapter
import com.mprusina.gitrepo.repos.ui.adapter.RepoPagingDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RepoListFragment : Fragment(), ReposContract.View {

    private lateinit var binding: FragmentRepoListBinding
    private lateinit var repoAdapter: RepoPagingDataAdapter
    @Inject lateinit var reposPresenter: ReposContract.Presenter
    private var searchJob: Job? = null
    private var showReposJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRepoListBinding.inflate(inflater, container, false)
        repoAdapter = RepoPagingDataAdapter(::openRepoDetails, ::handleFavoriteAction)

        binding.retryButton.setOnClickListener { repoAdapter.retry() }

        with(binding.reposList) {
            layoutManager = LinearLayoutManager(context)
            adapter = repoAdapter.withLoadStateHeaderAndFooter(
                header = RepoLoadStateAdapter { repoAdapter.retry() },
                footer = RepoLoadStateAdapter { repoAdapter.retry() }
            )
        }

        lifecycleScope.launch {
            repoAdapter.loadStateFlow.collect { loadState ->
                showLoading(loadState.source.refresh is LoadState.Loading)
                showMessage(loadState.source.refresh is LoadState.Error && repoAdapter.itemCount == 0)
            }
        }

        showRepos()
        initSearch()

        return binding.root
    }

    override fun showRepos() {
        showReposJob?.cancel()
        showReposJob = lifecycleScope.launchWhenStarted {
            reposPresenter.loadData().collectLatest { response ->
                repoAdapter.submitData(response)
            }
        }
    }

    override fun initSearch() {
        binding.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                searchRepos()
                true
            } else {
                false
            }
        }
        binding.search.setOnKeyListener { _, key, event ->
            if (key == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                searchRepos()
                true
            } else {
                false
            }
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(text: Editable?) {
                if (text.isNullOrEmpty()) {
                    showRepos()
                }
            }
        })

        lifecycleScope.launch {
            repoAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.reposList.scrollToPosition(0) }
        }
    }

    override fun searchRepos() {
        // Hide keyboard
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)

        binding.search.text?.trim()?.let { query ->
            if (query.isNotEmpty()) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    reposPresenter.searchRepos(query.toString()).collectLatest { searchData ->
                        repoAdapter.submitData(searchData)
                    }
                }
            }
        }
    }

    override fun showLoading(show: Boolean) {
        binding.loadingIndicator.isVisible = show
    }

    override fun showMessage(show: Boolean, message: String?) {
        binding.infoMessage.text = message ?: getString(R.string.error_loading_data)
        binding.infoMessage.isVisible = show
        binding.retryButton.isVisible = show
    }

    override fun openRepoDetails(repo: Repo) {
        val username = repo.owner
        val repoName = repo.name
        if (!username.isNullOrEmpty() && !repoName.isNullOrEmpty()) {
            val action = RepoListFragmentDirections.actionRepoListFragmentToRepoDetailsFragment(username, repoName)
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.error_no_repo_details), Toast.LENGTH_SHORT).show()
        }
    }

    override fun handleFavoriteAction(repo: Repo) = reposPresenter.handleRepoFavoriteAction(repo)
}