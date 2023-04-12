package com.baymax104.bookmanager20.view.process

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.domain.applicationViewModels
import com.baymax104.bookmanager20.domain.ProcessMessenger
import com.drake.statusbar.immersive
import com.drake.statusbar.setFullscreen
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.king.zxing.DecodeConfig
import com.king.zxing.analyze.MultiFormatAnalyzer

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/20 15:53
 *@Version 1
 */
class CaptureActivity : com.king.zxing.CaptureActivity() {

    private val isbnReg =
        Regex("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}\$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}\$|97[89][0-9]{10}\$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}\$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]\$")

    private val hints: Map<DecodeHintType, Any> = mapOf(
        DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.EAN_13),
        DecodeHintType.TRY_HARDER to BarcodeFormat.EAN_13,
        DecodeHintType.CHARACTER_SET to "UTF-8"
    )

    private val messenger: ProcessMessenger by applicationViewModels()

    override fun initCameraScan() {
        super.initCameraScan()

        val config = DecodeConfig().setHints(hints)

        cameraScan
            .setAnalyzer(MultiFormatAnalyzer(config))
            .setNeedAutoZoom(true)
            .setDarkLightLux(35f)
            .setBrightLightLux(40f)
            .bindFlashlightView(ivFlashlight)
            .setOnScanResultCallback {
                val code = it.text
                if (code.matches(isbnReg)) {
                    messenger.requestBook.post(code)
                    cameraScan.setAnalyzeImage(false)
                    return@setOnScanResultCallback false
                }
                cameraScan.setAnalyzeImage(true)
                true
            }
    }

    override fun getLayoutId(): Int = R.layout.activity_capture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        immersive(toolBar)
        setFullscreen(true)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}