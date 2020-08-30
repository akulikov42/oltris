package org.neakmobi.oltris.commands

import org.neakmobi.oltris.Player
import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.states.Game

class NextStage(val game: Game): ContinuousAction(0.35f) {

    val showCost = Array(Game.FIELD_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> game.fieldView.getFigure(idx, jdx).showCost } }
    val fieldFiguresX = Array(Game.FIELD_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> game.fieldView.getFigure(idx, jdx).xScreen } }
    val fieldFiguresY = Array(Game.FIELD_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> game.fieldView.getFigure(idx, jdx).yScreen } }
    val fieldFiguresW = game.fieldView.getFigure(0, 0).wScreen
    val fieldFiguresH = game.fieldView.getFigure(0, 0).hScreen

    val templateFiguresX = Array(Game.TEMPLATE_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> game.templateView.figures[idx][jdx].xScreen } }
    val templateFiguresY = Array(Game.TEMPLATE_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> game.templateView.figures[idx][jdx].yScreen } }
    val templateFiguresW = game.templateView.figures[0][0].wScreen
    val templateFiguresH = game.templateView.figures[0][0].hScreen

    init {

        var nextStage = game.puzzle.stage + 1
        if(nextStage > 99999)
            nextStage = 99999

        game.puzzleBuilder.genPuzzle(game.puzzle.stage + 1)
        game.puzzleBuilder.setPuzzle(game.puzzle)

        game.updateView()

        for(idx in 0 until Game.FIELD_SIZE) {
            for(jdx in 0 until Game.FIELD_SIZE) {
                game.fieldView.getFigure(idx, jdx).xScreen = fieldFiguresX[idx][jdx] + fieldFiguresW / 2f
                game.fieldView.getFigure(idx, jdx).yScreen = fieldFiguresY[idx][jdx] + fieldFiguresH / 2f
                game.fieldView.getFigure(idx, jdx).setScale(0f)
                showCost[idx][jdx] = game.fieldView.getFigure(idx, jdx).showCost
            }
        }

        for(idx in 0 until Game.TEMPLATE_SIZE) {
            for(jdx in 0 until Game.FIELD_SIZE) {
                game.templateView.figures[idx][jdx].xScreen = templateFiguresX[idx][jdx] + templateFiguresW / 2f
                game.templateView.figures[idx][jdx].yScreen = templateFiguresY[idx][jdx] + templateFiguresH / 2f
                game.templateView.figures[idx][jdx].setScale(0f)
            }
        }
    }

    override fun update(dt: Float): Boolean {

        nowMoment += dt

        for(idx in 0 until Game.FIELD_SIZE) {
            for(jdx in 0 until Game.FIELD_SIZE) {
                game.fieldView.getFigure(idx, jdx).xScreen = fieldFiguresX[idx][jdx] + (1f - progress) * fieldFiguresW / 2f
                game.fieldView.getFigure(idx, jdx).yScreen = fieldFiguresY[idx][jdx] + (1f - progress) * fieldFiguresH / 2f
                game.fieldView.getFigure(idx, jdx).setScale(progress)
                game.fieldView.getFigure(idx, jdx).showCost = false
            }
        }

        for(idx in 0 until Game.TEMPLATE_SIZE) {
            for(jdx in 0 until Game.FIELD_SIZE) {
                game.templateView.figures[idx][jdx].xScreen = templateFiguresX[idx][jdx] + (1f - progress) * templateFiguresW / 2f
                game.templateView.figures[idx][jdx].yScreen = templateFiguresY[idx][jdx] + (1f - progress) * templateFiguresH / 2f
                game.templateView.figures[idx][jdx].setScale(progress)
            }
        }

        if(progress >= 1f) {
            finish()
            return true
        }

        return false
    }

    override fun finish() {
        for(idx in 0 until Game.FIELD_SIZE) {
            for(jdx in 0 until Game.FIELD_SIZE) {
                game.fieldView.getFigure(idx, jdx).xScreen = fieldFiguresX[idx][jdx]
                game.fieldView.getFigure(idx, jdx).yScreen = fieldFiguresY[idx][jdx]
                game.fieldView.getFigure(idx, jdx).setScale(1f)
                game.fieldView.getFigure(idx, jdx).showCost = showCost[idx][jdx]
            }
        }

        for(idx in 0 until Game.TEMPLATE_SIZE) {
            for(jdx in 0 until Game.FIELD_SIZE) {
                game.templateView.figures[idx][jdx].xScreen = templateFiguresX[idx][jdx]
                game.templateView.figures[idx][jdx].yScreen = templateFiguresY[idx][jdx]
                game.templateView.figures[idx][jdx].setScale(1f)
            }
        }
        Player.save()
    }

}