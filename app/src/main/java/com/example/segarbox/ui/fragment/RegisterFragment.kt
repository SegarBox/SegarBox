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
import com.example.segarbox.databinding.FragmentRegisterBinding
import com.example.segarbox.helper.getColorFromAttr
import com.example.segarbox.ui.activity.MainActivity
import com.google.android.material.R.attr.colorOnSecondary
import com.google.android.material.R.attr.colorPrimary


class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setTextColor()
        binding.btnRegister.setOnClickListener(this)
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

        binding.tiName.apply {
            setBoxStrokeColorStateList(colorStroke)
            defaultHintTextColor = colorStroke
            hintTextColor = colorStroke
        }

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

        binding.tiPhone.apply {
            setBoxStrokeColorStateList(colorStroke)
            defaultHintTextColor = colorStroke
            hintTextColor = colorStroke
        }

        binding.tiPassword.apply {
            setBoxStrokeColorStateList(colorStroke)
            defaultHintTextColor = colorStroke
            hintTextColor = colorStroke
        }

        binding.tiConPassword.apply {
            setBoxStrokeColorStateList(colorStroke)
            defaultHintTextColor = colorStroke
            hintTextColor = colorStroke
        }
    }

    private fun isNotEmail(string: String): Boolean {
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(string).matches()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                val name = binding.etName.text.toString()
                val email = binding.etEmail.text.toString()
                val phone = binding.etPhone.text.toString()
                val password = binding.etPassword.text.toString()
                val conPassword = binding.etConPassword.text.toString()

                if (name.isEmpty()) {
                    binding.etName.error = "Input name"
                }
                if (email.isEmpty()) {
                    binding.etEmail.error = "Input email"
                }
                if (isNotEmail(email)) {
                    binding.etEmail.error = "Input a valid email"
                }
                if (phone.isEmpty()){
                    binding.etPhone.error = "Input phone number"
                }
                if(password.isEmpty()){
                    binding.etPassword.error = "Input password"
                }
                if(conPassword.isEmpty()){
                    binding.etConPassword.error = "Confirm your password"
                }
                if(conPassword != password){
                    binding.etConPassword.error = "Password is not match"
                }

                if (binding.etName.error == null && binding.etEmail.error == null && binding.etPhone.error == null && binding.etPassword.error == null && binding.etConPassword.error == null){
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Yeay!")
                        setMessage("Sign up success, happy shopping!")
                        setPositiveButton("Next") { _, _ ->
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                        create()
                        show()
                    }
                } else {
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Error!")
                        setMessage("Please fill all the required field correctly!")
                        setPositiveButton("Back") { _, _ ->

                        }
                    }
                }

                when {
//                    name.isEmpty() -> {
//                        binding.tiName.error = "Input name"
////                        binding.etName.error = "Input name"
//                    }
//                    name.isNotEmpty() -> {
//                        binding.tiName.error = null
//                        binding.tiName.helperText = null
////                        binding.etName.error = null
//                    }
//                    email.isEmpty() -> {
//                        binding.etEmail.error = "Input email"
//                    }
//                    isNotEmail(email) -> {
//                        binding.etEmail.error = "Input a valid email"
//                    }
//                    phone.isEmpty() -> {
//                        binding.tiPhone.error = "Input phone number"
//                    }
//                    password.isEmpty() -> {
//                        binding.tiPassword.error = "Input password"
//                    }
//                    conPassword.isEmpty() -> {
//                        binding.tiPassword.error = "Confirm your password"
//                    }
//                    conPassword != password -> {
//                        binding.tiConPassword.error = "Password is not match"
//                    }
//                    else -> {
//                        AlertDialog.Builder(requireContext()).apply {
//                            setTitle("Yeay!")
//                            setMessage("Sign up success, happy shopping!")
//                            setPositiveButton("Next") { _, _ ->
//                                startActivity(Intent(requireContext(), MainActivity::class.java))
//                                requireActivity().finish()
//                            }
//                            create()
//                            show()
//                        }
//                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}