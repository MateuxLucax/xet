package com.xet.presentation.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xet.R
import com.xet.data.Result
import com.xet.databinding.ActivityHomeBinding
import com.xet.presentation.ServiceLocator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        val meContainer = binding.homeMe

        val useCase = ServiceLocator.getLoginUseCases().loggedInUser
        val result = useCase()
        if (result is Result.Success) {
            meContainer.text = result.data.displayName
        } else {
            meContainer.text = R.string.home_error.toString()
        }

        setContentView(binding.root)
    }
}