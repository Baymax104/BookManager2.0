package com.baymax104.bookmanager20.architecture.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.baymax104.bookmanager20.architecture.FragmentViewModelScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/3 18:16
 *@Version 1
 */
@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var viewModelScope: FragmentViewModelScope

    private lateinit var activity: AppCompatActivity

    private var mBinding: ViewDataBinding? = null

    protected open fun initViewModel() {
    }

    protected abstract fun configureBinding(): DataBindingConfig

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val config = configureBinding()
        val (layout, vmId, stateVM) = config
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.setVariable(vmId, stateVM)
        config.params.forEach { variableId, value ->
            binding.setVariable(variableId, value)
        }
        mBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.unbind()
        mBinding = null
    }
}