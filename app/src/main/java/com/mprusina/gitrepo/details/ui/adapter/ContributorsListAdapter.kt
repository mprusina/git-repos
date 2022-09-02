package com.mprusina.gitrepo.details.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.common.viewholders.ContributorListViewHolder
import com.mprusina.gitrepo.databinding.FragmentContributorsListRowBinding
import com.mprusina.gitrepo.utils.CONTRIBUTOR_DIFF_CALLBACK

class ContributorsListAdapter(
    private val onFavoriteClickListener: (Contributor) -> Unit
) : ListAdapter<Contributor, ContributorListViewHolder>(CONTRIBUTOR_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributorListViewHolder {
        return ContributorListViewHolder(onFavoriteClickListener, FragmentContributorsListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ContributorListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}