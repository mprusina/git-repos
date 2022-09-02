package com.mprusina.gitrepo.favorites.favrepos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mprusina.gitrepo.common.model.Repo
import com.mprusina.gitrepo.common.viewholders.RepoListViewHolder
import com.mprusina.gitrepo.databinding.FragmentRepoListRowBinding
import com.mprusina.gitrepo.utils.REPO_DIFF_CALLBACK

class FavoriteReposListAdapter(
    private val onFavoriteClickListener: (Repo) -> Unit
) : ListAdapter<Repo, RepoListViewHolder>(REPO_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoListViewHolder {
        return RepoListViewHolder(null, onFavoriteClickListener, FragmentRepoListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RepoListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}