package com.mprusina.gitrepo.di

import android.content.Context
import androidx.room.Room
import com.mprusina.gitrepo.common.DatabaseRepository
import com.mprusina.gitrepo.common.api.GitHubService
import com.mprusina.gitrepo.common.db.GitHubDatabase
import com.mprusina.gitrepo.repos.ReposContract
import com.mprusina.gitrepo.repos.presenter.RepoListPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://api.github.com/"

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): GitHubDatabase {
        return Room.databaseBuilder(context.applicationContext, GitHubDatabase::class.java, "Github.db").build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): GitHubService {
        return retrofit.create(GitHubService::class.java)
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesRepoListPresenter(databaseRepository: DatabaseRepository, service: GitHubService) : ReposContract.Presenter {
        return RepoListPresenter(databaseRepository, service)
    }
}