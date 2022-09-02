package com.mprusina.gitrepo.common.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.databinding.FragmentContributorsListRowBinding

class ContributorListViewHolder(
    private val onFavoriteClickListener: (Contributor) -> Unit,
    private val binding: FragmentContributorsListRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(contributor: Contributor) = with(binding) {
        Glide.with(binding.root.context)
            .load(contributor.profilePhotoUrl)
            .centerCrop()
            .into(binding.contributorProfileImage)
        binding.contributorName.text = contributor.username
        if (contributor.favorite) {
            favorite.setImageResource(R.drawable.favorite_yes)
        } else {
            favorite.setImageResource(R.drawable.favorite_no)
        }
        favorite.setOnClickListener {
            if (contributor.favorite) {
                favorite.setImageResource(R.drawable.favorite_no)
            } else {
                favorite.setImageResource(R.drawable.favorite_yes)
            }
            onFavoriteClickListener(contributor)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onFavoriteClickListener: (Contributor) -> Unit): ContributorListViewHolder {
            val contributorListAdapter = FragmentContributorsListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ContributorListViewHolder(onFavoriteClickListener, contributorListAdapter)
        }
    }
}