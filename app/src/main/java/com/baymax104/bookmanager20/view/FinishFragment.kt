package com.baymax104.bookmanager20.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.baymax104.bookmanager20.adapter.FinishAdapter
import com.baymax104.bookmanager20.databinding.FragmentFinishBinding
import com.baymax104.bookmanager20.viewModel.FinishViewModel

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 22:56
 *@Version 1
 */
class FinishFragment : Fragment() {

    private lateinit var binding: FragmentFinishBinding

    private lateinit var vm: FinishViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.lifecycleOwner = viewLifecycleOwner
        vm = ViewModelProvider(requireActivity())[FinishViewModel::class.java]

        binding.vm = vm
        val adapter = FinishAdapter()
        binding.adapter = adapter

        vm.finishBooks.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.state.showEmpty()
            }
        }

    }
}