package com.dicoding.githubuser.data.retrofit

import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("users")
    @Headers("Authorization: token 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")
    fun getUser(): Call<List<ItemsItem>>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUser(
        @Query("q") q: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<ItemsItem>


    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>


    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

}
