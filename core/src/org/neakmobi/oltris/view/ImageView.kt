package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.neakmobi.oltris.renderer.Drawable

class ImageView(x: Float, y: Float, w: Float, h: Float, textureRegion: TextureRegion? = null): View(), Drawable {

    private val sprite = Sprite()

    init {
        if(textureRegion != null)
            sprite.setRegion(textureRegion)
        setBounds(x, y, w, h)
    }

    fun setImage(textureRegion: TextureRegion?) {
        sprite.setRegion(textureRegion)
    }

    fun setColor(r: Float, g: Float, b: Float, a: Float) {
        sprite.color = Color(r, g, b, a)
    }

    override fun updateBounds() {
        sprite.setBounds(xScreen, yScreen, wScreen, hScreen)
    }

    override fun drawObj(batch: Batch?) {
        sprite.draw(batch)
    }
}