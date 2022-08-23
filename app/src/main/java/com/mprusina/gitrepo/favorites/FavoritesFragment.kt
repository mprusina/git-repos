package com.mprusina.gitrepo.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mprusina.gitrepo.R
import com.mprusina.gitrepo.databinding.FragmentFavoritesBinding
import com.mprusina.gitrepo.favorites.favcontributors.FavoriteContributorsFragment
import com.mprusina.gitrepo.favorites.favrepos.FavoriteReposFragment

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        val favTabs = arrayOf(getString(R.string.tab_fav_repos), getString(R.string.tab_fav_contributors))

        val favoritesPagerAdapter = FavoritesPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = favoritesPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int -> tab.text = favTabs[position] }.attach()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) { viewPager.currentItem = tab.position }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return binding.root
    }


    inner class FavoritesPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FavoriteReposFragment()
                1 -> FavoriteContributorsFragment()
                else -> FavoriteReposFragment()
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }
}