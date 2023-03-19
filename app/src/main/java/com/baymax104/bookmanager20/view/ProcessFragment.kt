package com.baymax104.bookmanager20.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.baymax104.bookmanager20.adapter.ProcessAdapter
import com.baymax104.bookmanager20.databinding.FragmentProgressBinding
import com.baymax104.bookmanager20.viewModel.ProcessViewModel

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 22:39
 *@Version 1
 */
class ProcessFragment : Fragment() {

    private lateinit var binding: FragmentProgressBinding

    private lateinit var vm: ProcessViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.lifecycleOwner = viewLifecycleOwner
        vm = ViewModelProvider(requireActivity())[ProcessViewModel::class.java]

        binding.vm = vm
        val adapter = ProcessAdapter()
        binding.adapter = adapter

        vm.processBooks.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.state.showEmpty()
            }
        }
    }
}