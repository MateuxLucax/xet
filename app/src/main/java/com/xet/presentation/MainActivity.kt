package com.xet.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.xet.R
import com.xet.databinding.ActivityMainBinding
import com.xet.domain.usecase.user.LoginUseCases
import com.xet.presentation.home.HomeActivity
import com.xet.presentation.login.LoginActivity

class MainActivity(
    private var viewModel: MainViewModel = ServiceLocator.getMainViewModel()
) : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewModel.checkLoggedInUser()) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        finish()
    }

    class MainViewModel(
        private val useCases: LoginUseCases
    ): ViewModel() {
        fun checkLoggedInUser(): Boolean {
            return useCases.isLoggedInUser()
        }
    }
}