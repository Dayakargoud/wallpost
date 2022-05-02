package com.dayakar.wallpost

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @Created By DAYAKAR GOUD BANDARI on 30-04-2022.
 */

@HiltAndroidApp
class PhotoApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }
}