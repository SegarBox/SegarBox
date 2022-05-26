package com.example.segarbox.ui.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.segarbox.R
import com.example.segarbox.databinding.FragmentLoginBinding
import com.example.segarbox.helper.getColorFromAttr
import com.example.segarbox.ui.activity.MainActivity
import com.google.android.material.R.attr.colorOnSecondary
import com.google.android.material.R.attr.colorPrimary

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setTextColor()
        binding.btnLogin.setOnClickListener(this)
    }

    private fun setTextColor() {
        val colorStroke = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_focused),
                intArrayOf(android.R.attr.state_focused),
            ),

            intArrayOf(
                requireActivity().getColorFromAttr(colorOnSecondary),
                requireActivity().getColorFromAttr(colorPrimary)
            )
        )

        binding.tiEmail.apply {
            setBoxStrokeColorStateList(colorStroke)
            defaultHintTextColor = colorStroke
            hintTextColor = colorStroke
        }

        binding.tiPassword.apply {
            setBoxStrokeColorStateList(colorStroke)
            defaultHintTextColor = colorStroke
            hintTextColor = colorStroke
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                if (email.isEmpty()){
                    binding.etEmail.error = "Input email"
                }
                if (password.isEmpty()){
                    binding.etPassword.error = "Input password"
                }
                if (email.isNotEmpty() && password.isNotEmpty()){
                    requireActivity().finish()
                }
            }
        }
    }

}