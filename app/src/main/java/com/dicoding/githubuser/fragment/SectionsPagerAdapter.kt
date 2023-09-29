package com.dicoding.githubuser.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        val fragment = FollowerFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowerFragment.ARG_POSITION, position + 1)
            putString(FollowerFragment.ARG_USERNAME, username)
        }

//        var fragment: Fragment? = null
//         when (position) {
//            0 -> fragment = FollowerFragment()
//            1 -> fragment = FollowingFragment()
//        }
//        return fragment as Fragment
        return fragment
    }

}