package com.mprusina.gitrepo.utils

import androidx.recyclerview.widget.DiffUtil
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.common.model.Repo

val REPO_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem == newItem
    }
}

val CONTRIBUTOR_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contributor>() {
    override fun areItemsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
        return oldItem == newItem
    }
}