package com.mprusina.gitrepo.repos.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.databinding.RepoLoadStateFooterItemBinding

class RepoLoadStateViewHolder(
    private val binding: RepoLoadStateFooterItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMessage.text = loadState.error.localizedMessage
        }
        binding.loadingIndicator.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMessage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): RepoLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_load_state_footer_item, parent, false)
            val binding = RepoLoadStateFooterItemBinding.bind(view)
            return RepoLoadStateViewHolder(binding, retry)
        }
    }
}