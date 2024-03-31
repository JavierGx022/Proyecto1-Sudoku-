package com.example.sodokuprueba.Logica

class SodokuSolver {
    fun generateValidBoard(): Array<Array<Int>> {
        val board = Array(9) { IntArray(9) }
        solveSudoku(board)
        return board.map { it.toTypedArray() }.toTypedArray()
    }

    fun solveSudoku(board: Array<IntArray>): Boolean {
        return solveSudokuHelper(board)
    }

    private fun solveSudokuHelper(board: Array<IntArray>): Boolean {
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                if (board[row][col] == 0) {
                    for (num in 1..9) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num
                            if (solveSudokuHelper(board)) {
                                return true
                            }
                            board[row][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isValid(board: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        for (i in 0 until 9) {
            if (board[row][i] == num || board[i][col] == num || board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == num) {
                return false
            }
        }
        return true
    }
}
