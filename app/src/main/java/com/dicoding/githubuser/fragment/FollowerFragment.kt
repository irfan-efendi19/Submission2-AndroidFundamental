package com.dicoding.githubuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.UserAdapter
import com.dicoding.githubuser.databinding.FragmentFollowerBinding
import com.dicoding.githubuser.viewmodel.FollowerViewModel


class FollowerFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var username: String
    private lateinit var adapter: UserAdapter
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
            val layoutManager = LinearLayoutManager(requireContext())
            binding.FollowerRV.layoutManager = layoutManager

            if (isAdded && !isDetached) {
                val followViewModel = ViewModelProvider(
                    this, ViewModelProvider.NewInstanceFactory()
                )[FollowerViewModel::class.java]
                followViewModel.isLoading.observe(viewLifecycleOwner) {
                    showLoading(it)
                }
                followViewModel.follower.observe(viewLifecycleOwner) { follow ->
                    if (follow != null) {
                        adapter = UserAdapter(follow)
                        binding.FollowerRV.adapter = adapter
                    }
                }
                if (position == 1) {
                    followViewModel.getFollower(username, "following")
                } else {
                    followViewModel.getFollower(username, "follower")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.FollowerRV.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.ProgressBarFollow.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        var ARG_USERNAME: String = ""
        var ARG_POSITION = "section_number"
    }
}