package org.neakmobi.oltris.commands.tutorial

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.ClickScreenHandler
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.view.TextView

class UndoTutorial(val game: Game): ContinuousAction(3f) {

    val handler = ClickScreenHandler()

    val mainHint  = TextView(0.5f, game.fieldView.y + game.fieldView.h - 1.5f, "YOU CAN UNDO", 0.4f, 1f, 8f)
    val resetHint = TextView(0.3f, game.restartBtn.y + game.restartBtn.h + 0.1f, "EVERYTHING", 0.3f)
    val undoHint  = TextView(6f, game.undoBtn.y + game.undoBtn.h + 0.1f,"LAST MOVE", 0.3f)
    var ready = false

    fun setUp() {
        if(ready) return

        game.undoBtn.available = true
        game.restartBtn.available = true

        Controller.lockAll()
        Controller.resetHandler()
        Controller.addHandler(handler)

        for(idxLine in 0 until Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                game.fieldView.figureViews[idxLine][idxFigure].setAlpha(0.3f)
            }
        }
        for(idxLine in 0 until Game.TEMPLATE_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                game.templateView.getLine(idxLine)[idxFigure].setAlpha(0.3f)
            }
        }
        Renderer.subscribe(mainHint)
        Renderer.subscribe(resetHint)
        Renderer.subscribe(undoHint)
        ready = true
    }

    override fun update(dt: Float): Boolean {
        setUp()
        nowMoment += dt
        if(handler.haveEvents) {
            finish()
            return true
        }
        return false
    }

    override fun finish() {

        Controller.removeHandler(handler)
        Controller.unlockAll()

        ready = false
        Renderer.unsubscribe(mainHint)
        Renderer.unsubscribe(resetHint)
        Renderer.unsubscribe(undoHint)
    }

}