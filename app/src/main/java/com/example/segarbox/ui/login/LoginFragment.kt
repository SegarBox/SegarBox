package com.example.segarbox.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.data.Resource
import com.example.core.utils.getColorFromAttr
import com.example.segarbox.R
import com.example.segarbox.databinding.FragmentLoginBinding
import com.google.android.material.R.attr.colorOnSecondary
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

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
        observeData()
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

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                binding.progressBar.isVisible = isLoading
                binding.btnLogin.isEnabled = !isLoading
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                viewModel.login(
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString()
                ).observe(viewLifecycleOwner) { event ->
                    event.getContentIfNotHandled()?.let { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                viewModel.setLoading(true)
                            }

                            is Resource.Success -> {
                                viewModel.setLoading(false)
                                resource.data?.let { login ->
                                    login.token?.let { token ->
                                        viewModel.saveToken(token)
                                        login.user?.let { user ->
                                            viewModel.saveUserId(user.id)
                                        }
                                        requireActivity().finish()
                                    }
                                }
                            }

                            else -> {
                                viewModel.setLoading(false)
                                resource.data?.let { login ->
                                    login.message?.let { loginErrorMessage ->
                                        Snackbar.make(binding.root,
                                            loginErrorMessage,
                                            Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
                                    }
                                    login.loginError?.let { loginError ->
                                        if (!loginError.email.isNullOrEmpty()) {
                                            binding.etEmail.error = loginError.email!![0]
                                        }

                                        if (!loginError.password.isNullOrEmpty()) {
                                            binding.etPassword.error = loginError.password!![0]
                                        }
                                    }
                                }

                                if (!resource.message.isNullOrEmpty())
                                    Snackbar.make(binding.root,
                                        resource.message.toString(),
                                        Snackbar.LENGTH_SHORT).setAction("OK") {}.show()

                            }
                        }
                    }
                }
            }
        }
    }

}