package com.jigar.me.ui.view.base.abacus


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.jigar.me.R
import com.jigar.me.data.pref.AppPreferencesHelper
import com.jigar.me.ui.view.base.abacus.AbacusMasterSound.playClickSound
import com.jigar.me.ui.view.base.abacus.AbacusMasterSound.playResetSound
import com.jigar.me.utils.AppConstants


/**
 * View that draws, handles touch events for a visual abacus.
 */
class AbacusMasterView(context: Context, attrs: AttributeSet?) :
    SurfaceView(context, attrs), SurfaceHolder.Callback {
    private val fireAccumulator = 0L

    internal inner class DrawThread(surfaceHolder: SurfaceHolder?) : Thread() {
        private var isSingleDraw = false
        private var isPaused = false
        private var isSleep = false
        var isStopDrawing = false
            private set

        /**
         * Politely ask the thread to stop running.
         *  
         */
        fun stopDrawing() {
            isStopDrawing = true
        }

        /**
         * Pause the drawing thread by acquiring the internal draw mutex.
         * Only a single thread should be allowed to call either this method
         * or resumeDrawing().
         *
         * @throws InterruptedException
         */
        @Throws(InterruptedException::class)
        fun pauseDrawing() {
            if (!isPaused) {
                isPaused = true
            }
        }

        /**
         * Allow the drawing thread to resume by releasing the internal draw
         * mutex.  Only a single thread should be allowed to call either this
         * method or pauseDrawing().
         */
        fun resumeDrawing() {
            if (isPaused) {
//                sem.release();
                isPaused = false
            }
        }

        fun setSleep(isSleep: Boolean) {
            this.isSleep = isSleep
        }

        fun drawOneTime() {
            isSleep = false
            isSingleDraw = true
        }

        var onSingleDrawingCompletedListener: OnSingleDrawingCompletedListener? = null
        fun drawOneTime(onSingleDrawingCompletedListener: OnSingleDrawingCompletedListener?) {
            this.onSingleDrawingCompletedListener = onSingleDrawingCompletedListener
            isSleep = false
            isSingleDraw = true
        }

        override fun run() {
            while (!isStopDrawing && !isInterrupted) {
                if (isSleep) {
                    try {
                        sleep(50)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                } else {
                    var c: Canvas? = null
                    try {
                        clearTempBeads()
                        c = mSurfaceHolder!!.lockCanvas(null)
                        synchronized(mSurfaceHolder!!) { doDraw(c) }
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    } finally {
                        if (c != null) mSurfaceHolder!!.unlockCanvasAndPost(c)
                        if (isSingleDraw) {
                            isSingleDraw = false
                            if (onSingleDrawingCompletedListener != null) {
                                onSingleDrawingCompletedListener!!.onSingleDrawingCompleted()
                                onSingleDrawingCompletedListener = null
                            } else {
                                isSleep = true
                            }
                        }
                    }
                }
            }
        }

        init {
            mSurfaceHolder = surfaceHolder
        }
    }

    /**
     * Handle to the surface manager object we interact with
     */
    private var mSurfaceHolder: SurfaceHolder? = null
    private var thread: DrawThread? = null
    var engine: AbacusMasterEngine? = null
        private set
    private var motionRow: AbacusMasterRowEngine? = null
    private var motionBead = -1

    /*todo: need to add this as attribute*/
    private var colSpacing = 15
    private var beadState: AbacusMasterEngine.BeadState? = null
    private var defaultState: AbacusMasterEngine.BeadState? = null
    internal var noOfColumn = 1
    private var noOfRows_used = 9
    private val isBeadStackFromBottom: Boolean
    private val beadDrawables: Array<Drawable?>
    private var roadDrawable: Drawable?
    private var selectedBeadDrawable: Drawable?
    private var noOfBeads: Int
    val singleBeadValue: Int
    private var isInitialized = false
    var onBeadShiftListener: AbacusMasterBeadShiftListener? = null
    var isSoundEnabled = true
    private var selectedPositions: ArrayList<Int>? = null
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setNoOfRowAndBeads(noOfRows_used, noOfColumn, noOfBeads, colSpacing)
    }

    fun getNoOfColumn(): Int {
        return noOfColumn
    }

    fun setNoOfBeads(noOfBeads: Int) {
        setBeads(noOfBeads)
        invalidate()
    }

    private fun setBeads(noOfBeads: Int) {
        this.noOfBeads = noOfBeads
        if (beadDrawables.isNotEmpty()) {
            ExtraHeight = context.resources.getDimension(R.dimen.eight_dp).toInt()
            // size set manually
//            layoutParams.height = beadDrawables[0]!!.minimumHeight * (noOfBeads + 1) + ExtraHeight
            val sizes = resources.getDimension(R.dimen.bead_dimens).toInt()
            layoutParams.height = sizes * (noOfBeads + 1) + ExtraHeight
            layoutParams = layoutParams
        }
    }

    fun setNoOfColumn(noOfColumn: Int) {
        setRow(noOfColumn)
        invalidate()
    }

    private fun setRow(noOfRows: Int) {
        noOfColumn = noOfRows
        if (beadDrawables.isNotEmpty()) {
            // size set manually
//            layoutParams.width = (beadDrawables[0]!!.minimumWidth + colSpacing) * noOfRows
            val theme = AppPreferencesHelper(context, AppConstants.PREF_NAME).getCustomParam(
                AppConstants.Settings.TheamTempView, AppConstants.Settings.theam_Default)
            if (theme.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true)) {
                val sizes = context.resources.getDimension(R.dimen.bead_dimens_width).toInt()
                layoutParams.width = (sizes + colSpacing) * noOfRows
            }else{
                val sizes = resources.getDimension(R.dimen.bead_dimens).toInt()
                layoutParams.width = (sizes + colSpacing) * noOfRows
            }
            layoutParams = layoutParams
        }
    }

    fun setNoOfRowAndBeads(noOfRows_used: Int, noOfRows: Int, noOfBeads: Int, columnSpacing: Int) {
//        defaultState = null;
        this.noOfRows_used = noOfRows_used
        colSpacing = columnSpacing
        setRow(noOfRows)
        setBeads(noOfBeads)


//        postInvalidate();
    }

    private fun doDraw(canvas: Canvas?) {
        try {
//        canvas.drawColor(getResources().getColor(R.color.colorHeadingBlack_00));
//            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
            canvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
            if (engine != null) engine!!.draw(canvas)
        } catch (e: Exception) {
        }
    }

    @Synchronized
    private fun showReadout() {
        if (onBeadShiftListener != null) {
            onBeadShiftListener!!.onBeadShift(this, engine!!.getRowValue())
        }
    }

    fun saveState(state: Bundle) {
        beadState = engine!!.getState()
        state.putSerializable("beadState", beadState)
    }

    fun restoreState(state: Bundle) {
        beadState = state.getSerializable("beadState") as AbacusMasterEngine.BeadState?
        engine!!.setState(beadState!!)
    }

    override fun surfaceChanged(
        holder: SurfaceHolder, format: Int, width: Int,
        height: Int
    ) {
        try {
            if (selectedPositions == null) {
                selectedPositions = ArrayList()
                for (i in 0 until noOfColumn) {
                    selectedPositions!!.add(-1)
                }
            }
            engine = AbacusMasterEngine(
                selectedPositions!!, noOfColumn, noOfBeads, singleBeadValue,
                context, roadDrawable, selectedBeadDrawable, beadDrawables, isBeadStackFromBottom,
                colSpacing, noOfRows_used
            )
            if (beadState != null) {
                engine!!.setState(beadState!!)
            } else if (defaultState == null) {
                /*first time surface changed*/
                defaultState = engine!!.getState()
            }
            showReadout()
            startDrawing(holder)
        } catch (e: Exception) {
        }
    }

    fun setSelectedPositions(
        selectedPositions: ArrayList<Int>?,
        setPositionCompleteListener: AbacusMasterCompleteListener?
    ) {
        synchronized(this) {
            this.selectedPositions = selectedPositions
            if (engine != null) {
                engine!!.setSelectedPositions(selectedPositions!!)
            }
            if (thread != null) {
                thread?.drawOneTime(object : OnSingleDrawingCompletedListener {
                    override fun onSingleDrawingCompleted() {
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed(
                            {
                                defaultState = engine!!.getState()
                                showReadout() //refresh abacus value
                                thread!!.setSleep(true)
                                setPositionCompleteListener?.onSetPositionComplete()
                            }, 10
                        )
                    }
                })
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        setWillNotDraw(false)
        isInitialized = true
    }

    private fun startDrawing(holder: SurfaceHolder) {
        if (thread != null && thread?.isStopDrawing == false) {
            thread?.stopDrawing()
            thread?.interrupt()
        }
        thread = DrawThread(holder)
        thread!!.start()
        thread!!.drawOneTime()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Wait until the thread has really shut down, otherwise it might touch
        // the surface after we return and explode...
        stopDrawing()
    }

    private fun stopDrawing() {
        try {
            val retry = true
            thread!!.stopDrawing()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var downY = -1 //
    private var beadY = -1
    private var prevY = -1
    private var diffTouchAndDrawY = 0
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return try {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downY = event.y.toInt()
                    thread?.setSleep(false)
                    if (motionBead > -1) {
                        mSurfaceHolder?.let {
                            synchronized(it) {
                                val currentY = event.y.toInt()
                                if (prevY != currentY) {
                                    /*if user touch and hold (not move) then do not change position*/
                                    fillTempBeads()
                                    motionRow?.moveBeadTo(motionBead,(event.y - diffTouchAndDrawY).toInt())
                                }
                                prevY = event.y.toInt()
                            }
                        }
                    } else {
                        var x: Int
                        var y: Int

                        /* Process historical events so that beads don't "slip" */
                        /* TODO Try interpolating between such events too... */
                        var i = 0
                        while (i < event.historySize) {
                            x = event.getHistoricalX(i).toInt()
                            y = event.getHistoricalY(i).toInt()
                            motionRow = engine?.getRowAt(x, y)
                            if (motionRow != null) {
                                motionBead = motionRow!!.getBeadAt(x, y)

                                //                            fillTempBeads();
                                if (motionBead > -1) {
                                    beadY = motionRow!!.beads[motionBead]
                                    diffTouchAndDrawY = (event.y - beadY).toInt()
                                    thread?.resumeDrawing()
                                    break
                                }
                            }
                            i++
                        }
                        if (motionBead == -1) {
                            x = event.x.toInt()
                            y = event.y.toInt()
                            motionRow = engine?.getRowAt(x, y)
                            if (motionRow != null) {
                                motionBead = motionRow!!.getBeadAt(x, y)

                                //                            fillTempBeads();
                                if (motionBead > -1) {
                                    beadY = motionRow!!.beads[motionBead]
                                    diffTouchAndDrawY = (event.y - beadY).toInt()
                                    thread?.resumeDrawing()
                                }
                            }
                        }
                    }
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    thread?.setSleep(false)
                    if (motionBead > -1) {
                        mSurfaceHolder?.let {
                            synchronized(it) {
                                val currentY = event.y.toInt()
                                if (prevY != currentY) {
                                    fillTempBeads()
                                    motionRow?.moveBeadTo(motionBead,(event.y - diffTouchAndDrawY).toInt())
                                }
                                prevY = event.y.toInt()
                            }
                        }
                    } else {
                        var x: Int
                        var y: Int
                        var i = 0
                        while (i < event.historySize) {
                            x = event.getHistoricalX(i).toInt()
                            y = event.getHistoricalY(i).toInt()
                            motionRow = engine?.getRowAt(x, y)
                            if (motionRow != null) {
                                motionBead = motionRow!!.getBeadAt(x, y)
                                if (motionBead > -1) {
                                    beadY = motionRow!!.beads[motionBead]
                                    diffTouchAndDrawY = (event.y - beadY).toInt()
                                    thread?.resumeDrawing()
                                    break
                                }
                            }
                            i++
                        }
                        if (motionBead == -1) {
                            x = event.x.toInt()
                            y = event.y.toInt()
                            motionRow = engine?.getRowAt(x, y)
                            if (motionRow != null) {
                                motionBead = motionRow!!.getBeadAt(x, y)
                                if (motionBead > -1) {
                                    beadY = motionRow!!.beads[motionBead]
                                    diffTouchAndDrawY = (event.y - beadY).toInt()
                                    thread?.resumeDrawing()
                                }
                            }
                        }
                    }
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (motionBead > -1) {
                        mSurfaceHolder?.let {
                            synchronized(it) {
                                val upY = event.y.toInt()
                                if (upY < downY) {
                                    /*slide to upword*/
                                    motionRow?.moveBeadTo(
                                        motionBead,
                                        (beadY - (motionRow?.beadHeight?:0) + fireAccumulator).toInt()
                                    )
                                } else {
                                    /*slide to downword*/
                                    motionRow?.moveBeadTo(
                                        motionBead,
                                        (beadY + (motionRow?.beadHeight?:0) + fireAccumulator).toInt()
                                    )
                                }
                            }
                        }
                    }
                    if (motionRow != null) {
                        clearTempBeads()
                    }
                    if (motionBead > -1) {
                        if (isSoundEnabled) {
                            playClickSound(context)
                        }
                        showReadout()
                    }
                    motionRow = null
                    motionBead = -1
                    beadY = -1
                    downY = -1
                    Handler(Looper.getMainLooper()).postDelayed({
                        thread?.setSleep(true)
                        try {
                            thread?.pauseDrawing()
                        } catch (e: InterruptedException) {
                        }
                    }, 3)
                    true
                }
                else -> super.onTouchEvent(event)
            }
        } catch (e: Exception) {
            thread?.setSleep(true)
            try {
                thread?.pauseDrawing()
            } catch (e1: InterruptedException) {
            }
            super.onTouchEvent(event)
        }
    }

    /**
     * fill temp beads for selected row
     */
    private fun fillTempBeads() {
        if (motionRow != null && (motionRow?.tempBeads == null || motionRow?.tempBeads?.size == 0)) {
            if (motionRow?.beads?.size != 0){
                motionRow?.tempBeads = motionRow?.beads?.size?.let { IntArray(it) }
                for (i in 0 until motionRow!!.beads.size) {
                    motionRow?.tempBeads?.set(i, motionRow!!.beads[i])
                }
            }

        }
    }

    private fun clearTempBeads() {
        if (motionRow != null) {
            motionRow?.tempBeads = null
        }
    }

    fun reset() {
        if (defaultState != null) {
            if (isSoundEnabled) {
                playResetSound(context)
            }
            thread?.setSleep(false)
            engine?.setState(defaultState!!, true, object :
                AbacusMasterEngine.OnStateResetCompletedListener {
                override fun onStateResetCompleted() {
                    thread?.setSleep(true)
                    showReadout()
                }
            })
            beadState = defaultState
        }
    }

    fun resetAndSetNextRow() {
        if (defaultState != null) {
            if (isSoundEnabled) {
                playResetSound(context)
            }
            thread?.setSleep(false)
            engine?.setState(defaultState!!, true, object :
                AbacusMasterEngine.OnStateResetCompletedListener {
                override fun onStateResetCompleted() {
                    thread?.setSleep(true)
                    showReadout()
                }
            })
            beadState = defaultState
        }
    }

    fun quickReset() {
        if (defaultState != null) {
            thread?.setSleep(false)
            engine?.setState(defaultState!!, false, object :
                AbacusMasterEngine.OnStateResetCompletedListener {
                override fun onStateResetCompleted() {
                    thread?.setSleep(true)
                    showReadout()
                }
            })
            beadState = defaultState
        }
    }

    fun stop() {
        if (thread != null) {
            thread?.setSleep(true)
        }
    }

    companion object {
        var ExtraHeight = 0
    }

    init {
        setBackgroundColor(Color.TRANSPARENT)
        setZOrderOnTop(true) //necessary
        val array = context.obtainStyledAttributes(attrs, R.styleable.AbacusView)
        val id = array.getResourceId(R.styleable.AbacusView_beadDrawables, 0)
        roadDrawable = array.getDrawable(R.styleable.AbacusView_roadDrawable)
        selectedBeadDrawable = array.getDrawable(R.styleable.AbacusView_selectedBeadDrawable)
        val theme = AppPreferencesHelper(context,AppConstants.PREF_NAME).getCustomParam(AppConstants.Settings.TheamTempView, AppConstants.Settings.theam_Default)
        if (theme.equals(AppConstants.Settings.theam_Star, ignoreCase = true)) {
            selectedBeadDrawable = ContextCompat.getDrawable(context, R.drawable.star_gray_open)
        } else if (theme.equals(AppConstants.Settings.theam_eyes, ignoreCase = true)) {
            selectedBeadDrawable = ContextCompat.getDrawable(context, R.drawable.face_gray_open)
        } else if (theme.equals(AppConstants.Settings.theam_shape, ignoreCase = true)) {
            selectedBeadDrawable = ContextCompat.getDrawable(context, R.drawable.shape_triangle_gray)
            roadDrawable = ContextCompat.getDrawable(context, R.drawable.road_black)
            roadDrawable?.setTint(ContextCompat.getColor(context, R.color.black2))
        } else if (theme.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true)) {
            selectedBeadDrawable = ContextCompat.getDrawable(context, R.drawable.poligon_gray)
            roadDrawable = ContextCompat.getDrawable(context, R.drawable.road_black)
            roadDrawable?.setTint(ContextCompat.getColor(context, R.color.black2))
        } else if (theme.equals(AppConstants.Settings.theam_Egg, ignoreCase = true)) {
            selectedBeadDrawable = ContextCompat.getDrawable(context, R.drawable.egg)
        }
        noOfColumn = array.getInt(R.styleable.AbacusView_noOfRows, 1)
        noOfBeads = array.getInt(R.styleable.AbacusView_noOfBead, 4)
        singleBeadValue = array.getInt(R.styleable.AbacusView_singleBeadValue, 1)
        isBeadStackFromBottom = array.getBoolean(R.styleable.AbacusView_isBeadStackFromBottom, false)
        colSpacing = array.getDimensionPixelSize(R.styleable.AbacusView_columnSpacing, 10)
        if (id != 0) {
            val imgs = resources.obtainTypedArray(id)
            beadDrawables = arrayOfNulls(imgs.length())
            for (i in 0 until imgs.length()) {
                beadDrawables[i] = imgs.getDrawable(i)
            }
            imgs.recycle()
        } else {
            beadDrawables = arrayOfNulls(1)
            if (theme.equals(AppConstants.Settings.theam_eyes, ignoreCase = true)) {
                beadDrawables[0] = ContextCompat.getDrawable(context, R.drawable.star_red_close)
            } else if (theme.equals(AppConstants.Settings.theam_Poligon, ignoreCase = true)) {
                beadDrawables[0] = ContextCompat.getDrawable(context, R.drawable.poligon_gray)
            } else if (theme.equals(AppConstants.Settings.theam_eyes, ignoreCase = true)) {
                beadDrawables[0] = ContextCompat.getDrawable(context, R.drawable.face_red_close)
            } else if (theme.equals(AppConstants.Settings.theam_shape, ignoreCase = true)) {
                beadDrawables[0] = ContextCompat.getDrawable(context, R.drawable.shape_square_gray)
            } else if (theme.equals(AppConstants.Settings.theam_Egg, ignoreCase = true)) {
                beadDrawables[0] = ContextCompat.getDrawable(context, R.drawable.egg0)
            }
        }
        array.recycle()

        // Register interest in changes to the surface
        val holder = holder
        holder.setFormat(PixelFormat.TRANSPARENT)
        holder.addCallback(this)
    }
}