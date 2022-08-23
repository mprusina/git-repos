package com.mprusina.gitrepo.common.api

import com.mprusina.gitrepo.common.model.Contributor
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("search/repositories?q=language:kotlin&order=desc&sort=stars")
    suspend fun getRepos(@Query("page") page: Int, @Query("per_page") perPage: Int): RepoResponseDTO

    @GET("repos/{owner}/{repo}")
    suspend fun getRepoDetails(@Path("owner") owner: String, @Path("repo") repo: String) : RepoDTO

    @GET("repos/{owner}/{repo}/contributors")
    suspend fun getRepoContributors(@Path("owner") owner: String, @Path("repo") repo: String) : List<Contributor>
}
