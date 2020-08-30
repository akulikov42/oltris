package org.neakmobi.oltris.model

import java.nio.ByteBuffer

class Figure(idxLine: Int = 0, idxFigure: Int = 0) {

    var type:   Int = 0
    var moves:  Int = 0
        private set(value) { field = if (value > 0) value else 0 }

    var line:   Int = idxLine
    var column: Int = idxFigure

    val haveMoves: Boolean
        get() = moves > 0

    fun incMove() { moves++ }
    fun decMove() { moves-- }

    fun reset() {
        type = 0
        moves = 0
    }

    fun copy(src: Figure) {
        this.type   = src.type
        this.moves  = src.moves
    }

    fun save(buffer: ByteBuffer) {
        buffer.putInt(type)
        buffer.putInt(moves)
        buffer.putInt(line)
        buffer.putInt(column)
    }

    fun load(buffer: ByteBuffer) {
        type   = buffer.int
        moves  = buffer.int
        line   = buffer.int
        column = buffer.int
    }

}