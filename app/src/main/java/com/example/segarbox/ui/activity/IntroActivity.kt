package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.databinding.ActivityIntroBinding
import com.example.segarbox.helper.getHelperColor
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")
class IntroActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityIntroBinding? = null
    private val binding get() = _binding!!
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setNavigationBar()
        prefViewModel.saveIntro(true)
        binding.skipLayout.setOnClickListener(this)
        binding.tvGetStarted.setOnClickListener(this)
    }

    private fun setNavigationBar() {
        window.navigationBarColor = this.getHelperColor(R.color.grey)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.
            skip_layout -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.tv_get_started -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}