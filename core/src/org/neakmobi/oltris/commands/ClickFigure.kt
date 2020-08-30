package org.neakmobi.oltris.commands

import org.neakmobi.oltris.commands.basic.Action
import org.neakmobi.oltris.commands.basic.ActionManager
import org.neakmobi.oltris.controller.FigureFieldHandler
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.view.FigureView

class ClickFigure(val game: Game): Action {

    var selectedFigure: FigureView
    var neighborFigures = mutableListOf<FigureView>()

    var firstFigureIdx = 0
    var firstFigureJdx = 0
    var secondFigureIdx = 0
    var secondFigureJdx = 0

    var upFigure: FigureView? = null
    var downFigure: FigureView? = null
    var leftFigure: FigureView? = null
    var rightFigure: FigureView? = null

    var badClick = false

    init {

        firstFigureIdx = game.figureFieldHandler.i0
        firstFigureJdx = game.figureFieldHandler.j0

        selectedFigure = game.fieldView.getFigure(firstFigureIdx, firstFigureJdx)

        if(!selectedFigure.showCost || !selectedFigure.visible || selectedFigure.blockInput)
            badClick = true

        if(!badClick) {

            if (firstFigureIdx - 1 >= 0)
                neighborFigures.add(game.fieldView.getFigure(firstFigureIdx - 1, firstFigureJdx))
            if (firstFigureIdx + 1 < Game.FIELD_SIZE)
                neighborFigures.add(game.fieldView.getFigure(firstFigureIdx + 1, firstFigureJdx))
            if (firstFigureJdx - 1 >= 0)
                neighborFigures.add(game.fieldView.getFigure(firstFigureIdx, firstFigureJdx - 1))
            if (firstFigureJdx + 1 < Game.FIELD_SIZE)
                neighborFigures.add(game.fieldView.getFigure(firstFigureIdx, firstFigureJdx + 1))

            selectedFigure.selection = FigureView.TypeSelection.SELECTED

            for (figure in neighborFigures) {
                if (figure.showCost)
                    figure.selection = FigureView.TypeSelection.GOOD_MOVE
                else
                    figure.selection = FigureView.TypeSelection.BAD_MOVE
            }
        }

    }

    override fun update(dt: Float): Boolean {

        if(badClick) {
            finish()
            return true
        }

        if(game.figureFieldHandler.haveEvents) {
            if(game.figureFieldHandler.eventType == FigureFieldHandler.EventType.SELECT_2_FIG) {
                    secondFigureIdx = game.figureFieldHandler.i0
                    secondFigureJdx = game.figureFieldHandler.j0
                    if(game.fieldView.getFigure(secondFigureIdx, secondFigureJdx).selection == FigureView.TypeSelection.GOOD_MOVE)
                        ActionManager.addAction(SwapFigure(game, firstFigureIdx, firstFigureJdx, secondFigureIdx, secondFigureJdx))
            }
            game.figureFieldHandler.haveEvents = false
            finish()
            return true
        }

        return false

    }

    override fun finish() {
        selectedFigure.selection = FigureView.TypeSelection.NO_SELECTED
        for(figure in neighborFigures)
            figure.selection = FigureView.TypeSelection.NO_SELECTED
    }

}