package com.mprusina.gitrepo.details.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.databinding.FragmentRepoDetailsListRowBinding

class ContributorsListAdapter(private val onFavoriteClickListener: (Contributor) -> Unit) : ListAdapter<Contributor, ContributorsListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentRepoDetailsListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: FragmentRepoDetailsListRowBinding) : RecyclerView.ViewHolder(binding.root) {
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
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contributor>() {
            override fun areItemsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
                return oldItem == newItem
            }
        }
    }
}