package org.neakmobi.oltris.model

import com.badlogic.gdx.files.FileHandle
import java.nio.ByteBuffer

/**
 *
 * Модель хода
 *
 */

class Move(var i0: Int = 0, var j0: Int = 0, var i1: Int = 0, var j1: Int = 0) {

    fun save(buffer: ByteBuffer) {
        buffer.putInt(i0)
        buffer.putInt(j0)
        buffer.putInt(i1)
        buffer.putInt(j1)
    }

    fun load(buffer: ByteBuffer) {
        i0 = buffer.int
        j0 = buffer.int
        i1 = buffer.int
        j1 = buffer.int
    }

}