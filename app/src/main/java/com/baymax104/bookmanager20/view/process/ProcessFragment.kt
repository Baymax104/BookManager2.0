package com.baymax104.bookmanager20.view.process

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.baymax104.bookmanager20.adapter.ProcessAdapter
import com.baymax104.bookmanager20.dataSource.FAIL
import com.baymax104.bookmanager20.dataSource.Success
import com.baymax104.bookmanager20.databinding.FragmentProgressBinding
import com.baymax104.bookmanager20.util.*
import com.baymax104.bookmanager20.viewModel.ProcessViewModel
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 22:39
 *@Version 1
 */
@AndroidEntryPoint
@RuntimePermissions
class ProcessFragment : Fragment() {

    lateinit var binding: FragmentProgressBinding

    val vm: ProcessViewModel by activityViewModels()

    @Inject
    lateinit var addWayDialog: AddWayDialog

    @Inject
    lateinit var bookInfoDialog: BookInfoDialog

    private var uriPath: String? = null

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == -1) {
            uriPath?.let {
                ImageUtil.compress(requireContext(), it) { file ->
                    vm.photoUri.value = file?.absolutePath
                }
            }
        }
    }

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
        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm = vm
        val adapter = ProcessAdapter()
        binding.adapter = adapter

        vm.processBooks.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.state.showEmpty()
            }
        }
        vm.observerBuilder.observe {
            add { index, _ ->
                adapter.notifyItemInserted(index)
                binding.bookList.scrollToPosition(index)
            }
        }

        Bus.with<String>(CaptureActivity::class todo "getResult")
            .observeCoroutine(viewLifecycleOwner, MainScopeContext + BaseExceptionHandler) {
                when (val state = vm.requestBookInfo(it)) {
                    is Success -> {
                        bookInfoDialog.show()
                        vm.requestBook.value = state.data
                    }
                    is FAIL -> ToastUtils.showShort(state.message)
                }
            }

        Bus.observeTag(ManualAddDialog::class todo "takePhoto", viewLifecycleOwner) {
            takePhoto()
        }

        binding.add.setOnClickListener {
            addWayDialog.show()
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun takePhoto() {
        val file = ImageUtil.createFile()
        uriPath = file?.absolutePath
        val uri = UriUtils.file2Uri(file)
        val intent = IntentUtils.getCaptureIntent(uri)
        cameraLauncher.launch(intent)
    }
}