package org.neakmobi.oltris.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import org.neakmobi.oltris.Player
import org.neakmobi.oltris.commands.ConfirmReset
import org.neakmobi.oltris.commands.MainMenuClose
import org.neakmobi.oltris.commands.basic.ActionManager
import org.neakmobi.oltris.commands.MainMenuShow
import org.neakmobi.oltris.controller.ButtonHandler
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.view.*

class MainMenu: State() {

    private val logo        = ImageView(0f, 11.8f, 9f, 2f)
    private val stage       = StageView(0.5f, 10.5f, 8f, 0.75f, "LEVEL:")
    private val league      = LeagueView(0.5f, 2f, 8f, 1.5f)

    val playBtn             = Button(
            Renderer.getRegion("main_atlas", "play_button"),
            2.5f,
            7f,
            4f,
            2.3f
    )
    val resetBtn            = Button(
            Renderer.getRegion("main_atlas", "reset_button"),
            1f,
            5f,
            2.5f,
            1.5f
    )
    val changeTemplateBtn   = RadioButton(
            Renderer.getRegion("main_atlas", "template_above"),
            Renderer.getRegion("main_atlas", "template_below"),
            5.5f,
            5f,
            2.5f,
            1.5f,
            true
    )

    val buttonHandler = ButtonHandler()

    init {
        logo.setImage(Renderer.getRegion("main_atlas", "logo"))
    }

    override fun start() {
        Controller.addHandler(buttonHandler)
        buttonHandler.addButton(playBtn)
        buttonHandler.addButton(changeTemplateBtn)
        buttonHandler.addButton(resetBtn)
        Gdx.input.setCatchKey(Input.Keys.BACK, true)

        Renderer.subscribe(logo)
        Renderer.subscribe(playBtn)
        Renderer.subscribe(changeTemplateBtn)
        Renderer.subscribe(resetBtn)
        Renderer.subscribe(stage)
        Renderer.subscribe(league)
        updateView()

        ActionManager.addAction(MainMenuShow(arrayOf(playBtn, resetBtn, changeTemplateBtn), stage, league, logo))

    }

    override fun finish() {
        Controller.removeAll()
        Renderer.clear()
    }

    override fun pause() {
        Player.save()
    }

    override fun resume() {
        Player.load()
        updateView()
    }

    override fun update(dt: Float) {

        ActionManager.update(dt)

        if(buttonHandler.haveEvents) {
            for(clicked in buttonHandler.clicked) {

                when (clicked) {
                    playBtn -> {
                        ActionManager.addAction(MainMenuClose(arrayOf(playBtn, resetBtn, changeTemplateBtn), stage, league, logo, States.GAME))
                    }

                    ButtonHandler.BACK_BUTTON -> {
                        Gdx.app.exit()
                    }

                    ButtonHandler.BACKSPACE_BUTTON -> {
                        Gdx.app.exit()
                    }

                    changeTemplateBtn -> {
                        Player.templateBelow = !Player.templateBelow
                    }

                    resetBtn -> {
                        ActionManager.addAction(ConfirmReset(this))
                    }

                }

            }

            buttonHandler.clicked.clear()
        }
    }

    override fun updateView() {
        stage.set(Player.puzzle.stage)
        league.set(Player.puzzle.stage)
        changeTemplateBtn.isOn = !Player.templateBelow
    }

    override fun resize(width: Int, height: Int) {
        stage.setBounds()
        logo.setBounds()
        playBtn.setBounds()
        changeTemplateBtn.setBounds()
        resetBtn.setBounds()
        league.setBounds()
        updateView()
    }

}