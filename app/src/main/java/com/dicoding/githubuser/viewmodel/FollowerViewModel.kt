package com.dicoding.githubuser.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel:ViewModel() {
    private val _follower = MutableLiveData<List<ItemsItem>>()
    val follower: LiveData<List<ItemsItem>> = _follower

    private  val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowerViewModel"
    }


    fun getFollower(username: String, tab:String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            val client: Call<List<ItemsItem>> = if (tab == "follower"){
                ApiConfig.getApiService().getUserFollowers(username)
            }else{
                ApiConfig.getApiService().getUserFollowing(username)
            }
            client.enqueue(object: Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _follower.value = response.body()
                    }else{
                        Log.e(ContentValues.TAG, "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(ContentValues.TAG,"Error: ${t.message.toString()}" )
                }

            })
        }
        }
}