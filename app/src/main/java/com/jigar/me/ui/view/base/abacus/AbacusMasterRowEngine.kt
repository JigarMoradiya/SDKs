package com.jigar.me.ui.view.base.abacus

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import com.jigar.me.R
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import java.util.*

class AbacusMasterRowEngine(
    private val context: Context,
    private val position: Point,
    selectedPosition: Int,
    private val beadWidth: Int,
    beadHeight1: Int,
    private var beadDrawables: Array<Drawable?>,
    private val isBeadStackFromBottom: Boolean,
    private var numBeads: Int,
    private val roadDrawable: Drawable?,
    private val selectedBeadDrawable: Drawable?,
    private val noOfRows_used: Int,
    private var numColumns: Int
) {

    var tempBeads: IntArray? = null
    var beadHeight: Int = 0
    var beads: IntArray

    /**
     * Total width of the row in real device pixels
     */
    private var height = 0

    /**
     * The beads' width in real device pixels
     */

    /**
     * The beads' height in real device pixels
     */

    /**
     * The number of beads on this row
     */

    /**
     * The horizontal location of the center of each bead on the row
     */
//     var beads: IntArray? = null
//      var tempBeads: IntArray? = null
    private lateinit var tempBeads_new: IntArray

    private var beadPaint: Paint
    private var rowPaint: Paint

    private var beadDrawables_eyes: ArrayList<Drawable?> = arrayListOf()
    private var beadDrawables_eyes_smaile: ArrayList<Drawable?> = arrayListOf()
    private var isColorFull: Boolean = false
    private var selectedPositions: ArrayList<Int>? = null
    private var ExtraHeight = 0
    private var theam = AppConstants.Settings.theam_Default
    init{
        beadHeight = beadHeight1
        ExtraHeight =
            context.resources.getDimension(R.dimen.eight_dp).toInt()
        selectedPositions = ArrayList()
        height = (numBeads + 1) * beadHeight
        beadPaint = Paint()
        beadPaint.color = Color.argb(255, 73, 137, 30)
        beadPaint.style = Paint.Style.FILL
        beadDrawables_eyes = ArrayList()
        beadDrawables_eyes_smaile = ArrayList()
        theam = AppPreferencesHelper(context,AppConstants.PREF_NAME)
            .getCustomParam(AppConstants.Settings.TheamTempView, AppConstants.Settings.theam_Default)
        when {
            theam.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true) -> {
                beadDrawables_eyes.add(ContextCompat.getDrawable(context, R.drawable.poligon_gray))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.poligon_gray))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.poligon_gray))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.poligon_gray))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.poligon_blue))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.poligon_blue))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.poligon_blue))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.poligon_blue))
            }
            theam.equals(AppConstants.Settings.theam_eyes, ignoreCase = true) -> {
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.face_pink_close))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.face_orange_close))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.face_blue_close))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.face_green_close))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.face_pink_open))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.face_orange_open))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.face_blue_open))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.face_green_open))
            }
            theam.equals(AppConstants.Settings.theam_shape, ignoreCase = true) -> {
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.shape_triangle_gray))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.shape_stone_gray))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.shape_circle_gray))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.shape_hexagon_gray))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.shape_triangle))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.shape_stone))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.shape_circle))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.shape_hexagon))
            }
            theam.equals(AppConstants.Settings.theam_Star, ignoreCase = true) -> {
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.star_yellow_close))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.star_blue_close))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.star_orange_close))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.star_green_close))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.star_yellow_open))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.star_blue_open))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.star_orange_open))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.star_green_open))
            }
            theam.equals(AppConstants.Settings.theam_Egg, ignoreCase = true) -> {
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.egg1))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.egg4))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.egg2))
                beadDrawables_eyes.add(ContextCompat.getDrawable(context,R.drawable.egg3))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.egg1))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.egg4))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.egg2))
                beadDrawables_eyes_smaile.add(ContextCompat.getDrawable(context,R.drawable.egg3))
            }
        }
        rowPaint = Paint()
        beads = IntArray(numBeads)
        setSelectedPosition(selectedPosition)
        isColorFull = AppPreferencesHelper(
            context,
            AppConstants.PREF_NAME
        ).getCustomParamBoolean(AppConstants.Settings.abacus_colorful,true)

    }

    fun setSelectedPosition(selectedPosition: Int) {
        val heightDiff = if (isBeadStackFromBottom) height - beadHeight * numBeads else 0
        val list_abacus = ArrayList<String?>()
        tempBeads_new = IntArray(4)
        //        tempBeads_new[0] =0;
        for (i in 0 until numBeads) {
            if (i <= selectedPosition && !isBeadStackFromBottom) {
                // this is ok. for uppar portion
                beads[i] = heightDiff + i * beadHeight + beadHeight
            } else if (i <= selectedPosition && isBeadStackFromBottom) {
                beads[i] = i * beadHeight
            } else {
                beads[i] = heightDiff + i * beadHeight
            }
            if (isBeadStackFromBottom) {
                tempBeads_new[0] = 0
            }
            tempBeads_new[i] = heightDiff + i * beadHeight
            list_abacus.add("value" + beads[i])
        }
        if (AppPreferencesHelper(context, AppConstants.PREF_NAME)
                .getCustomParam("column$isBeadStackFromBottom$numColumns","").isEmpty()
        ) {
            val allIds = TextUtils.join(",", list_abacus)
            AppPreferencesHelper(context, AppConstants.PREF_NAME)
                .setCustomParam("column$isBeadStackFromBottom$numColumns", allIds)
        }
        numColumns--
    }

    fun getPosition(): Point {
        return position
    }

    fun moveBeadTo(i: Int, y: Int): Int {
        return moveBeadToInternal(i, y - position.y)
    }

    fun moveBeadToInternal(i: Int, y1: Int): Int {
        // Don't allow beads to be dragged off the ends of the row
        var y = y1
        y = if (y >= 0) y else 0
        y = if (y <= height - beadHeight) y else height - beadHeight

        // Handle collisions between beads
        if (y > beads[i]) {
            // ... when moving right:
            if (i < numBeads - 1
                && y + beadHeight > beads[i + 1]
            ) {
                y = moveBeadToInternal(i + 1, y + beadHeight) - beadHeight
            }
        } else if (y < beads[i]) {
            // ... when moving left:
            if (i > 0
                && y - beadHeight < beads[i - 1]
            ) {
                y = moveBeadToInternal(i - 1, y - beadHeight) + beadHeight
            }
        }
        return y.also { beads[i] = it }
    }


    /**
     * Get the row's current value
     *
     * @return Current value set on the row, or -1 if indeterminate
     */
    fun getValue(): Int {
        var `val` = 0
        if (beads[0] >= 0.5 * beadHeight) {
            `val` = numBeads
        } else {
//            if (numBeads == 1) {
//                val = beads[0] < beadHeight ? 1 : 0;
//            } else {
            for (i in 0 until numBeads - 1) {
                if (beads[i + 1] - beads[i] >= 1.5 * beadHeight) {
                    `val` = numBeads - 1 - i
                    break
                }
                if (beads[numBeads - 1] <= height - 1.5 * beadHeight) {
                    `val` = 0
                    break
                }
                //                }
            }
        }

        return if (isBeadStackFromBottom) {
            numBeads - `val`
        } else {
            `val`
        }
    }

    fun draw(canvas: Canvas?, isLastRow: Boolean, rowSpacing: Int, col: Int) {
        val road = roadDrawable!!
        val rowThickness = road.minimumWidth
        val startX = position.x + beadWidth / 2 - rowThickness / 2
        val endX = position.x + beadWidth / 2 + rowThickness / 2
        road.setBounds(startX,position.y,endX,position.y + height + ExtraHeight)
        road.draw(canvas!!)
        var drawablePos = 0
        val listAbacus = ArrayList<String>()
        var tempvalue = -1
        val str: String = AppPreferencesHelper(context,AppConstants.PREF_NAME).getCustomParam("column$isBeadStackFromBottom","")
        if (str.isNotEmpty()) {
            val strAll = str.split(",".toRegex()).toTypedArray()
            val listTemp = ArrayList<String?>()
            for (i in 0 until numBeads) {
                listTemp.add("value" + beads[i])
            }
            val allIds = TextUtils.join(",", listTemp)
            for (j in strAll.indices) {
                if (!allIds.contains(strAll[j])) {
                    tempvalue = strAll[j].replace("value", "").toInt()
                    break
                }
            }
        }
        for (i in 0 until numBeads) {
            if (beadDrawables.size == drawablePos) {
                drawablePos = 0
            }
            var beadDrawable = beadDrawables[drawablePos]

            if (isBeadStackFromBottom) {
                val temp = beads[i] + 2
//                Log.e("jigarLogs","i = "+i)
//                Log.e("jigarLogs","beads = "+beads[i])
//                Log.e("jigarLogs","temp = "+temp)
//                Log.e("jigarLogs","tempvalue = "+tempvalue)
                beadDrawable = if (tempvalue > temp && tempvalue != -1) {
                    beadDrawables_eyes_smaile[drawablePos]!!
                } else {
                    beadDrawables_eyes[drawablePos]!!
                }
//                if (tempvalue > temp && tempvalue != -1) {
//                    beadDrawable = if (!isColorFull || noOfRows_used >= col) {
//                        when {
//                            theam.equals(AppConstants.Settings.theam_Egg,ignoreCase = true) -> ContextCompat.getDrawable(context,R.drawable.egg)!!
//                            else -> beadDrawables_eyes_smaile[drawablePos]!!
//                        }
//                    } else {
//                        beadDrawables_eyes_smaile[drawablePos]!!
//                    }
//                } else {
//                    beadDrawable = if (!isColorFull || noOfRows_used >= col) {
//                        when {
//                            theam.equals(AppConstants.Settings.theam_Egg,ignoreCase = true) -> ContextCompat.getDrawable(context,R.drawable.egg)!!
//                            else -> beadDrawables_eyes[drawablePos]!!
//                        }
//                    } else {
//                        beadDrawables_eyes[drawablePos]!!
//                    }
//                }
            } else {
//                Log.e("jigarLogs","Top beads = "+beads[i])
                if (beads[i] > 0) {
                    when {
                        theam.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.poligon_blue)!!
                        }
                        theam.equals(AppConstants.Settings.theam_eyes, ignoreCase = true) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.face_red_open)!!
                        }
                        theam.equals(AppConstants.Settings.theam_shape,ignoreCase = true) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.shape_square)!!
                        }
                        theam.equals(AppConstants.Settings.theam_Star,ignoreCase = true) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.star_red_open)!!
                        }
                        theam.equals(
                            AppConstants.Settings.theam_Egg,
                            ignoreCase = true
                        ) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.egg0)!!
                        }
                    }
                }else{
                    when {
                        theam.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.poligon_gray)!!
                        }
                        theam.equals(AppConstants.Settings.theam_eyes, ignoreCase = true) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.face_red_close)!!
                        }
                        theam.equals(AppConstants.Settings.theam_shape,ignoreCase = true) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.shape_square_gray)!!
                        }
                        theam.equals(AppConstants.Settings.theam_Star,ignoreCase = true) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.star_red_close)!!
                        }
                        theam.equals(
                            AppConstants.Settings.theam_Egg,
                            ignoreCase = true
                        ) -> {
                            beadDrawable = ContextCompat.getDrawable(context,R.drawable.egg0)!!
                        }
                    }
                }


                // bottom beads
//                if (beads[i] > 0) {
//                    if (!isColorFull || noOfRows_used >= col) {
//                        when {
//                            theam.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.poligon_gray)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_eyes, ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.face_gray_open)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_shape,ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.shape_square_gray)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_Star,ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.star_gray_open)!!
//                            }
//                            theam.equals(
//                                AppConstants.Settings.theam_Egg,
//                                ignoreCase = true
//                            ) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.egg)!!
//                            }
//                        }
//                    } else {
//                        when {
//                            theam.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.poligon_blue)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_eyes, ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.face_red_open)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_shape,ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.shape_square)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_Star,ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.star_red_open)!!
//                            }
//                            theam.equals(
//                                AppConstants.Settings.theam_Egg,
//                                ignoreCase = true
//                            ) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.egg0)!!
//                            }
//                        }
//                    }
//                } else {
//                    if (!isColorFull || noOfRows_used >= col) {
//                        when {
//                            theam.equals(AppConstants.Settings.theam_eyes, ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.face_gray_open)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_Poligon,ignoreCase = true) -> {
//                                beadDrawable = beadDrawables_eyes[drawablePos]!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_shape,ignoreCase = true) -> {
//                                beadDrawable = beadDrawables_eyes[drawablePos]!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_Star,ignoreCase = true) -> {
//                                beadDrawable = beadDrawables_eyes[drawablePos]!!
//                            }
//                            theam.equals(
//                                AppConstants.Settings.theam_Egg,
//                                ignoreCase = true
//                            ) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.egg)!!
//                            }
//                        }
//                    } else {
//                        when {
//                            theam.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.poligon_gray)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_eyes, ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.face_red_close)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_shape,ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.shape_square_gray)!!
//                            }
//                            theam.equals(AppConstants.Settings.theam_Star,ignoreCase = true) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.star_red_close)!!
//                            }
//                            theam.equals(
//                                AppConstants.Settings.theam_Egg,
//                                ignoreCase = true
//                            ) -> {
//                                beadDrawable = ContextCompat.getDrawable(context,R.drawable.egg0)!!
//                            }
//                        }
//                        //                        beadDrawable = context.getResources().getDrawable(R.drawable.abacus_face_blue);
//                    }
//                }
            }
            var isBeadSelected = false
            if (tempBeads != null && tempBeads!!.size > i && tempBeads!![i] != beads[i] && selectedBeadDrawable != null) {
                /*bead is selected*/
                when {
                    !theam.equals(AppConstants.Settings.theam_shape, ignoreCase = true) -> {
                        beadDrawable = selectedBeadDrawable
                    }
                }
                isBeadSelected = true
            }
            if (isColorFull || isBeadSelected) {
                beadDrawable?.setBounds(
                    position.x + rowSpacing / 2,
                    position.y + beads[i] + if (!isBeadStackFromBottom) ExtraHeight else 0,
                    position.x + beadWidth - rowSpacing / 2,
                    position.y + beads[i] + beadHeight + if (!isBeadStackFromBottom) ExtraHeight else 0
                )
                beadDrawable?.draw(canvas)
            }
            if (!isColorFull) {
                beadDrawable?.setBounds(
                    position.x + rowSpacing / 2,
                    position.y + beads[i] + if (!isBeadStackFromBottom) ExtraHeight else 0,
                    position.x + beadWidth - rowSpacing / 2,
                    position.y + beads[i] + beadHeight + if (!isBeadStackFromBottom) ExtraHeight else 0
                )
                beadDrawable?.draw(canvas)
            }

            // TODO Draw direction
            drawablePos++

            if (isBeadStackFromBottom && AppPreferencesHelper(context,AppConstants.PREF_NAME).getCustomParam("column$isBeadStackFromBottom","").isEmpty()) {
                listAbacus.add("value" + beads[i])
            }
        }
        if (isBeadStackFromBottom && AppPreferencesHelper(context, AppConstants.PREF_NAME).getCustomParam("column$isBeadStackFromBottom","").isEmpty()) {
            listAbacus.add("value0")
            val allIds = TextUtils.join(",", listAbacus)
            AppPreferencesHelper(context, AppConstants.PREF_NAME).setCustomParam("column$isBeadStackFromBottom",allIds)
        }
    }

    fun getBeadAt(x: Int, y: Int): Int {
        for (i in 0 until numBeads) {
            if (x >= position.x
                && x <= position.x + beadWidth
                && y >= position.y + beads[i]
                && y <= position.y + beads[i] + beadHeight
            ) {
                return i
            }
        }
        return -1
    }

    fun getBeadPosition(i: Int): Int {
        return beads[i]
    }
}