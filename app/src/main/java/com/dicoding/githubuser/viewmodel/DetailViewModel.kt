package com.dicoding.githubuser.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.entity.GithubRepository
import com.dicoding.githubuser.data.entity.UserFavoriteEntity
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class DetailViewModel(private val githubRepository: GithubRepository) : ViewModel() {

    private val userDetailLiveData = MutableLiveData<ItemsItem>()
    val userDetail: LiveData<ItemsItem> = userDetailLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        const val TAG = "DetailViewModel"
    }

//    init {
//        detailUser("name")
//    }

    fun detailUser(username: String) {
        _isLoading.value = true
//        val client = ApiConfig.getApiService().getUserDetail(username)
        viewModelScope.launch(Dispatchers.IO) {
            ApiConfig.getApiService().getUserDetail(username)
                .enqueue(object : Callback<ItemsItem> {
                    override fun onResponse(
                        call: Call<ItemsItem>,
                        response: Response<ItemsItem>
                    ) {
                        _isLoading.value = false
                        try {
                            if (response.isSuccessful) {
                                userDetailLiveData.value = response.body()
                            } else {
                                throw HttpException(response)
                            }
                        } catch (e: HttpException) {
                            Log.e(ContentValues.TAG, "Error: ${e.response().toString()}")
                        }
                    }

                    override fun onFailure(call: Call<ItemsItem>, t: Throwable) {
                        _isLoading.value = false
                        Log.e(ContentValues.TAG, "Error:${t.message.toString()}")
                    }
                })
        }
    }

    fun addToFavorite(user: UserFavoriteEntity) = githubRepository.insertFavorite(user)

    fun deleteFromFavorite(user: UserFavoriteEntity) = githubRepository.deleteFavorite(user)

    fun getUserInfo(username: String) = githubRepository.getUserInfo(username)

}

