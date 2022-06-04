package com.example.segarbox.ui.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.FragmentLoginBinding
import com.example.segarbox.helper.getColorFromAttr
import com.example.segarbox.ui.viewmodel.LoginViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.R.attr.colorOnSecondary
import com.google.android.material.R.attr.colorPrimary

private val Context.dataStore by preferencesDataStore(name = "settings")
class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(requireActivity().dataStore))
    }

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
        loginViewModel.loginResponse.observe(viewLifecycleOwner) { loginResponse ->

            // Jika Berhasil Login
            if (loginResponse.token != null) {
                prefViewModel.saveToken(loginResponse.token)
                requireActivity().finish()
            }
            // Jika tidak berhasil login
            else {

                // Jika error input
                if (loginResponse.errors != null) {

                    loginResponse.errors.apply {
                        if (this.email != null && this.email.isNotEmpty()) {
                            binding.etEmail.error = this.email[0]
                        }

                        if (this.password != null && this.password.isNotEmpty()) {
                            binding.etPassword.error = this.password[0]
                        }
                    }
                }
                // Jika error dari catch
                else {
                    loginResponse.message?.let {
                        binding.etPassword.error = it
                    }
                }

            }
        }

        loginViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.btnLogin.isEnabled = !isLoading
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                loginViewModel.login(
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString()
                )
            }
        }
    }

}