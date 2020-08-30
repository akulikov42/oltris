package org.neakmobi.oltris.commands.tutorial

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.ClickScreenHandler
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.view.PuzzleView
import org.neakmobi.oltris.view.TextView

class MainQuestTutorial_1(val game: Game, val puzzleView: PuzzleView): ContinuousAction(10f) {

    val handler = ClickScreenHandler()

    val hint_1 = TextView(0.5f, 13f, "THESE LINES", 0.4f, 1f, 8f)
    val hint_2 = TextView(0.5f, 12.35f, "MUST BE", 0.4f, 1f, 8f)
    val hint_3 = TextView(0.5f, 11.70f, "THE SAME", 0.4f, 1f, 8f)

    var period = 3f
    var moment = 0f
        set(value) {
            field = if(value > period) {
                value - period
            } else {
                value
            }
        }

    var ready = false
    var currLine = -1
    var nextLine = 0

    fun setUp() {
        if(ready) return

        Controller.lockAll()
        Controller.resetHandler()
        Controller.addHandler(handler)

        game.undoBtn.available = false
        game.restartBtn.available = false

        for(idxLine in 0 until Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                puzzleView.field.figureViews[idxLine][idxFigure].blockInput = true
                puzzleView.field.figureViews[idxLine][idxFigure].setAlpha(0.3f)
            }
        }
        Renderer.subscribe(hint_1)
        Renderer.subscribe(hint_2)
        Renderer.subscribe(hint_3)
        ready = true
    }

    fun setAlpha() {

        nextLine = when(moment) {
            in 0f..1f -> { 0 }
            in 1f..2f -> { 1 }
            else      -> { 2 }
        }

        if(currLine == nextLine)
            return

        currLine = nextLine

        for(idxLine in 0 until  Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                if(idxLine == currLine)
                    puzzleView.field.getFigure(currLine, idxFigure).setAlpha(1f)
                else
                    puzzleView.field.getFigure(idxLine, idxFigure).setAlpha(0.3f)
            }
        }

        for(idxLine in 0 until Game.TEMPLATE_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                if(idxLine == 2 - currLine)
                    puzzleView.template.getLine(idxLine)[idxFigure].setAlpha(1f)
                else
                    puzzleView.template.getLine(idxLine)[idxFigure].setAlpha(0.3f)
            }
        }

    }

    override fun update(dt: Float): Boolean {
        setUp()
        moment += dt * 1.5f
        setAlpha()

        if(handler.haveEvents) {
            finish()
            return true
        }

        return false
    }

    override fun finish() {

        ready = false

        Controller.removeHandler(handler)
        Controller.unlockAll()

        for(idxLine in 0 until Game.FIELD_SIZE)
            for(idxFigure in 0 until Game.FIELD_SIZE)
                puzzleView.field.figureViews[idxLine][idxFigure].blockInput = false


        Renderer.unsubscribe(hint_1)
        Renderer.unsubscribe(hint_2)
        Renderer.unsubscribe(hint_3)
    }

}