package org.neakmobi.oltris.commands.tutorial

import org.neakmobi.oltris.commands.SwapFigure
import org.neakmobi.oltris.commands.basic.ActionManager
import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.FigureFieldHandler
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.view.FigureView
import org.neakmobi.oltris.view.ImageView
import org.neakmobi.oltris.view.PuzzleView
import org.neakmobi.oltris.view.TextView

class ClickTutorial(val game: Game, val puzzleView: PuzzleView, val figureFieldHandler: FigureFieldHandler): ContinuousAction(10f) {

    val hand = ImageView(0f, 0f, 0f, 0f, Renderer.getRegion("main_atlas", "hand"))
    val hint = TextView(0f, 0f, "CLICK", 0.75f)

    var firstFigure = FigureView(0f, 0f, 0f, 0f)
    var secondFigure = FigureView(0f, 0f, 0f, 0f)

    var i0 = 0
    var j0 = 2
    var i1 = 1
    var j1 = 2

    var firstFigureSelected = false

    var ready = false

    fun setUp() {
        if(ready) return

        figureFieldHandler.blockClick = true

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

        firstFigure = puzzleView.field.getFigure(i0, j0)
        secondFigure = puzzleView.field.getFigure(i1, j1)
        firstFigure.blockInput = false
        secondFigure.blockInput = false
        firstFigure.setAlpha(1f)

        hand.setBounds(
                firstFigure.x + firstFigure.w / 4,
                firstFigure.y - firstFigure.h / 2,
                firstFigure.w,
                firstFigure.w
        )
        hint.setBounds(4.5f, 8.5f)

        Renderer.subscribe(hand)
        Renderer.subscribe(hint)

        ready = true
    }

    fun nextFig() {
        firstFigureSelected = true
        hand.setBounds(
                secondFigure.x + secondFigure.w / 4,
                secondFigure.y - secondFigure.h / 2,
                secondFigure.w,
                secondFigure.w
        )
        firstFigure.selection = FigureView.TypeSelection.SELECTED
        secondFigure.selection = FigureView.TypeSelection.GOOD_MOVE
        firstFigure.setAlpha(1f)
        secondFigure.setAlpha(1f)
    }

    fun swapFig() {
        ActionManager.addAction(SwapFigure(game, i0, j0, i1, j1))
        firstFigure.selection = FigureView.TypeSelection.NO_SELECTED
        secondFigure.selection = FigureView.TypeSelection.NO_SELECTED
    }

    override fun update(dt: Float): Boolean {
        setUp()
        if(figureFieldHandler.eventType == FigureFieldHandler.EventType.SELECT_1_FIG) {
            if(!firstFigureSelected) {
                if (puzzleView.field.getFigure(figureFieldHandler.i0, figureFieldHandler.j0) == firstFigure) {
                    nextFig()
                }
                figureFieldHandler.haveEvents = false
                figureFieldHandler.firstFigureSelected = false
                return false
            }

            if (puzzleView.field.getFigure(figureFieldHandler.i0, figureFieldHandler.j0) != secondFigure) {
                figureFieldHandler.haveEvents = false
                figureFieldHandler.firstFigureSelected = false
                return false
            }

            swapFig()
            finish()
            return true

        }

        figureFieldHandler.haveEvents = false
        return false
    }

    override fun finish() {

        game.undoBtn.available = true
        game.restartBtn.available = true

        figureFieldHandler.blockClick = false

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

        firstFigure.selection = FigureView.TypeSelection.NO_SELECTED
        secondFigure.selection = FigureView.TypeSelection.NO_SELECTED

        Renderer.unsubscribe(hint)
        Renderer.unsubscribe(hand)
        ready = false
    }

}