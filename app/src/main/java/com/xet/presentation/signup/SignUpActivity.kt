package com.xet.presentation.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.xet.databinding.ActivitySignUpBinding
import com.xet.presentation.ServiceLocator
import com.xet.presentation.home.HomeActivity
import com.xet.presentation.login.LoginActivity
import com.xet.presentation.login.afterTextChanged

class SignUpActivity(
    private val viewModel: SignUpViewModel = ServiceLocator.getSignUpViewModel()
) : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val login = binding.signUpLogin
        val fullName = binding.signUpUserFullName
        val username = binding.signUpUsername
        val password = binding.signUpPassword
        val signUpBtn = binding.signUpBtn
        val loading = binding.signUpLoading

        viewModel.signUpFormState.observe(this@SignUpActivity, Observer {
            val loginState = it ?: return@Observer

            signUpBtn.isEnabled = loginState.isDataValid

            if (loginState.fullNameError != null) {
                fullName.error = getString(loginState.fullNameError)
            }
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        viewModel.signUpResult.observe(this@SignUpActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showSignUpFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                redirectSignUp()
            }
            setResult(Activity.RESULT_OK)
        })

        fullName.afterTextChanged {
            viewModel.loginDataChanged(
                fullName.text.toString(),
                username.text.toString(),
                password.text.toString()
            )
        }

        username.afterTextChanged {
            viewModel.loginDataChanged(
                fullName.text.toString(),
                username.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            viewModel.loginDataChanged(
                fullName.text.toString(),
                username.text.toString(),
                password.text.toString()
            )
        }

        signUpBtn.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel.signUp(fullName.text.toString(), username.text.toString(), password.text.toString())
        }

        login.setOnClickListener {
            redirectLogin()
        }
    }

    private fun redirectLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun redirectSignUp() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun showSignUpFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}