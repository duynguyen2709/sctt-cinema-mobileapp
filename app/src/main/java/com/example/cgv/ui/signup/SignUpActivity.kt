package com.example.cgv.ui.signup

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cgv.BaseActivity
import com.example.cgv.CoreApplication
import com.example.cgv.R
import com.example.cgv.databinding.ActivitySignUpBinding
import com.example.cgv.ext.expandOrCollapse
import com.example.cgv.model.Movie
import com.example.cgv.model.Resource
import com.example.cgv.model.User
import com.example.cgv.ui.home.MainActivity
import com.example.cgv.viewmodel.AuthenticationViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {
    override val layoutId: Int = R.layout.activity_sign_up
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
            R.id.btnSignUp -> {
                if(checkValidField()) {
                    viewModel.postSignUp(
                        User(
                            email = viewBinding.edtUserName.text.toString(),
                            password = viewBinding.edtPassword.text.toString(),
                            fullName = viewBinding.edtFullName.text.toString(),
                            phoneNumber = viewBinding.edtPhoneNumber.text.toString()
                        )
                    )
                }
            }
            R.id.ivBack->{
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
                        finish()
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
            !checkEmpty(viewBinding.edtPhoneNumber) -> {
                this.showErrorToast(
                    getString(
                        R.string.error_empty,
                        getString(R.string.full_name)
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
            !checkSizePassword(viewBinding.edtPassword.text.toString()) -> {
                this.showErrorToast(
                    getString(
                        R.string.password_size,
                        getString(R.string.password)
                    )
                )
                false
            }
            !checkUpperCaseLetter(viewBinding.edtPassword.text.toString()) -> {
                this.showErrorToast(
                    getString(
                        R.string.password_upper_case,
                        getString(R.string.password)
                    )
                )
                false
            }
            !checkDigit(viewBinding.edtPassword.text.toString()) -> {
                this.showErrorToast(
                    getString(
                        R.string.password_digit,
                        getString(R.string.password)
                    )
                )
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

    private fun checkSizePassword(password: String?) = password?.length ?: 0 >= 8

    private fun checkUpperCaseLetter(password: String?) =
        Regex("^(?=.*[A-Z])").find(password ?: "") != null

    private fun checkDigit(password: String?) = Regex("^(?=.*\\d)").find(password ?: "") != null

    private fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    private fun checkValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

private fun SignUpActivity.showErrorToast(text: String) {
    viewBinding.layoutToast.apply {
        tvToast.text = text
        root.expandOrCollapse()
    }
    Handler().postDelayed({
        viewBinding.layoutToast.root.expandOrCollapse()
    }, 2000)
}
