package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.neakmobi.oltris.renderer.Drawable

open class Button(
        textureRegion: TextureRegion?,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        var color: Color = Color.WHITE
): View(), Drawable {

    protected val sprite = Sprite()
    protected val clickedColor = Color(color.r, color.g, color.b, color.a / 3f)

    open var available = true
        set(value) {
            field = value
            sprite.color = if(!field) {
                Color(color.r, color.g, color.b, color.a / 3f)
            } else {
                color
            }
        }

    protected open var isClicked = false
        set(value) {
            field = value
            if(!field) {
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
                        clickedColor.a)
            }
        }

    init {
        setBounds(x, y, w, h)
        isClicked = false
        if(textureRegion != null)
            sprite.setRegion(textureRegion)
    }

    open fun push() {
        if(!available)
            return
        isClicked = true
    }

    open fun release(changeState: Boolean = true) {
        if(!available)
            return
        isClicked = false
    }

    override fun drawObj(batch: Batch?) {
        sprite.draw(batch)
    }

    override fun updateBounds() {
        sprite.setBounds(xScreen, yScreen, wScreen, hScreen)
    }
}