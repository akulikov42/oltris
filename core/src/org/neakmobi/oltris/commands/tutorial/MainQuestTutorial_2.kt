package org.neakmobi.oltris.commands.tutorial

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.ClickScreenHandler
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.view.PuzzleView
import org.neakmobi.oltris.view.TextView

class MainQuestTutorial_2(val game: Game, val puzzleView: PuzzleView): ContinuousAction(10f) {

    val handler = ClickScreenHandler()

    val hint_1 = TextView(0.5f, 13f, "THESE BLOCKS", 0.4f, 1f, 8f)
    val hint_2 = TextView(0.5f, 12.35f, "ARE IN", 0.4f, 1f, 8f)
    val hint_3 = TextView(0.5f, 11.70f, "PLACE", 0.4f, 1f, 8f)

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

        puzzleView.field.getFigure(0, 3).setAlpha(1f)
        puzzleView.field.getFigure(0, 4).setAlpha(1f)
        puzzleView.field.getFigure(0, 5).setAlpha(1f)
        puzzleView.template.getLine(Game.TEMPLATE_SIZE - 1)[3].setAlpha(1f)
        puzzleView.template.getLine(Game.TEMPLATE_SIZE - 1)[4].setAlpha(1f)
        puzzleView.template.getLine(Game.TEMPLATE_SIZE - 1)[5].setAlpha(1f)

        Renderer.subscribe(hint_1)
        Renderer.subscribe(hint_2)
        Renderer.subscribe(hint_3)
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

        for(idxLine in 0 until Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                puzzleView.field.figureViews[idxLine][idxFigure].blockInput = false
                //puzzleView.field.figureViews[idxLine][idxFigure].setAlpha(1f)
            }
        }

        Renderer.unsubscribe(hint_1)
        Renderer.unsubscribe(hint_2)
        Renderer.unsubscribe(hint_3)
    }


}