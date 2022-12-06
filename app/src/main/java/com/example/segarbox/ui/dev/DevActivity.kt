package com.example.segarbox.ui.dev

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import com.example.segarbox.R
import com.example.segarbox.core.data.source.local.datastore.SettingPreferences
import com.example.segarbox.databinding.ActivityDevBinding
import com.example.segarbox.ui.home.MainActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")

class DevActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityDevBinding? = null
    private val binding get() = _binding!!

    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.btnSaveUrl.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save_url -> {
                prefViewModel.saveBaseUrl(binding.etBaseUrl.text.toString())
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}