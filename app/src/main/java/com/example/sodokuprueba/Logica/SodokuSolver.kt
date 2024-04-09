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

    fun isValidSolution(board: Array<IntArray>): Boolean {
        // Verificar filas y columnas
        for (i in 0 until 9) {
            val rowSet = HashSet<Int>()
            val colSet = HashSet<Int>()

            for (j in 0 until 9) {
                if (board[i][j] !in 1..9 || board[j][i] !in 1..9 || rowSet.contains(board[i][j]) || colSet.contains(board[j][i])) {
                    return false
                }

                rowSet.add(board[i][j])
                colSet.add(board[j][i])
            }
        }

        // Verificar subcuadros de 3x3
        for (i in 0 until 9 step 3) {
            for (j in 0 until 9 step 3) {
                val subSquareSet = HashSet<Int>()

                for (row in i until i + 3) {
                    for (col in j until j + 3) {
                        if (board[row][col] !in 1..9 || subSquareSet.contains(board[row][col])) {
                            return false
                        }

                        subSquareSet.add(board[row][col])
                    }
                }
            }
        }

        return true
    }
}
