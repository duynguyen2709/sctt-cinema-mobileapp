package com.example.cgv.ui.login

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cgv.BaseActivity
import com.example.cgv.CoreApplication
import com.example.cgv.R
import com.example.cgv.databinding.ActivityLogInBinding
import com.example.cgv.ext.expandOrCollapse
import com.example.cgv.model.LoginParam
import com.example.cgv.model.Resource
import com.example.cgv.viewmodel.AuthenticationViewModel
import com.google.android.material.textfield.TextInputEditText

class LogInActivity : BaseActivity<ActivityLogInBinding>() {
    override val layoutId: Int = R.layout.activity_log_in
    private lateinit var viewModel: AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AuthenticationViewModel::class.java)
        bindViewModel()
    }

    override fun bindView() {
        super.bindView()
        viewBinding.activity = this

    }

    fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                if (checkValidField()) {
                    viewModel.postLogIn(
                        LoginParam(
                            email = viewBinding.edtUserName.text.toString(),
                            password = viewBinding.edtPassword.text.toString()
                        )
                    )
                }

            }
            R.id.ivBack -> {
                finish()
            }
        }
    }

    override fun bindViewModel() {
        viewModel.userInfoLiveData.observe(this, Observer {
            when (it?.status) {
                Resource.SUCCESS -> {
                    it.data?.let { response ->
                        CoreApplication.instance.saveUser(response.user)
                        CoreApplication.instance.saveToken(response.token)
                        Toast.makeText(this, response.token, Toast.LENGTH_SHORT).show()
                        navigateToHomeWithClearBStack()
                    }
                }
                Resource.LOADING -> {

                }
                Resource.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun checkValidField(): Boolean {
        return when {
            !checkEmpty(viewBinding.edtUserName) -> {
                this.showErrorToast(
                    getString(
                        R.string.error_empty,
                        getString(R.string.email_address)
                    )
                )
                false
            }
            !checkEmpty(viewBinding.edtPassword) -> {
                this.showErrorToast(
                    getString(
                        R.string.error_empty,
                        getString(R.string.password)
                    )
                )
                false
            }
            !checkEmpty(viewBinding.edtPassword) -> {
                this.showErrorToast(
                    getString(
                        R.string.error_empty,
                        getString(R.string.password)
                    )
                )
                false
            }
            !checkValidEmail(viewBinding.edtUserName.text.toString()) -> {
                this.showErrorToast(getString(R.string.invalid_email))
                false
            }
            else -> true
        }
    }

    private fun checkEmpty(view: TextInputEditText): Boolean {
        if (view.text.toString().isBlank()) {
            return false
        }
        return true
    }

    private fun checkValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

private fun LogInActivity.showErrorToast(text: String) {
    viewBinding.layoutToast.apply {
        tvToast.text = text
        root.expandOrCollapse()
    }
    Handler().postDelayed({
        viewBinding.layoutToast.root.expandOrCollapse()
    }, 2000)
}
