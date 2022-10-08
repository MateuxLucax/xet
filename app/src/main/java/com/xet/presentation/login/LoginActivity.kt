package com.xet.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.xet.databinding.ActivityLoginBinding
import com.xet.presentation.ServiceLocator
import com.xet.presentation.home.HomeActivity
import com.xet.presentation.signup.SignUpActivity

class LoginActivity (
    private var viewModel: LoginViewModel = ServiceLocator.getLoginViewModel()
) : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.loginUsername
        val password = binding.loginPassword
        val loginBtn = binding.loginBtn
        val loading = binding.loginLoading
        val signUp = binding.loginCreateAccount

        viewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            loginBtn.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        viewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                redirectLoginSuccessful()
            }
            setResult(Activity.RESULT_OK)
        })

        username.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        loginBtn.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel.login(username.text.toString(), password.text.toString())
        }

        signUp.setOnClickListener {
            redirectLogin()
        }
    }

    private fun redirectLoginSuccessful() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun redirectLogin() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}