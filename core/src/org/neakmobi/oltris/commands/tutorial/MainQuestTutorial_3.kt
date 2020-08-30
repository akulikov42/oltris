package org.neakmobi.oltris.commands.tutorial

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.ClickScreenHandler
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.view.ImageView
import org.neakmobi.oltris.view.PuzzleView
import org.neakmobi.oltris.view.TextView

class MainQuestTutorial_3(val game: Game, val puzzleView: PuzzleView): ContinuousAction(10f) {

    val handler = ClickScreenHandler()

    val hint_1 = TextView(0.5f, 13f, "MOVE THIS BLOCK", 0.40f, 1f, 8f)
    val hint_2 = TextView(0.5f, 12.35f, "2 SQUARES LEFT", 0.40f, 1f, 8f)
    val hint_3 = TextView(0.5f, 11.70f, "TO SOLVE THE LINE", 0.40f, 1f, 8f)
    var arrow = ImageView(0f, 0f, 0f, 0f)

    var ready = false

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

        for(idxLine in 0 until Game.TEMPLATE_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                puzzleView.template.getLine(idxLine)[idxFigure].setAlpha(0.3f)
            }
        }

        puzzleView.field.getFigure(0, 0).setAlpha(1f)
        puzzleView.field.getFigure(0, 1).setAlpha(1f)
        puzzleView.field.getFigure(0, 2).setAlpha(1f)
        puzzleView.template.getLine(Game.TEMPLATE_SIZE - 1)[0].setAlpha(1f)
        puzzleView.template.getLine(Game.TEMPLATE_SIZE - 1)[1].setAlpha(1f)
        puzzleView.template.getLine(Game.TEMPLATE_SIZE - 1)[2].setAlpha(1f)

        arrow = ImageView(
                puzzleView.field.getFigure(0, 0).x,
                puzzleView.field.getFigure(0, 0).y + puzzleView.field.getFigure(0, 0).h,
                puzzleView.field.getFigure(0, 0).w * 3,
                puzzleView.field.getFigure(0, 0).h * 1.5f,
                Renderer.getRegion("main_atlas", "tutorial_arrow")
        )
        arrow.setColor(1f, 1f, 0f, 1f)

        Renderer.subscribe(hint_1)
        Renderer.subscribe(hint_2)
        Renderer.subscribe(hint_3)
        Renderer.subscribe(arrow)

        ready = true
    }

    override fun update(dt: Float): Boolean {
        setUp()

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

        game.undoBtn.available = true
        game.restartBtn.available = true

        for(idxLine in 0 until Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                puzzleView.field.figureViews[idxLine][idxFigure].blockInput = false
                puzzleView.field.figureViews[idxLine][idxFigure].setAlpha(1f)
            }
        }

        for(idxLine in 0 until Game.TEMPLATE_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                puzzleView.template.getLine(idxLine)[idxFigure].setAlpha(1f)
            }
        }

        Renderer.unsubscribe(hint_1)
        Renderer.unsubscribe(hint_2)
        Renderer.unsubscribe(hint_3)
        Renderer.unsubscribe(arrow)
    }



}