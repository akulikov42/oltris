package org.neakmobi.oltris.model

import org.neakmobi.oltris.states.Game
import java.nio.ByteBuffer
import java.util.*


/**
 *
 * Модель головоломки.
 *
 * @property solveField     Поле решений. На него опирается проверка совпадения линии.
 *
 * @property currentField    Поле головоломки.
 *
 *
 */
class Puzzle {

    var stage = 0
        set(value) {
            field = if(value < 99999) value else 99999
        }

    lateinit var puzzleField        : Field
    lateinit var solveField         : Field
             var currentField       = Field()
             var userMoves          = Stack<Move>()

    val events = LinkedList<PuzzleEvent>()

    val currentLine: Int
        get() = currentField.solvedLines



    fun set(stage: Int, solveField: Field, puzzleField: Field) {

        this.stage          = stage
        this.solveField     = solveField
        this.puzzleField    = puzzleField

        currentField = Field()
        this.currentField.copy(puzzleField)

        userMoves.clear()
        events.clear()

    }



    fun swapFigure(i0: Int, j0: Int, i1: Int, j1: Int): Boolean {

        if(!currentField.swapFigure(i0, j0, i1, j1))
            return false

        userMoves.push(Move(i0, j0, i1, j1))
        checkLine()

        return true
    }


    fun restart() {

        currentField.copy(puzzleField)
        userMoves.clear()
        events.clear()

    }


    fun nextLine() {

        currentField.solveLine()
        userMoves.push(Move(-1, -1, -1, -1))

        if(!checkStage()) {
            checkLine()
        }

    }



    fun undo(): Boolean {

        if(userMoves.empty())
            return false

        var lastMove = userMoves.pop()
        while(lastMove.i0 == -1) {
            currentField.undoSolveLine()
            if(userMoves.isEmpty())
                return true
            lastMove = userMoves.pop()
        }

        currentField.swapFigure(lastMove.i0, lastMove.j0, lastMove.i1, lastMove.j1, true)

        return true
    }



    private fun checkLine(): Boolean {

        for(idx in 0 until Game.FIELD_SIZE) {
            if( currentField.figure(currentLine, idx).moves != 0 || solveField.figure(currentLine, idx).type != currentField.figure(currentLine, idx).type)
                return false
        }

        events.add(PuzzleEvent.LINE_COMPLETED)
        return true
    }



    private fun checkStage(): Boolean {

        if(currentLine < Game.FIELD_SIZE)
            return false

        events.add(PuzzleEvent.STAGE_COMPLETED)

        return true

    }


    private fun puzzleSolved(): Boolean {

        for(idxLine in 0 until Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                if(solveField.figure(idxLine, idxFigure).type == currentField.figure(idxLine, idxFigure).type && currentField.figure(idxLine, idxFigure).moves == 0)
                    continue
                return false
            }
        }

        return true

    }

    fun save(buffer: ByteBuffer) {
        if(puzzleSolved()) {
            val puzzleBuilder = PuzzleBuilder()
            puzzleBuilder.genPuzzle(stage + 1)
            puzzleBuilder.setPuzzle(this)
        }

        solveField.save(buffer)
        puzzleField.save(buffer)
        currentField.save(buffer)
        buffer.putInt(stage)

        var cntMoves = 0
        val userMovesForSave = Stack<Move>()
        while(!userMoves.empty()) {
            userMovesForSave.push(userMoves.pop())
            cntMoves++
        }

        buffer.putInt(cntMoves)
        for(idx in 0 until cntMoves)
            userMovesForSave.pop().save(buffer)

    }

    fun load(buffer: ByteBuffer) {

        solveField   = Field();  solveField.load(buffer)
        puzzleField  = Field();  puzzleField.load(buffer)
        currentField = Field();  currentField.load(buffer)
        stage        = 100000//buffer.int

        val cntMoves = buffer.int
        var tmpMove  : Move

        for(idx in 0 until cntMoves) {
            tmpMove = Move()
            tmpMove.load(buffer)
            userMoves.push(tmpMove)
        }

    }

}