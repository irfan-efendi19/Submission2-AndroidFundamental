package com.dicoding.githubuser.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.entity.GithubRepository

class FavoriteViewModel(private val userRepository: GithubRepository): ViewModel() {

    fun getUserFavorite() = userRepository.getFavoriteUser()
}