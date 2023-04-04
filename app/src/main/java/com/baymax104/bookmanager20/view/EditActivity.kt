package com.baymax104.bookmanager20.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.databinding.ActivityEditBinding
import com.baymax104.bookmanager20.viewModel.EditViewModel
import com.drake.statusbar.immersive
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    val vm: EditViewModel by viewModels()

    companion object {
        fun actionStart(context: Context) {
            context.startActivity(Intent(context, EditActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        binding.lifecycleOwner = this

        initWindow()

        Log.i("BM-Edit", vm.toString())

    }


    private fun initWindow() {
        immersive(binding.toolbar, true)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

}