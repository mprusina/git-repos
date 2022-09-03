package com.mprusina.gitrepo.repos.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.retryButton.setOnClickListener { repoAdapter.retry() }

        with(binding.repoList) {
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

        lifecycleScope.launch {
            repoAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.repoList.scrollToPosition(0) }
        }
    }

    override fun showRepos() {
        showReposJob?.cancel()
        showReposJob = lifecycleScope.launchWhenStarted {
            reposPresenter.loadData().collectLatest { response ->
                repoAdapter.submitData(response)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Hide keyboard
                val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
                // Execute search if query not empty
                if (query.isNotEmpty()) {
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch {
                        reposPresenter.searchRepos(query).collectLatest { searchData ->
                            repoAdapter.submitData(searchData)
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(s: String): Boolean {
                return true
            }
        })
        val closeSearchButton: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        closeSearchButton.setOnClickListener {
            // On "X" click, close search and display original list
            if (!searchView.isIconified) {
                searchView.isIconified = true
            }
            myActionMenuItem.collapseActionView()
            showRepos()
        }

        myActionMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                // On Up (left-facing arrow) click, close search and display original list
                showRepos()
                return true
            }
        })
    }
}



