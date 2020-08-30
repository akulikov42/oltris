package org.neakmobi.oltris.commands.tutorial

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.FigureFieldHandler
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.view.FigureView
import org.neakmobi.oltris.view.ImageView
import org.neakmobi.oltris.view.PuzzleView
import org.neakmobi.oltris.view.TextView

class SwapTutorial(val puzzleView: PuzzleView, val figureFieldHandler: FigureFieldHandler): ContinuousAction(10f) {

    val hand = ImageView(0f, 0f, 0f, 0f, Renderer.getRegion("main_atlas", "hand"))
    val hint = TextView(0f, 0f, "SWAP", 0.75f)


    var handStartX = 0f
    var handStartY = 0f
    var handEndX = 0f
    var handEndY = 0f
    var handSize = 0f
    var nowHandAnimProgress = 0f
        set(value) { field = if(value > 1f) value - 1f else value }

    lateinit var firstFigure: FigureView
    lateinit var secondFigure: FigureView

    var ready = false

    fun setUp() {
        if(ready) return
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

        figureFieldHandler.blockClick = true
        firstFigure = puzzleView.field.getFigure(0, 2)
        secondFigure = puzzleView.field.getFigure(1, 2)

        firstFigure.blockInput = false
        firstFigure.setAlpha(1f)
        secondFigure.blockInput = false
        secondFigure.setAlpha(1f)

        handStartX = firstFigure.x + firstFigure.w / 4
        handStartY = firstFigure.y - firstFigure.h / 2
        handEndX = secondFigure.x + secondFigure.w / 4
        handEndY = secondFigure.y - secondFigure.h / 4
        handSize = firstFigure.w
        hint.setBounds(4.5f, 8.5f)
        hand.setBounds(handStartX, handStartY, handSize, handSize)
        Renderer.subscribe(hand)
        Renderer.subscribe(hint)
        ready = true
    }

    override fun update(dt: Float): Boolean {
        setUp()
        if(figureFieldHandler.eventType == FigureFieldHandler.EventType.SWAP_FIGURE) {
            finish()
            return true
        }
        nowHandAnimProgress += dt / 2f
        hand.setBounds(
                handStartX + nowHandAnimProgress * (handEndX - handStartX),
                handStartY + nowHandAnimProgress * (handEndY - handStartY)
        )
        return false
    }

    override fun finish() {
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
        Renderer.unsubscribe(hint)
        Renderer.unsubscribe(hand)
        ready = false
    }
}