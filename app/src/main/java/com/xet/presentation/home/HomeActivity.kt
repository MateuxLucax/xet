package com.xet.presentation.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xet.databinding.ActivityHomeBinding
import com.xet.presentation.ServiceLocator

class HomeActivity(
    private var homeViewModel: HomeViewModel = ServiceLocator.getHomeViewModel()
) : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        binding.homeUser.text = homeViewModel.loadUser()

        setContentView(binding.root)
    }
}