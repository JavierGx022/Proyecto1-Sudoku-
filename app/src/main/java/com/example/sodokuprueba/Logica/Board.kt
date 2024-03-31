package com.example.sodokuprueba.Logica

class Board(val size:Int, val cells:List<Cell>) {
    fun getCell(row:Int, colum:Int) = cells[row * size + colum]
}