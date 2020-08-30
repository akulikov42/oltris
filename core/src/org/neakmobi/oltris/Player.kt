package org.neakmobi.oltris

import com.badlogic.gdx.Gdx
import org.neakmobi.oltris.model.Puzzle
import org.neakmobi.oltris.model.PuzzleBuilder
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.states.StateManager
import java.nio.ByteBuffer

object Player {

    var templateBelow = true
    private val filename = "settings"
    private val file = Gdx.files.local(filename)

    lateinit var puzzle: Puzzle

    fun load() {

        puzzle = Puzzle()

        if(!file.exists()) {
            templateBelow = true
            val puzzleBuilder = PuzzleBuilder()
            puzzleBuilder.genPuzzle(1)
            puzzleBuilder.setPuzzle(puzzle)
        } else {
            val arr = file.readBytes()
            val buffer = ByteBuffer.wrap(arr)
            puzzle.load(buffer)
            templateBelow = buffer.int == 1
        }

    }

    fun save() {

        val buffer = ByteBuffer.allocate(Game.FIELD_SIZE * Game.FIELD_SIZE * 4 * 4 * 8)

        puzzle.save(buffer)
        buffer.putInt(if(templateBelow) 1 else 0)

        val arr = buffer.array()

        file.writeBytes(arr, false)

    }

    fun reset() {
        templateBelow = true
        puzzle = Puzzle()
        val puzzleBuilder = PuzzleBuilder()
        puzzleBuilder.genPuzzle(1)
        puzzleBuilder.setPuzzle(puzzle)
        StateManager.currentState.updateView()
    }

}