package com.simpleshopping

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView

class NotepadItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f * context.resources.displayMetrics.density
        color = resolveThemeColor(context, R.attr.notepadLineColor, 0xAABCCCDC.toInt())
    }

    private val marginPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.5f * context.resources.displayMetrics.density
        color = resolveThemeColor(context, R.attr.notepadMarginColor, 0xAACC6666.toInt())
    }

    private val marginX: Float = 40f * context.resources.displayMetrics.density

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft.toFloat()
        val right = (parent.width - parent.paddingRight).toFloat()

        // Draw horizontal lines at the bottom of each child
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val y = (child.bottom + params.bottomMargin).toFloat()
            canvas.drawLine(left, y, right, y, linePaint)
        }

        // Draw red margin line
        val top = parent.paddingTop.toFloat()
        val bottom = (parent.height - parent.paddingBottom).toFloat()
        canvas.drawLine(marginX, top, marginX, bottom, marginPaint)
    }

    private fun resolveThemeColor(context: Context, attr: Int, fallback: Int): Int {
        val typedValue = TypedValue()
        return if (context.theme.resolveAttribute(attr, typedValue, true)) {
            typedValue.data
        } else {
            fallback
        }
    }
}
