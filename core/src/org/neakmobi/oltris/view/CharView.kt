package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.neakmobi.oltris.renderer.Drawable

class CharView: View(), Drawable {

    private var sprite = Sprite()
    var isEmpty = true

    fun setColor(r: Float, g: Float, b: Float, a: Float) {
        sprite.setColor(r, g, b, a)
    }

    fun setRegion(region: TextureRegion?) {
        sprite.setRegion(region)
    }

    fun setScale(scale: Float) {
        sprite.setScale(scale)
    }

    override fun drawObj(batch: Batch?) {
        if(isEmpty) return
        sprite.draw(batch)
    }

    override fun updateBounds() {
        sprite.setBounds(xScreen, yScreen, wScreen, hScreen)
    }

}