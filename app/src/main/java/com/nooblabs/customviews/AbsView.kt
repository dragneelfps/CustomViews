package com.nooblabs.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min

abstract class AbsView : View {

    constructor(context: Context): super(context)

    constructor(context: Context, attributeSet: AttributeSet): this(context,attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int): super(context,attributeSet,defStyle)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = getImprovedWidth(widthMeasureSpec)
        val h = getImprovedHeight(heightMeasureSpec)
        Log.d("debug","Width: $w Height: $h")
        setMeasuredDimension(w,h)
    }

    private fun getImprovedWidth(widthMeasureSpec: Int): Int {
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        return when(MeasureSpec.getMode(widthMeasureSpec)){
            MeasureSpec.UNSPECIFIED -> {
                hGetMaxWidth()
            }
            MeasureSpec.EXACTLY -> {
                specSize
            }
            MeasureSpec.AT_MOST -> {
                hGetMinWidth()
            }
            else -> {
                hGetMinWidth()
            }
        }
    }

    private fun getImprovedHeight(heightMeasureSpec: Int): Int {
        val specSize = MeasureSpec.getSize(heightMeasureSpec)
        return when(MeasureSpec.getMode(heightMeasureSpec)){
            MeasureSpec.UNSPECIFIED -> {
                hGetMaxHeight()
            }
            MeasureSpec.EXACTLY -> {
                specSize
            }
            MeasureSpec.AT_MOST -> {
                hGetMinHeight()
            }
            else -> {
                hGetMinHeight()
            }
        }
    }

    abstract fun hGetMinWidth(): Int

    abstract fun hGetMinHeight(): Int

    abstract fun hGetMaxWidth(): Int

    abstract fun hGetMaxHeight(): Int


    private fun getImprovedDimen(measureSpec: Int, atMostSize: Int): Int {
        val specSize = MeasureSpec.getSize(measureSpec)
        Log.d("debug","SpecSize: $specSize, minSize: $atMostSize")
        val result = when(MeasureSpec.getMode(measureSpec)){
            MeasureSpec.UNSPECIFIED ->{
                specSize
            }
            MeasureSpec.EXACTLY -> {
                //For any exact size
                specSize
                //For no exact size less than min
//                max(specSize, atMostSize)
            }
            MeasureSpec.AT_MOST -> {
                min(specSize, atMostSize)
            }
            else -> specSize
        }
//        Log.d("debug","Result size was: $result")
        return result
    }
}