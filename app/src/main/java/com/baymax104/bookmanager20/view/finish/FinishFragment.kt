package com.baymax104.bookmanager20.view.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.baymax104.bookmanager20.adapter.FinishAdapter
import com.baymax104.bookmanager20.databinding.FragmentFinishBinding
import com.baymax104.bookmanager20.viewModel.FinishViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 22:56
 *@Version 1
 */
@AndroidEntryPoint
class FinishFragment : Fragment() {

    private lateinit var binding: FragmentFinishBinding

    private val vm: FinishViewModel by activityViewModels()

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
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = FinishAdapter()
        adapter.data = vm.finishBooks.value
        binding.adapter = adapter

        vm.finishBooks.observe(viewLifecycleOwner) {
            adapter.data = it
            if (it.isEmpty()) {
                binding.state.showEmpty()
            }
        }

    }
}