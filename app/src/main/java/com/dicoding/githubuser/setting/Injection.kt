package com.dicoding.githubuser.setting

import android.content.Context
import com.dicoding.githubuser.data.entity.GithubRepository
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.data.room.FavDatabase
import com.dicoding.githubuser.util.UserExecutor

object Injection {
    fun provideRepository(context: Context): GithubRepository {
        val apiConfig = ApiConfig.getApiService()
        val database = FavDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = UserExecutor()

        return GithubRepository.getInstance(apiConfig, dao, appExecutors)
    }
}