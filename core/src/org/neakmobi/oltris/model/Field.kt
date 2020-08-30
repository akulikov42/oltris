package org.neakmobi.oltris.model

import org.neakmobi.oltris.states.Game
import java.nio.ByteBuffer

class Field {

    private val figures = Array(Game.FIELD_SIZE) { i -> Array(Game.FIELD_SIZE) { j -> Figure(i, j) } }

    var solvedLines = 0

    fun figure(i: Int, j: Int) : Figure = figures[i][j]

    /**
     *
     * Обмен двух фигур
     * @return true - обмен произведен, false - ошибка обмена (недостаточно ходов, например)
     *
     */
    fun swapFigure(i0: Int, j0: Int, i1: Int, j1: Int, increaseMove: Boolean = false): Boolean {

        if(figures[i0][j0].moves < 1 && figures[i1][j1].moves < 1 && !increaseMove)
            return false

        if(i0 < 0 || i1 < 0 || j0 < 0 || j1 < 0 || i0 >= Game.FIELD_SIZE || i1 >= Game.FIELD_SIZE || j0 >= Game.FIELD_SIZE || j1 >= Game.FIELD_SIZE)
            return false

        var tmpInt: Int        = figures[i0][j0].line
        figures[i0][j0].line   = figures[i1][j1].line
        figures[i1][j1].line   = tmpInt
        tmpInt                 = figures[i0][j0].column
        figures[i0][j0].column = figures[i1][j1].column
        figures[i1][j1].column = tmpInt

        val tmpFigure: Figure = figures[i0][j0]
        figures[i0][j0]       = figures[i1][j1]
        figures[i1][j1]       = tmpFigure

        if(increaseMove) {
            figures[i0][j0].incMove()
            figures[i1][j1].incMove()
        } else {
            figures[i0][j0].decMove()
            figures[i1][j1].decMove()
        }

        return true
    }

    fun solveLine() {
        solvedLines++
    }

    fun undoSolveLine() {
        solvedLines--
    }

    fun copy(src: Field) {
        for(idxLine in figures.indices) {
            for(idxFigure in figures[0].indices) {
                figures[idxLine][idxFigure].copy(src.figures[idxLine][idxFigure])
            }
        }
        solvedLines = src.solvedLines
    }

    fun save(buffer: ByteBuffer) {
        buffer.putInt(solvedLines)
        for(idxLine in 0 until Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                figures[idxLine][idxFigure].save(buffer)
            }
        }
    }

    fun load(buffer: ByteBuffer) {
        solvedLines = buffer.int
        for(idxLine in 0 until Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                figures[idxLine][idxFigure].load(buffer)
            }
        }
    }

}