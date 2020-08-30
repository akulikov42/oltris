package org.neakmobi.oltris.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.ScreenViewport
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.utils.TextureAtlas

object Renderer {

    private var camera = OrthographicCamera(0f, 0f)
    private var viewport = ScreenViewport(camera)

    fun setClearColor(c: Color) {
        Gdx.gl.glClearColor(c.r, c.g, c.b, c.a)
    }

    private val initialized: Boolean get() = (batch != null)

    private val forRender = mutableListOf<Drawable>()
    private val atlases = mutableMapOf<String, TextureAtlas>()
    private var batch: SpriteBatch? = null

    fun init(batch: SpriteBatch) {
        viewport.setScreenPosition(0, 0)
        viewport.setScreenSize(ScreenManager.screenRect.width.toInt(), ScreenManager.screenRect.height.toInt())
        Renderer.batch = batch
    }

    fun addAtlas(nameAtlas: String, atlas: TextureAtlas) {
        atlases[nameAtlas] = atlas
    }

    fun getAtlas(nameAtlas: String): TextureAtlas? {
        return atlases[nameAtlas]
    }

    fun getRegion(nameAtlas: String, nameRegion: String): TextureRegion? {
        return atlases[nameAtlas]?.getRegion(nameRegion)
    }

    fun subscribe(d: Drawable) { forRender.add(d) }

    fun unsubscribe(d: Drawable) { forRender.remove(d) }

    fun clear() { forRender.clear() }

    fun resize(width: Int, height: Int) {
        ScreenManager.update(width.toFloat(), height.toFloat())
        viewport.update(width, height, false)
        camera.position.set(width / 2f, height / 2f, 0f)
        batch!!.projectionMatrix = camera.combined
    }

    fun draw() {
        if(!initialized)
            return
        batch!!.begin()
        for(d in forRender) {
            d.drawObj(batch)
        }
        batch!!.end()
    }



}