package com.mprusina.gitrepo.common.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mprusina.gitrepo.common.model.Contributor

@Dao
interface FavoriteContributorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContributor(contributor: Contributor)

    @Query("SELECT * FROM fav_contributors ORDER BY id ASC")
    fun getFavContributors(): LiveData<List<Contributor>>

    // Using this to map favorite data fetched from API. Looks cleaner than another observer in Fragment, and it's only a 1-time call to mark favorite contributors
    @Query("SELECT * FROM fav_contributors ORDER BY id ASC")
    suspend fun getContributorList(): List<Contributor>

    @Delete
    suspend fun deleteContributor(contributor: Contributor)
}