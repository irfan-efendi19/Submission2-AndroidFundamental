package com.dicoding.githubuser.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.ResultUser
import com.dicoding.githubuser.data.entity.UserFavoriteEntity
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.fragment.SectionsPagerAdapter
import com.dicoding.githubuser.hide
import com.dicoding.githubuser.setting.ViewModelFactory
import com.dicoding.githubuser.show
import com.dicoding.githubuser.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityDetailBinding
    private val binding get() = _binding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var username: String
    private var isFavorite = false

    companion object {
        const val EXTRA_USER = "EXTRA_USER"

        private val TAB_TITLES = listOf(
            "Following",
            "Follower"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val userImage = intent.getStringExtra("userAvatar")
//        val userName = intent.getStringExtra("userName")
//        val userDesc = intent.getStringExtra("userDesc")
//        showUserDetail(userImage, userName, userDesc)

        val username = intent.getStringExtra(EXTRA_USER)
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        showDetail()
        setViewPager()

//        detailViewModel.isLoading.observe(this) { showLoading(it) }

//        val sectionsPagerAdapter = SectionsPagerAdapter(this)
//        val viewPager: ViewPager2 = binding.viewPager
//        viewPager.adapter = sectionsPagerAdapter
//        val tabs: TabLayout = binding.tabs
//        TabLayoutMediator(tabs, viewPager) { tab, position ->
//            tab.text = resources.getString(TAB_TITLES[position])
//        }.attach()
    }

    private fun showDetail() {
        username = intent.getStringExtra("username").toString()
        detailViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.ProgressBarDetail.visibility = View.VISIBLE
            } else {
                binding.ProgressBarDetail.visibility = View.GONE
            }
        }
        detailViewModel.detailUser(username)
        detailViewModel.userDetail.observe(this) { userDetail ->
            Glide.with(this)
                .load(userDetail.avatarUrl)
                .into(binding.DetailImageView)
            binding.apply {
                nameTV.text = userDetail.name
                DescTV.text = userDetail.login
                TVFollower.text = "${userDetail.followers} Followers"
                TVFollowing.text = "${userDetail.following} Following"
            }
            setFavorite(userDetail)
        }
    }

    private fun setViewPager() {
        val adapter = SectionsPagerAdapter(this, username)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            val text = TAB_TITLES[position]
            tab.text = text
        }.attach()
    }

    private fun setFavorite(user: ItemsItem) {
        val userEntity = UserFavoriteEntity(user.name, user.login, user.avatarUrl)
        detailViewModel.getUserInfo(user.login).observe(this) {
            isFavorite = it.isNotEmpty()

            binding.favoriteButton.imageTintList = if (it.isEmpty()) {
                ColorStateList.valueOf(Color.rgb(255, 255, 255))
            } else {
                ColorStateList.valueOf(Color.rgb(255, 77, 77))
            }
        }

        binding.favoriteButton.setOnClickListener {
            if (isFavorite) {
                detailViewModel.deleteFromFavorite(userEntity).observe(this) {
                    when (it) {
                        is ResultUser.Loading -> {
                            binding.favoriteButton.hide()
                            binding.ProgressBarDetail.show()
                        }

                        is ResultUser.Success -> {
                            binding.ProgressBarDetail.hide()
                            binding.favoriteButton.show()
                            Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()
                        }

                        is ResultUser.Error -> {
                            binding.ProgressBarDetail.hide()
                            binding.favoriteButton.show()
                            Toast.makeText(this, "Opps, ada yang salah nih.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                detailViewModel.addToFavorite(userEntity).observe(this) {
                    when (it) {
                        is ResultUser.Loading -> {
                            binding.favoriteButton.hide()
                            binding.ProgressBarDetail.show()
                        }

                        is ResultUser.Success -> {
                            binding.ProgressBarDetail.hide()
                            binding.favoriteButton.show()
                            Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()
                        }

                        is ResultUser.Error -> {
                            binding.ProgressBarDetail.hide()
                            binding.favoriteButton.show()
                            Toast.makeText(this, "Opps, ada yang salah nih.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

//    private fun showUserDetail(
//        userImage: String?,
//        userLogin: String?,
//        userDesc: String?
//    ) {
//        if (userImage != null && userLogin != null) {
//            Glide.with(this)
//                .load(userImage)
//                .into(binding.DetailImageView)
//            binding.nameTV.text = userLogin
//            binding.DescTV.text = userDesc
//            showLoading(false)
//        } else
//            showLoading(true)
//    }


}