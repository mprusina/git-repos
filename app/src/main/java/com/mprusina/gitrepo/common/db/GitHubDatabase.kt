package com.mprusina.gitrepo.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mprusina.gitrepo.common.db.dao.FavoriteContributorsDao
import com.mprusina.gitrepo.common.db.dao.FavoriteReposDao
import com.mprusina.gitrepo.common.model.Contributor
import com.mprusina.gitrepo.common.model.Repo

@Database(
    entities = [Repo::class, Contributor::class],
    version = 1,
    exportSchema = false
)
abstract class GitHubDatabase : RoomDatabase() {
    abstract fun contributorDao(): FavoriteContributorsDao
    abstract fun repoDao(): FavoriteReposDao
}