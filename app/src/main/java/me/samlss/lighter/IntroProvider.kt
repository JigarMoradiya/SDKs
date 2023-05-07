package me.samlss.lighter

import android.util.Log
import android.view.View
import com.jigar.me.R
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import me.samlss.lighter.interfaces.OnLighterListener
import me.samlss.lighter.parameter.Direction
import me.samlss.lighter.parameter.LighterParameter
import me.samlss.lighter.parameter.MarginOffset
import me.samlss.lighter.shape.RectShape

object IntroProvider {
    fun videoTutorialIntro(prefManager : AppPreferencesHelper, lighter: Lighter?, freeModeView: View, videoTutorialView: View, exerciseView: View, examView: View, numberPuzzleView: View){
        try {
            val corner = freeModeView.context.resources.getDimension(R.dimen.home_menu_corner)
            lighter?.setOnLighterListener(object : OnLighterListener {
                    override fun onDismiss() {
                        prefManager.setCustomParamBoolean(AppConstants.Settings.isTourWatch, true)
                    }
                    override fun onShow(index: Int) = Unit
                })?.setBackgroundColor(0xB3000000.toInt())
                ?.addHighlight(
                    LighterParameter.Builder()
                        .setHighlightedView(freeModeView)
                        .setTipLayoutId(R.layout.layout_tip_free_mode)
                        .setLighterShape(RectShape(corner, corner, corner))
                        .setTipViewRelativeDirection(Direction.RIGHT)
                        .setTipViewDisplayAnimation(LighterHelper.getScaleAnimation())
                        .setTipViewRelativeOffset(MarginOffset(0, 0, 0, 0))
                        .build()
                )
                ?.addHighlight(
                    LighterParameter.Builder()
                        .setHighlightedView(videoTutorialView)
                        .setTipLayoutId(R.layout.layout_tip_video_tutorial)
                        .setLighterShape(RectShape(corner, corner, corner))
                        .setTipViewRelativeDirection(Direction.LEFT)
                        .setTipViewDisplayAnimation(LighterHelper.getScaleAnimation())
                        .setTipViewRelativeOffset(MarginOffset(0, 0, 0, 0))
                        .build()
                )
                ?.addHighlight(
                    LighterParameter.Builder()
                        .setHighlightedView(exerciseView)
                        .setTipLayoutId(R.layout.layout_tip_exercise)
                        .setLighterShape(RectShape(corner, corner, corner))
                        .setTipViewRelativeDirection(Direction.TOP)
                        .setTipViewDisplayAnimation(LighterHelper.getScaleAnimation())
                        .setTipViewRelativeOffset(MarginOffset(0, 0, 0, 0))
                        .build()
                )
                ?.addHighlight(
                    LighterParameter.Builder()
                        .setHighlightedView(examView)
                        .setTipLayoutId(R.layout.layout_tip_exam)
                        .setLighterShape(RectShape(corner, corner, corner))
                        .setTipViewRelativeDirection(Direction.RIGHT)
                        .setTipViewDisplayAnimation(LighterHelper.getScaleAnimation())
                        .setTipViewRelativeOffset(MarginOffset(0, 0, 0, 0))
                        .build()
                )
                ?.addHighlight(
                    LighterParameter.Builder()
                        .setHighlightedView(numberPuzzleView)
                        .setTipLayoutId(R.layout.layout_tip_number_sequence)
                        .setLighterShape(RectShape(corner, corner, corner))
                        .setTipViewRelativeDirection(Direction.TOP)
                        .setTipViewDisplayAnimation(LighterHelper.getScaleAnimation())
                        .setTipViewRelativeOffset(MarginOffset(0, 0, 0, 0))
                        .build()
                )
                ?.show()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}