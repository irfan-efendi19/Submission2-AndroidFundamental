package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.FavoriteAdapter
import com.dicoding.githubuser.data.ResultUser
import com.dicoding.githubuser.data.entity.UserFavoriteEntity
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.hide
import com.dicoding.githubuser.setting.ViewModelFactory
import com.dicoding.githubuser.show
import com.dicoding.githubuser.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String

    private val viewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(DetailActivity.EXTRA_USER)
        val bundle = Bundle()
        bundle.putString(DetailActivity.EXTRA_USER, username)

        observer()
        setupFavoriteRV()
    }

    private fun observer() {
        viewModel.getUserFavorite().observe(this) {
            when (it) {
                is ResultUser.Loading -> {
                    binding.ProgressBarFavorite.show()
                }

                is ResultUser.Success -> {
                    binding.ProgressBarFavorite.hide()
                    setupRvData(it.data)
                }

                is ResultUser.Error -> {
                    binding.ProgressBarFavorite.show()
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }

                else -> {
                }
            }
        }
    }

    private fun setupRvData(responseItems: List<UserFavoriteEntity>) {
        username = intent.getStringExtra("username").toString()
        val adapter = FavoriteAdapter()
        adapter.differ.submitList(responseItems)
        binding.favoriteRV.adapter = adapter
        adapter.onClick = { user ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("username", user.login)
            startActivity(intent)
        }
    }

    private fun setupFavoriteRV() {
        binding.favoriteRV.apply {
            val rvLayoutManager = LinearLayoutManager(this@FavoriteActivity)
            layoutManager = rvLayoutManager
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}