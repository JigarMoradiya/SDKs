package com.jigar.me.ui.view.base.abacus

import android.content.Context
import android.media.MediaPlayer
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import java.io.IOException

object AbacusMasterSound {
    private val player = MediaPlayer()
    @JvmStatic
    fun playClickSound(context: Context?) {
        play(context!!, "click.wav")
    }
    @JvmStatic
    fun playResetSound(context: Context?) {
        play(context!!, "reset.wav")
    }
    @JvmStatic
    private fun play(context: Context, fileName: String) {
        if (AppPreferencesHelper(context, AppConstants.PREF_NAME)
                .getCustomParamBoolean(AppConstants.Settings.Setting_sound,true)
        ) {
            try {
                val afd = context.assets.openFd(fileName)
                if (player.isPlaying) {
                    player.stop()
                }
                player.reset()
                player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                player.prepare()
                player.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}