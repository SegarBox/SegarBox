package com.example.segarbox.ui.transaction

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.Resource
import com.example.core.ui.TransactionsAdapter
import com.example.core.utils.Code
import com.example.core.utils.tokenFormat
import com.example.segarbox.databinding.FragmentInProgressBinding
import com.example.segarbox.ui.invoice.InvoiceActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InProgressFragment : Fragment(), TransactionsAdapter.OnItemTransactionsClickCallback {

    private var _binding: FragmentInProgressBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private val transactionsAdapter = TransactionsAdapter(this)
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInProgressBinding.inflate(layoutInflater, container, false)
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

        viewModel.getToken().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                this.token = it
            }
        }

        viewModel.getTransactionsResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            viewModel.setLoading(false)
                            transactionsAdapter.submitList(it)
                        }
                    }

                    is Resource.Empty -> {
                        viewModel.setLoading(false)
                    }

                    else -> {
                        resource.message?.let {
                            viewModel.setLoading(false)
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                        }
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                binding.progressBar.isVisible = isLoading
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (token.isNotEmpty()) {
            viewModel.getTransactions(token.tokenFormat(), "inprogress")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBtnClicked(transactionId: Int) {
        val intent = Intent(requireContext(), InvoiceActivity::class.java)
        intent.putExtra(Code.KEY_TRANSACTION_ID, transactionId)
        startActivity(intent)
    }

    override fun onRootClicked(transactionId: Int) {
        val intent = Intent(requireContext(), InvoiceActivity::class.java)
        intent.putExtra(Code.KEY_TRANSACTION_ID, transactionId)
        startActivity(intent)
    }
}