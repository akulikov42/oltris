package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch

class TextButton(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        str: String
): Button(null, x, y, w, h, Color.BLACK) {

    private val text = TextView(x, y, str, h, 1f, w)

    override var available = true
        set(value) {
            field = value
            text.setColor(if(!field) color else Color(color.r, color.g, color.b, color.a / 3f))
        }

    override var isClicked = false
        set(value) {
            field = value
            text?.setColor(if(!field) color else clickedColor)
        }

    override fun drawObj(batch: Batch?) {
        text.drawObj(batch)
    }

    override fun updateBounds() {
        text?.setBounds(xScreen, yScreen, wScreen, hScreen)
    }

}