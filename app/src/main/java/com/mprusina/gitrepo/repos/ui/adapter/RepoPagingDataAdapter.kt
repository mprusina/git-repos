package com.mprusina.gitrepo.repos.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.mprusina.gitrepo.common.model.Repo
import com.mprusina.gitrepo.common.viewholders.RepoListViewHolder
import com.mprusina.gitrepo.databinding.FragmentRepoListRowBinding
import com.mprusina.gitrepo.utils.REPO_DIFF_CALLBACK

class RepoPagingDataAdapter(
    private val onItemClickListener: (Repo) -> Unit,
    private val onFavoriteClickListener: (Repo) -> Unit
) : PagingDataAdapter<Repo, RepoListViewHolder>(REPO_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoListViewHolder {
        return RepoListViewHolder(onItemClickListener, onFavoriteClickListener, FragmentRepoListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RepoListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}