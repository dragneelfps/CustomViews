package com.nooblabs.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.Log

import android.view.View
import kotlin.math.min

class TestView: AbsView, View.OnClickListener {

    var radius = 75
    @ColorInt var backGroundColor = resources.getColor(android.R.color.holo_green_light)
    @ColorInt var circleColor = resources.getColor(android.R.color.holo_red_light)

    init {
        minimumWidth = radius*2
        minimumHeight = radius*2
        setOnClickListener(this)
        isSaveEnabled = true
        Log.d("debug",this.toString())
    }

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): this(context,attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context,attributeSet,defStyle){
        val t = context.obtainStyledAttributes(attributeSet,
                R.styleable.TestView,
                defStyle,
                0)
        Log.d("debug","OLD Back: $backGroundColor circle: $circleColor")
        backGroundColor = t.getColor(R.styleable.TestView_backGroundColor, backGroundColor)
        circleColor = t.getColor(R.styleable.TestView_circleColor, circleColor)
        val name = t.getString(R.styleable.TestView_name)
        Log.d("debug","Name : $name")
        Log.d("debug","Back: $backGroundColor circle: $circleColor")
        t.recycle()
        initCircle()
    }

    fun initCircle(){
        minimumWidth = radius*2
        minimumHeight = radius*2
    }

    override fun hGetMinWidth(): Int {
        return suggestedMinimumWidth
    }

    override fun hGetMinHeight(): Int {
        return suggestedMinimumHeight
    }

    override fun hGetMaxWidth(): Int {
        return suggestedMinimumWidth
    }

    override fun hGetMaxHeight(): Int {
        return suggestedMinimumHeight
    }

    fun redraw(){
        requestLayout()
        invalidate()
    }

    override fun onClick(v: View?) {
        radius = (radius * 1.2).toInt()
        redraw()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let { canvas ->
            Log.d("debug","canvas: ${canvas.width} x ${canvas.height}")
            val w = canvas.width
            val h = canvas.height

            canvas.drawRect(Rect(0,0,w,h), getBrush(backGroundColor))

            val ox = w/2
            val oy = h/2
            val radius = min(min(ox,this.radius), min(oy,this.radius))
            canvas.drawCircle(ox.toFloat(), oy.toFloat(), radius.toFloat(), getBrush(circleColor))
        }
    }

    private fun getBrush(@ColorInt color: Int): Paint? {
        val p = Paint()
        p.color = color
        return p
    }




    /**
     * Saving state of view with BaseSavedState Pattern
     */

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(): Parcelable {
        Log.d("debug","onSave Called")
        return onSaveInstanceStateStandard()
    }

    @SuppressLint("MissingSuperCall")
    override fun onRestoreInstanceState(state: Parcelable?) {
        onRestoreInstanceStateStandard(state)
    }

    companion object {
        class SavedState : BaseSavedState {
            var defRadius: Int = 0

            constructor(superState: Parcelable) : super(superState)
            constructor(p: Parcel?) : super(p){
                defRadius = p?.readInt() ?: 0
            }

            override fun writeToParcel(out: Parcel?, flags: Int) {
                super.writeToParcel(out, flags)
                out?.writeInt(defRadius)
            }

            override fun toString(): String {
                return "TestView: defRadius = $defRadius"
            }
            companion object {
                val CREATOR = object : Parcelable.Creator<SavedState> {
                    override fun createFromParcel(source: Parcel?): SavedState {
                        return SavedState(source)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls<SavedState?>(size)
                    }
                }
            }
        }
    }

    fun onSaveInstanceStateStandard(): Parcelable{
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.defRadius = radius
        return ss
    }

    fun onRestoreInstanceStateStandard(source: Parcelable?){
        if(source !is SavedState){
            super.onRestoreInstanceState(source)
            return
        }
        super.onRestoreInstanceState(source.superState)
        radius = source.defRadius
        initCircle()
        redraw()
    }

}