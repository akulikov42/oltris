package org.neakmobi.oltris.commands

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.view.FigureView
import org.neakmobi.oltris.states.Game

class SwapFigure(
        val game: Game,
        val i0: Int,
        val j0: Int,
        val i1: Int,
        val j1: Int) : ContinuousAction(0.13f) {

    private var firstFigureView: FigureView = game.fieldView.getFigure(i0, j0)
    private var secondFigureView: FigureView = game.fieldView.getFigure(i1, j1)

    private var dx = 0f
    private var dy = 0f
    private var x0 = 0f
    private var y0 = 0f
    private var x1 = 0f
    private var y1 = 0f

    private var badSwap = false

    init {

        Controller.lockAll()

        dx = firstFigureView.x - secondFigureView.x
        dy = firstFigureView.y - secondFigureView.y

        x0 = firstFigureView.x;  y0 = firstFigureView.y
        x1 = secondFigureView.x; y1 = secondFigureView.y

        if(firstFigureView.blockInput || !firstFigureView.visible || !firstFigureView.showCost)
            badSwap = true
        if(secondFigureView.blockInput || !secondFigureView.visible || !secondFigureView.showCost)
            badSwap = true

    }

    override fun finish() {

        Controller.unlockAll()

        firstFigureView.setBounds(x0, y0)
        secondFigureView.setBounds(x1, y1)

        if(!badSwap)
            game.puzzle.swapFigure(i0 + game.puzzle.currentField.solvedLines, j0, i1 + game.puzzle.currentField.solvedLines, j1)

        game.updateView()

    }

    override fun update(dt: Float): Boolean {

        if(badSwap) {
            finish()
            return true
        }

        nowMoment += dt

        firstFigureView.setBounds(x0 - dx * progress, y0 - dy * progress)
        secondFigureView.setBounds(x1 + dx * progress, y1 + dy * progress)

        if(progress >= 1f) {
            finish()
            return true
        } else {
            return false
        }
    }

}