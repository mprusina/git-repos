package com.mprusina.gitrepo.repos.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.common.model.Repo
import com.mprusina.gitrepo.databinding.FragmentRepoListRowBinding

class RepoPagingDataAdapter(
    private val onItemClickListener: (Repo) -> Unit,
    private val onFavoriteClickListener: (Repo) -> Unit
) : PagingDataAdapter<Repo, RepoPagingDataAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentRepoListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(private var binding: FragmentRepoListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo) = with(binding) {
            Glide.with(binding.root.context).load(repo.ownerAvatar).centerCrop().into(profileImage)
            ownerRepoName.text = itemView.context.resources.getString(R.string.owner_repo_name, repo.owner, repo.name)
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
            root.setOnClickListener { onItemClickListener(repo) }
            favorite.setOnClickListener {
                if (repo.favorite) {
                    favorite.setImageResource(R.drawable.favorite_no)
                } else {
                    favorite.setImageResource(R.drawable.favorite_yes)
                }
                onFavoriteClickListener(repo)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }
}