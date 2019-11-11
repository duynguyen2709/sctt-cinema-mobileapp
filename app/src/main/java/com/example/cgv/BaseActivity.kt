package com.example.cgv

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.cgv.util.PopUp
import com.example.cgv.util.PopupDialog
import java.util.ArrayList
import android.content.Intent
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.cgv.ui.home.MainActivity


abstract class BaseActivity<ViewBinding : ViewDataBinding> : AppCompatActivity(){
    lateinit var viewBinding: ViewBinding

    @get:LayoutRes
    abstract val layoutId: Int

    private var listOfPopupDialogFragment: ArrayList<DialogFragment?>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, layoutId)
        viewBinding.lifecycleOwner = this
        bindView()
    }

    override fun onStart() {
        super.onStart()
        bindViewModel()
    }

    open fun bindView() {}

    open fun bindViewModel() {}

    fun showPopup(popup: PopUp?) {
        closePopup()
        val popupDialogFragment = PopupDialog.newInstance(popup)
        popupDialogFragment.show(supportFragmentManager, PopupDialog().tag)
        listOfPopupDialogFragment?.add(popupDialogFragment)
    }

    fun closePopup() {
        for (item in listOfPopupDialogFragment ?: arrayListOf()) {
            item?.dismissAllowingStateLoss()
        }
        listOfPopupDialogFragment?.clear()
    }

    fun navigateToHomeWithClearBStack(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }


}