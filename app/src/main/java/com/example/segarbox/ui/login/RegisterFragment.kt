package com.example.segarbox.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.data.Resource
import com.example.core.utils.getColorFromAttr
import com.example.segarbox.R
import com.example.segarbox.databinding.FragmentRegisterBinding
import com.google.android.material.R.attr.colorOnSecondary
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setTextColor()
        observeData()
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

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                binding.progressBar.isVisible = isLoading
                binding.btnRegister.isEnabled = !isLoading
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                viewModel.register(
                    name = binding.etName.text.toString(),
                    email = binding.etEmail.text.toString(),
                    phone = binding.etPhone.text.toString(),
                    password = binding.etPassword.text.toString(),
                    password_confirmation = binding.etConPassword.text.toString()
                ).observe(viewLifecycleOwner) { event ->
                    event.getContentIfNotHandled()?.let { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                viewModel.setLoading(true)
                            }

                            is Resource.Success -> {
                                viewModel.setLoading(false)
                                resource.data?.let { register ->
                                    register.token?.let { token ->
                                        viewModel.saveToken(token)
                                        register.user?.let { user ->
                                            viewModel.saveUserId(user.id)
                                        }
                                        requireActivity().finish()
                                    }
                                }
                            }

                            else -> {
                                viewModel.setLoading(false)
                                resource.data?.let { register ->
                                    register.registerError?.let { registerError ->
                                        if (!registerError.name.isNullOrEmpty()) {
                                            binding.etName.error = registerError.name!![0]
                                        }

                                        if (!registerError.email.isNullOrEmpty()) {
                                            binding.etEmail.error = registerError.email!![0]
                                        }

                                        if (!registerError.phone.isNullOrEmpty()) {
                                            binding.etPhone.error = registerError.phone!![0]
                                        }

                                        if (!registerError.password.isNullOrEmpty()) {
                                            binding.etPassword.error = registerError.password!![0]
                                        }
                                    }
                                }

                                if (!resource.message.isNullOrEmpty())
                                    Snackbar.make(binding.root, resource.message.toString(), Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}