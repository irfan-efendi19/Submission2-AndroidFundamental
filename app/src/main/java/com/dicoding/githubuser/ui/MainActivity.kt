package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.SearchAdapter
import com.dicoding.githubuser.adapter.UserAdapter
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.setting.ViewModelFactory
import com.dicoding.githubuser.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var userAdapter: UserAdapter
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Splash Screen
        Thread.sleep(3000)
        installSplashScreen()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showUser()

        mainViewModel.getThemeSetting().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.btnDarkMode.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.btnFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }


        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val query = searchView.text.toString()
                    mainViewModel.searchUser(query)
                    searchView.hide()
                    false
                }
        }
    }

    private fun showUser() {
        mainViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.ProgressBarAwal.visibility = View.VISIBLE
            } else {
                binding.ProgressBarAwal.visibility = View.GONE
            }
        }

        mainViewModel.userlist.observe(this) {
            binding.userGitHubRV.layoutManager = LinearLayoutManager(this)
            userAdapter = UserAdapter(it)
            binding.userGitHubRV.adapter = userAdapter
        }

        mainViewModel.searchUser.observe(this) {
            binding.userGitHubRV.layoutManager = LinearLayoutManager(this)
            searchAdapter = SearchAdapter(it)
            binding.userGitHubRV.adapter = searchAdapter

            searchAdapter.onClick = { user ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("username", user.login)
                startActivity(intent)
            }
        }
    }
}



