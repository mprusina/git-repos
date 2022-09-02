package com.mprusina.gitrepo.common.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.common.model.Repo
import com.mprusina.gitrepo.databinding.FragmentRepoListRowBinding

class RepoListViewHolder(
    private val onItemClickListener: ((Repo) -> Unit)?,
    private val onFavoriteClickListener: (Repo) -> Unit,
    private val binding: FragmentRepoListRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: Repo) = with(binding) {
        Glide.with(binding.root.context).load(repo.ownerAvatar).centerCrop().into(profileImage)
        ownerRepoName.text =
            itemView.context.resources.getString(R.string.owner_repo_name, repo.owner, repo.name)
        description.text = repo.description
        language.text = itemView.context.resources.getString(R.string.language, repo.language)
        startgazerCount.text = repo.stargazersCount.toString()
        forkCount.text = repo.forksCount.toString()
        openIssuesCount.text = repo.openIssuesCount.toString()
        watcherCount.text = repo.watchersCount.toString()
        if (repo.favorite) {
            favorite.setImageResource(R.drawable.favorite_yes)
        } else {
            favorite.setImageResource(R.drawable.favorite_no)
        }
        favorite.setOnClickListener {
            if (repo.favorite) {
                favorite.setImageResource(R.drawable.favorite_no)
            } else {
                favorite.setImageResource(R.drawable.favorite_yes)
            }
            onFavoriteClickListener(repo)
        }

        onItemClickListener?.let {
            root.setOnClickListener { it(repo) }
        }
    }


    companion object {
        fun create(parent: ViewGroup, onItemClickListener: ((Repo) -> Unit)?, onFavoriteClickListener: (Repo) -> Unit): RepoListViewHolder {
            val repoListAdapter = FragmentRepoListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return RepoListViewHolder(onItemClickListener, onFavoriteClickListener, repoListAdapter)
        }
    }
}