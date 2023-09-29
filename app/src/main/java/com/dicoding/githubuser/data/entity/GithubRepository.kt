package com.dicoding.githubuser.data.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.githubuser.data.ResultUser
import com.dicoding.githubuser.data.retrofit.ApiInterface
import com.dicoding.githubuser.data.room.GithubDAO
import com.dicoding.githubuser.util.UserExecutor

class GithubRepository private constructor(
    private val apiService: ApiInterface,
    private val githubDAO: GithubDAO,
    private val appExecutors: UserExecutor
) {
    private val result = MediatorLiveData<ResultUser<List<UserFavoriteEntity>>>()
    private val resultS = MediatorLiveData<ResultUser<String>>()

    fun getFavoriteUser(): MediatorLiveData<ResultUser<List<UserFavoriteEntity>>> {
        result.value = ResultUser.Loading
        val localData = githubDAO.getAllFavorite()
        result.addSource(localData) {
            result.value = ResultUser.Success(it)
        }
        return result
    }

    fun getUserInfo(username: String): LiveData<List<UserFavoriteEntity>> {
        return githubDAO.getUserInfo(username)
    }

    fun insertFavorite(user: UserFavoriteEntity): LiveData<ResultUser<String>> {
        resultS.value = ResultUser.Loading
        appExecutors.diskIO.execute {
            githubDAO.insert(user)
        }
        resultS.value = ResultUser.Success("${user.login} telah ditambahkan ke Favorite")
        return resultS
    }

    fun deleteFavorite(user: UserFavoriteEntity): MediatorLiveData<ResultUser<String>> {
        resultS.value = ResultUser.Loading
        appExecutors.diskIO.execute {
            githubDAO.delete(user)
        }
        resultS.value = ResultUser.Success("${user.login} telah dihapus dari Favorite")
        return resultS
    }

    companion object {
        @Volatile
        private var instance: GithubRepository? = null
        fun getInstance(
            apiService: ApiInterface,
            githubDAO: GithubDAO,
            appExecutors: UserExecutor
        ): GithubRepository =
            instance ?: synchronized(this) {
                instance ?: GithubRepository(apiService, githubDAO, appExecutors)
            }.also { instance = it }
    }
}