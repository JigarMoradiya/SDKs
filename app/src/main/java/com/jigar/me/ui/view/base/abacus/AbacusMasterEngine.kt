package com.jigar.me.ui.view.base.abacus

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.Log
import com.jigar.me.R
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.utils.AppConstants
import java.io.Serializable
import java.util.*

class AbacusMasterEngine(
    private var selectedPositions: ArrayList<Int>,
    private val numColumns: Int,
    private val noOfBeads: Int,
    private val singleBeadValue: Int,
    context: Context,
    roadDrawable: Drawable?,
    selectedBeadDrawable: Drawable?,
    beads: Array<Drawable?>,
    isBeadStackFromBottom: Boolean,
    private val colSpacing: Int,
    noOfRows_used: Int
) {
    class BeadState : Serializable {
        var numRows = 0
        var numBeads = 0
        lateinit var positions: Array<DoubleArray>

        companion object {
            const val serialVersionUID = 3121933153069222515L
        }
    }

    interface OnStateResetCompletedListener {
        fun onStateResetCompleted()
    }

    private var position: Point? = null
    private var rows: Array<AbacusMasterRowEngine?> = arrayOfNulls(numColumns)
    private var paint: Paint? = null
    private var paintBorder: Paint? = null
    private var beadHeight = 0
    private var beadWidth = 0
    private var borderWidth = 0
    private var rowHeight = 0
    private var canvas : Canvas?= null

    init {
        val beadDrawable = beads[0]
//        beadWidth = beadDrawable!!.minimumWidth + colSpacing
//        beadHeight = beadDrawable.minimumHeight

        // size set manually
        val sizes = context.resources.getDimension(R.dimen.bead_dimens).toInt()

        val theme = AppPreferencesHelper(context, AppConstants.PREF_NAME).getCustomParam(
            AppConstants.Settings.TheamTempView, AppConstants.Settings.theam_Default)
        val sizes_width = if (theme.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true)) {
            context.resources.getDimension(R.dimen.bead_dimens_width).toInt()
        }else{
            sizes
        }

        beadWidth = sizes_width + colSpacing
        beadHeight = sizes

        borderWidth = 1
        rowHeight = 11 * beadHeight

        position = Point(0, 0)
        for (i in 0 until numColumns) {
            val rowPosition = Point()
            rowPosition.x = position!!.x + i * beadWidth
            rowPosition.y = position!!.y
            rows[i] = AbacusMasterRowEngine(
                context,
                rowPosition,
                if (selectedPositions.size > i) selectedPositions[i] else -1,
                beadWidth,
                beadHeight,
                beads,
                isBeadStackFromBottom,
                noOfBeads,
                roadDrawable,
                selectedBeadDrawable,
                noOfRows_used,
                numColumns
            )
        }
        paint = Paint()
        paint!!.color = Color.argb(255, 188, 157, 118) // brownish
        paint!!.style = Paint.Style.FILL
        paintBorder = Paint()
        paintBorder!!.color = Color.argb(255, 100, 255, 0) // brownish
        paintBorder!!.style = Paint.Style.STROKE
        paintBorder!!.strokeWidth = 8f
    }

    fun setSelectedPositions(selectedPosition: ArrayList<Int>) {
        selectedPositions = selectedPosition
        for (i in selectedPositions.indices) {
            val rowPosition = Point()
            rowPosition.x = position!!.x + i * beadWidth
            rowPosition.y = position!!.y
            if (rows[i] != null) {
                rows[i]!!.setSelectedPosition(selectedPositions[i])
            }
        }
    }

    fun getRowAt(x: Int, y: Int): AbacusMasterRowEngine? {
        for (i in 0 until numColumns) {
            if (x >= position!!.x + beadWidth * i
                && x <= position!!.x + beadWidth + beadWidth * i
                && y >= position!!.y
                && y <= position!!.y + rowHeight
            ) return rows[i]
        }
        return null
    }

    /**
     * Calculate the total value of all rows in the abacus.  If any of
     * the rows have an indeterminate value (as signaled by a -1 returned
     * by that row's getValue() method, the entire abacus is considered
     * indeterminate and this method will also return -1.
     *
     * @return Current value on the abacus, or -1 if indeterminate
     */
    fun getValue(): Int {
        var accumulator = 0
        for (r in rows) {
            accumulator *= 10
            val rval = r!!.getValue()
            accumulator += if (rval > -1) rval * singleBeadValue else return -1
        }
        return accumulator
    }

    fun getRowValue(): IntArray {
        val accumulator = 0
        val rowval = IntArray(numColumns)
        for ((i, r) in rows.withIndex()) {
            val rval = r!!.getValue()
            rowval[i] = rval
        }
        return rowval
    }
    fun draw(canvass: Canvas?) {
        this.canvas = canvass
        var i = 1
        for (r in rows) {
            r!!.draw(canvas, i == rows.size, colSpacing, i)
            i++
        }
    }

    fun getState(): BeadState {
        val beadState = BeadState()
        beadState.numRows = numColumns
        beadState.numBeads = noOfBeads
        beadState.positions = Array(beadState.numRows) {
            DoubleArray(
                beadState.numBeads
            )
        }
        for (i in 0 until beadState.numRows) {
            for (j in 0 until beadState.numBeads) {
                beadState.positions[i][j] = rows[i]!!.getBeadPosition(j).toDouble() / rowHeight
            }
        }
        return beadState
    }

    fun setState(
        beadState: BeadState,
        isAnimated: Boolean,
        onStateResetCompletedListener: OnStateResetCompletedListener?
    ) {
        if (isAnimated) {
            val currentState = getState()
            val valueAnimator = ValueAnimator.ofFloat(0f, 100f)
            valueAnimator.duration = 400
            valueAnimator.addUpdateListener { valueAnimator ->
                for (i in 0 until beadState.numRows) {
                    for (j in 0 until beadState.numBeads) {
                        val position = beadState.positions[i][j]
                        val currentPosition = currentState.positions[i][j]
                        val totaldiff = position - currentPosition
                        val currentDiff = totaldiff * valueAnimator.animatedFraction
                        val curAnimatedPos = currentPosition + currentDiff
                        rows[i]?.moveBeadToInternal(j, (curAnimatedPos * rowHeight).toInt())
                    }
                }
            }
            valueAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    onStateResetCompletedListener?.onStateResetCompleted()
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            valueAnimator.start()
        } else {
            setState(beadState)
            Handler().postDelayed({ onStateResetCompletedListener!!.onStateResetCompleted() }, 3)
        }
    }

    fun setState(beadState: BeadState) {
        for (i in 0 until beadState.numRows) {
            for (j in 0 until beadState.numBeads) {
                rows[i]!!.moveBeadToInternal(
                    j,
                    (beadState.positions[i][j] * rowHeight).toInt()
                )
            }
        }
    }
}