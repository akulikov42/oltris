package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion

class RadioButton(
        val onTexture: TextureRegion?,
        val offTexture: TextureRegion?,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        on: Boolean = false,
        color: Color = Color.WHITE
): Button(onTexture, x, y, w, h, color) {

    var isOn = on
        set(value) {
            field = value
            if(field) {
                sprite.setRegion(onTexture)
            } else {
                sprite.setRegion(offTexture)
            }
        }

    override var isClicked = false
        get() = super.isClicked
        set(value) {
            field = value
            if (!field) {
                sprite.setColor(
                        color.r,
                        color.g,
                        color.b,
                        color.a)
            } else {
                sprite.setColor(
                        clickedColor.r,
                        clickedColor.g,
                        clickedColor.b,
                        if(isOn) clickedColor.a else clickedColor.a * 2f)
            }
        }

    init {
        sprite.setRegion(if(isOn) onTexture else offTexture)
    }

    override fun release(changeState: Boolean) {
        super.release(changeState)
        if(changeState)
            changeState()
    }

    private fun changeState() {
        isOn = !isOn
    }

}