package com.example.sodokuprueba.Logica

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.graphics.Paint
import android.graphics.Color
import android.view.MotionEvent
import android.graphics.Rect

class SodokuBoard(context: Context, attributeSet: AttributeSet): View(context, attributeSet){


    private var sqrtSize=3
    private var size= 9
    private var cellSizePixels= 0F

    private var rowSelected=0
    private var columSelected=0

    private var listener: SodokuBoard.onTouchListener?=null
    private var cells: List<Cell>?= null

    private val thickLinePaint= Paint().apply {
        color= Color.BLACK
        style= Paint.Style.STROKE
        strokeWidth=4F
    }

    private val thinLine= Paint().apply {
        color= Color.BLACK
        style= Paint.Style.STROKE
        strokeWidth=2F
    }

    private val selectedCell= Paint().apply {
        color= Color.parseColor("#6ead3a")
        style= Paint.Style.FILL_AND_STROKE
    }
    private val conflicCell= Paint().apply {
        color= Color.parseColor("#efedef")
        style= Paint.Style.FILL_AND_STROKE
    }

    private val text= Paint().apply {
        style=Paint.Style.FILL_AND_STROKE
        color= Color.BLACK
        textSize= 50F
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels= (width/size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
        drawText(canvas)
    }

    private fun fillCells(canvas:Canvas){


        cells?.forEach{
            val r= it.row
            val c= it.colum
            if(r==rowSelected && c==columSelected){
                fillCell(canvas, r,c,selectedCell)
            }else if(r==rowSelected || c==columSelected){
                fillCell(canvas, r, c, conflicCell)
            }else if(r/sqrtSize==rowSelected/sqrtSize && c/sqrtSize==columSelected/sqrtSize){
                fillCell(canvas, r,c, conflicCell)
            }
        }
    }


    private fun fillCell(canvas:Canvas, r: Int, c:Int, paint: Paint ){
        canvas.drawRect(c*cellSizePixels, r*cellSizePixels, (c+1)* cellSizePixels, (r+1)*cellSizePixels,paint)
    }
    private fun drawLines(canvas:Canvas){
        canvas.drawRect(0F,0F,width.toFloat(),height.toFloat(),thickLinePaint)

        for(i in 1 until size){
            val painToUse= when (i%sqrtSize){
               0-> thickLinePaint
                else -> thinLine
            }
          canvas.drawLine(i*cellSizePixels, 0F, i*cellSizePixels, height.toFloat(),painToUse )
          canvas.drawLine(0F,i*cellSizePixels, width.toFloat(),i*cellSizePixels,painToUse )
        }


    }

    private fun drawText(canvas:Canvas){

        cells?.forEach{
            val row= it.row
            val colum= it.colum
            val valueString= it.value?.toString() ?: ""
            val textBound= Rect()
            text.getTextBounds(valueString, 0, valueString.length,textBound )
            val textWidth= text.measureText(valueString)
            val textHeight= textBound.height()
            canvas.drawText(valueString, (colum *cellSizePixels) + cellSizePixels /2 - textWidth/2,
                (row*cellSizePixels)+cellSizePixels/2 - textHeight/2 , text
                )

        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels= Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels,sizePixels)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action){
            MotionEvent.ACTION_DOWN->{
                touchEvent(event.x, event.y)
                true
            }else-> false
        }
    }

    private fun touchEvent(x:Float, y:Float){

        val possRowSelected= (y/cellSizePixels).toInt()
        val possColumSelected=(x/cellSizePixels).toInt()

        listener?.onCellTouched(possRowSelected,possColumSelected)
    }

     fun updateSelectedCellUI(row: Int, colum: Int){
        rowSelected=row
        columSelected=colum
        invalidate()
    }
     fun registerListener(listener: SodokuBoard.onTouchListener){
         this.listener= listener
     }
    fun updateCells(cells:List<Cell>){
        this.cells= cells
        invalidate()
    }

    interface onTouchListener{
        fun onCellTouched(row:Int, colum:Int)
    }

    fun validateInput(row: Int, column: Int, input: Int): Boolean {
        // Verificar si el número ingresado es válido en la fila, columna y cuadrícula de 3x3
        return isNumberValidInRow(row, input) && isNumberValidInColumn(column, input) && isNumberValidInGrid(row, column, input)
    }

    private fun isNumberValidInRow(row: Int, input: Int): Boolean {
        // Verificar si el número ingresado ya está presente en la fila
        for (i in 0 until size) {
            if (cells!![row * size + i].value == input) {
                return false
            }
        }
        return true
    }

    private fun isNumberValidInColumn(column: Int, input: Int): Boolean {
        // Verificar si el número ingresado ya está presente en la columna
        for (i in 0 until size) {
            if (cells!![i * size + column].value == input) {
                return false
            }
        }
        return true
    }

    private fun isNumberValidInGrid(row: Int, column: Int, input: Int): Boolean {
        // Obtener la posición de inicio de la cuadrícula de 3x3
        val startRow = (row / sqrtSize) * sqrtSize
        val startColumn = (column / sqrtSize) * sqrtSize

        // Verificar si el número ingresado ya está presente en la cuadrícula de 3x3
        for (i in startRow until startRow + sqrtSize) {
            for (j in startColumn until startColumn + sqrtSize) {
                if (cells!![i * size + j].value == input) {
                    return false
                }
            }
        }
        return true
    }

}