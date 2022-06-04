package com.example.segarbox.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.model.DummyModelTransaction
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.FragmentHistoryBinding
import com.example.segarbox.databinding.FragmentInProgressBinding
import com.example.segarbox.helper.tokenFormat
import com.example.segarbox.ui.activity.InvoiceActivity
import com.example.segarbox.ui.adapter.DummyAdapterTransaction
import com.example.segarbox.ui.adapter.TransactionsAdapter
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.example.segarbox.ui.viewmodel.TransactionViewModel

private val Context.dataStore by preferencesDataStore(name = "settings")
class HistoryFragment : Fragment(), TransactionsAdapter.OnItemTransactionsClickCallback {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private val transactionsAdapter = TransactionsAdapter(this)
    private val transactionViewModel by viewModels<TransactionViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(requireActivity().dataStore))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        setAdapter()
        observeData()
    }

    private fun setAdapter() {
        binding.rvTransaction.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = transactionsAdapter
        }
    }

    private fun observeData() {

        prefViewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
        }

        transactionViewModel.transactionsResponse.observe(viewLifecycleOwner) { event ->
            Log.e("HISTORY", "ON RESUME")
            event.getContentIfNotHandled()?.let { transactionsResponse ->
                transactionsResponse.data?.let {
                    transactionsAdapter.submitList(it)
                }

                transactionsResponse.message?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }

        }

        transactionViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }

    override fun onResume() {
        super.onResume()
        if (token.isNotEmpty()) {
            transactionViewModel.getTransactions(token.tokenFormat(), "complete")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBtnClicked(transactionId: Int) {}

    override fun onRootClicked(transactionId: Int) {
        val intent = Intent(requireContext(), InvoiceActivity::class.java)
        intent.putExtra(Code.KEY_TRANSACTION_ID, transactionId)
        startActivity(intent)
    }
}