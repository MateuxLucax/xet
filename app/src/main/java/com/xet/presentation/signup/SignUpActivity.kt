package com.xet.presentation.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xet.databinding.ActivitySignUpBinding
import com.xet.presentation.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signUp = binding.signUpLogin

        signUp.setOnClickListener {
            redirectLogin()
        }
    }


    private fun redirectLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}