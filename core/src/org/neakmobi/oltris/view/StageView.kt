package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import org.neakmobi.oltris.model.Puzzle
import org.neakmobi.oltris.renderer.Drawable

open class StageView(x: Float, y: Float, w: Float, h: Float, var preamble: String = "LEVEL:"): View(), Drawable {

    protected open val current = TextView(x, y,preamble + "0", h, 1f, w)

    init {
        setBounds(x, y, w, h)
    }

    open fun set(value: Int) {
        current.setString(preamble + "$value")
    }

    open fun set(str: String) {
        current.setString(preamble + str)
    }

    open fun set(str: String, colors: Array<Color>) {
        current.setString("{dollar}{dollar}{dollar} BIG BOSS {dollar}{dollar}{dollar}", colors)
    }

    override fun drawObj(batch: Batch?) {
        current.drawObj(batch)
    }

    override fun updateBounds() {
        current.setBounds(x, y)
    }

}