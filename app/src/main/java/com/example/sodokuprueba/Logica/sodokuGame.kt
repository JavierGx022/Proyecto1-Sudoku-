package com.example.sodokuprueba.Logica

import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class SodokuGame {
    var selectedCellData = MutableLiveData<Pair<Int, Int>>()
    var cellLiveData = MutableLiveData<List<Cell>>()

    private var rowSelected = -1
    private var columnSelected = -1
    private lateinit var board: Board

    init {
        generateBoard()
        selectedCellData.postValue(Pair(rowSelected, columnSelected))
        cellLiveData.postValue(board.cells)
    }

    private fun generateBoard() {
        val cells = mutableListOf<Cell>()

        // Generar un tablero de Sudoku válido
        val solver = SodokuSolver()
        val validBoard = solver.generateValidBoard()

        // Convertir el tablero de 2D a 1D
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val modifiable = validBoard[i][j] == 0 // Determinar si la celda es modificable o no
                cells.add(Cell(i, j, validBoard[i][j], modifiable))
            }
        }

        // Establecer el número de celdas a eliminar del tablero
        val numberOfCellsToRemove = 2 // Por ejemplo, puedes ajustar este número según lo desees

        // Eliminar algunos números para crear el tablero inicial del juego
        val random = Random
        var cellsRemoved = 0

        while (cellsRemoved < numberOfCellsToRemove) {
            val row = random.nextInt(9)
            val column = random.nextInt(9)

            if (cells[row * 9 + column].value != null) {
                cells[row * 9 + column].value = null
                cellsRemoved++
            }
        }

        board = Board(9, cells)
    }

    fun Input(number: Int) {
        if (rowSelected == -1 || columnSelected == -1) return

        board.getCell(rowSelected, columnSelected).value = number
        cellLiveData.postValue(board.cells)
    }

    fun updateSelected(row: Int, column: Int) {
        rowSelected = row
        columnSelected = column
        selectedCellData.postValue(Pair(row, column))
    }

    fun isSolutionCorrect(): Boolean {
        // Obtener la matriz de enteros del tablero actual
        val currentBoard = Array(9) { row ->
            IntArray(9) { col ->
                val cellValue = board.getCell(row, col).value
                cellValue ?: 0 // Si la celda está vacía, asignamos 0
            }
        }

        val solver = SodokuSolver()

        // Generar la solución real del tablero
        val solution = solver.generateValidBoard()

        // Verificar si la solución del usuario coincide con la solución real
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val userCellValue = currentBoard[i][j]
                val solutionCellValue = solution[i][j]

                if (userCellValue != solutionCellValue) {
                    return false
                }
            }
        }
        return true
    }





}
