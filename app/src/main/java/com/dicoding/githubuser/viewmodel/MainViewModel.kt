package com.dicoding.githubuser.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.data.response.SearchItem
import com.dicoding.githubuser.data.response.SearchResponse
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.setting.SettingPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _userlist = MutableLiveData<List<ItemsItem>>()
    val userlist: LiveData<List<ItemsItem>> = _userlist

    private val _searchUser = MutableLiveData<List<SearchItem>>()
    val searchUser: LiveData<List<SearchItem>> = _searchUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findUser()
    }

    private fun findUser() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            ApiConfig.getApiService().getUser()
                .enqueue(object : Callback<List<ItemsItem>> {
                    override fun onResponse(
                        call: Call<List<ItemsItem>>,
                        response: Response<List<ItemsItem>>
                    ) {
                        _isLoading.value = false
                        try {
                            if (response.isSuccessful) {
                                _userlist.value = response.body()
                            } else {
                                throw HttpException(response)
                            }
                        } catch (e: HttpException) {
                            Log.e(ContentValues.TAG, "Error: ${e.response().toString()}")
                        }
                    }

                    override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                        _isLoading.value = false
                        Log.e(ContentValues.TAG, "Error:${t.message.toString()}")
                    }

                })
        }
    }

    fun searchUser(username: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            ApiConfig.getApiService().getUser(username)
                .enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        _isLoading.value = false
                        try {
                            if (response.isSuccessful) {
                                _searchUser.value = response.body()!!.items!!
                            } else {
                                throw HttpException(response)
                            }
                        } catch (e: HttpException) {
                            Log.e(ContentValues.TAG, "Error: ${e.response().toString()}")
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        _isLoading.value = false
                        Log.e(ContentValues.TAG, "Error:${t.message.toString()}")
                    }

                })
        }
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }


}