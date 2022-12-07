package com.example.segarbox.ui.login

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.data.source.local.datastore.SettingPreferences
import com.example.core.utils.getColorFromAttr
import com.example.segarbox.R
import com.example.segarbox.databinding.FragmentRegisterBinding
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.R.attr.colorOnSecondary
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.snackbar.Snackbar

private val Context.dataStore by preferencesDataStore(name = "settings")

class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(requireActivity().dataStore))
    }
    private val registerViewModel by viewModels<RegisterViewModel> {
        RetrofitViewModelFactory.getInstance(com.example.core.data.RetrofitRepository())
    }

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
        registerViewModel.registerResponse.observe(viewLifecycleOwner) { registerResponse ->
            // Jika berhasil register
            when {
                registerResponse.token != null -> {
                    prefViewModel.saveToken(registerResponse.token!!)
                    registerResponse.user?.let {
                        prefViewModel.saveUserId(it.id)
                    }
                    requireActivity().finish()
                }
                // Jika tidak berhasil register
                else -> {

                    // Jika error input
                    when {
                        registerResponse.errors != null -> {

                            registerResponse.errors.apply {
                                this?.let {
                                    if (!it.name.isNullOrEmpty()) {
                                        binding.etName.error = it.name!![0]
                                    }

                                    if (!it.email.isNullOrEmpty()) {
                                        binding.etEmail.error = it.email!![0]
                                    }

                                    if (!it.phone.isNullOrEmpty()) {
                                        binding.etPhone.error = it.phone!![0]
                                    }

                                    if (!it.password.isNullOrEmpty()) {
                                        binding.etPassword.error = it.password!![0]
                                    }
                                }
                            }
                        }

                        // Jika error dari catch
                        else -> {
                            registerResponse.message?.let {
                                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                            }
                        }
                    }
                }
            }
        }

        registerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.isVisible = isLoading
            binding.btnRegister.isEnabled = !isLoading
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                registerViewModel.register(
                    name = binding.etName.text.toString(),
                    email = binding.etEmail.text.toString(),
                    phone = binding.etPhone.text.toString(),
                    password = binding.etPassword.text.toString(),
                    password_confirmation = binding.etConPassword.text.toString()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}