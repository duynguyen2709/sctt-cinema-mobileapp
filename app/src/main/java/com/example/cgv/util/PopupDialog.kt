package com.example.cgv.util

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.cgv.R

class PopupDialog : DialogFragment() {

    private lateinit var popup: PopUp
    private var binding: ViewDataBinding? = null

    companion object {
        fun newInstance(popup: PopUp?): PopupDialog =
            PopupDialog().apply {
                arguments = bundleOf("popup_model" to popup)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        popup = arguments?.getParcelable("popup_model") ?: PopUp()
        if (popup.popupId == 0)
            return layoutInflater.inflate(R.layout.layout_error_toast, container, false)

        binding = DataBindingUtil.inflate(inflater, popup.popupId, container, false)
        return if (binding?.root != null)
            binding?.root
        else
            layoutInflater.inflate(popup.popupId, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popup.messageQueue?.invoke(binding, dialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCancelable(popup.isCancelable)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (popup.isMatchParent)
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        else
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }
}