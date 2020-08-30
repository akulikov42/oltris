package org.neakmobi.oltris

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.renderer.ScreenManager
import org.neakmobi.oltris.states.*
import org.neakmobi.oltris.utils.TextureAtlas
import org.neakmobi.oltris.Player

class GameScreen: Screen {

    private var batch = SpriteBatch()

    var width:  Int = Gdx.app.graphics.width
    var height: Int = Gdx.app.graphics.height

    override fun show() {
        Player.load()

        ScreenManager.init(width.toFloat(), height.toFloat())
        Renderer.init(batch)
        Renderer.addAtlas("main_atlas", TextureAtlas("game_texture.png", "game_atlas.xml"))

        StateManager.addState(States.GAME, Game())
        StateManager.addState(States.MAIN_MENU, MainMenu())
        //StateManager.addState(States.TUTORIAL, Tutorial())
        StateManager.changeState(States.MAIN_MENU)

        Gdx.input.inputProcessor = Controller

        Renderer.setClearColor(Color(0.42f, 0.78f, 1f, 1f))
        //Renderer.setClearColor(Color(0f, 150/255f, 187/255f, 1f))

    }

    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        StateManager.update(delta)
        Renderer.draw()

    }

    override fun hide() {}

    override fun pause() {
        StateManager.pause()
    }

    override fun resume() {
        StateManager.resume()
    }

    override fun resize(width: Int, height: Int) {
        Controller.screenWidth = width.toFloat()
        Controller.screenHeight = height.toFloat()
        Renderer.resize(width, height)
        StateManager.resize(width, height)
    }

    override fun dispose() {
        batch.dispose()
    }

}