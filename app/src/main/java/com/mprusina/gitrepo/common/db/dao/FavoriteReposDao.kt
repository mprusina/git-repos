package com.mprusina.gitrepo.common.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mprusina.gitrepo.common.model.Repo

@Dao
interface FavoriteReposDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(repo: Repo)

    @Query("SELECT * FROM fav_repos ORDER BY id ASC")
    fun getFavRepos(): LiveData<List<Repo>>

    // Using this to map favorite data fetched from API. Looks cleaner than another observer in Fragment, and it's only a 1-time call to mark favorite repos
    @Query("SELECT * FROM fav_repos ORDER BY id ASC")
    suspend fun getRepoList(): List<Repo>

    @Delete
    suspend fun deleteRepo(repo: Repo)
}