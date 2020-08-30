package org.neakmobi.oltris.commands

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.renderer.ScreenManager
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.states.StateManager
import org.neakmobi.oltris.states.States
import org.neakmobi.oltris.view.Button
import org.neakmobi.oltris.view.PuzzleView
import org.neakmobi.oltris.view.StageView

class GameClose(
        val puzzleView: PuzzleView,
        val playerView: StageView,
        val undoBtn: Button,
        val resetBtn: Button,
        val nextState: States
): ContinuousAction(0.6f) {


    val figMod = 0.5f

    val backgroundX = puzzleView.background.xScreen

    val showCost = Array(Game.FIELD_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> puzzleView.field.getFigure(idx, jdx).showCost } }
    val figuresX = Array(Game.FIELD_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> puzzleView.field.getFigure(idx, jdx).xScreen } }
    val figuresY = Array(Game.FIELD_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> puzzleView.field.getFigure(idx, jdx).yScreen } }

    val figureW = puzzleView.field.getFigure(0, 0).wScreen
    val figureH = puzzleView.field.getFigure(0, 0).hScreen

    val undoBtnX = undoBtn.xScreen
    val resetBtnX = resetBtn.xScreen
    val stageY = playerView.yScreen

    val templateX = Array(Game.TEMPLATE_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> puzzleView.template.figures[idx][jdx].xScreen } }
    val templateY = Array(Game.TEMPLATE_SIZE) { idx -> Array(Game.FIELD_SIZE) { jdx -> puzzleView.template.figures[idx][jdx].yScreen } }
    val templateW = puzzleView.template.figures[0][0].wScreen
    val templateH = puzzleView.template.figures[0][0].hScreen

    val uiProgress: Float
        get() {
            if(progress > figMod)
                return (progress - figMod) * (1f / (1f - figMod))
            return 0f
        }


    val figProgress: Float
        get() {
            if(progress < figMod)
                return progress * (1f / figMod)
            return 1f
        }

    val figDuration: Float = 0.5f

    val cntFieldFig = Game.FIELD_SIZE * Game.FIELD_SIZE
    val fieldFigDelay: Float = figDuration / cntFieldFig

    val cntTemplateFig = Game.TEMPLATE_SIZE * Game.FIELD_SIZE
    val templateFigDelay: Float = figDuration / cntTemplateFig

    init {
        Controller.lockAll()
    }

    fun fieldFigProgress(numFig: Int): Float {

        if(figProgress < fieldFigDelay * numFig) return 0f
        if(figProgress > fieldFigDelay * numFig + figDuration) return 1f

        return (figProgress - fieldFigDelay * numFig) / figDuration

    }

    fun templateFigProgress(numFig: Int): Float {
        if(figProgress < templateFigDelay * numFig) return 0f
        if(figProgress > templateFigDelay * numFig + figDuration) return 1f

        return (figProgress - templateFigDelay * numFig) / figDuration
    }

    override fun update(dt: Float): Boolean {

        nowMoment += dt

        puzzleView.background.xScreen = backgroundX + (ScreenManager.screenRect.width * 1.2f - backgroundX) * uiProgress
        resetBtn.xScreen = resetBtnX - (resetBtn.wScreen * 1.2f + resetBtnX) * uiProgress
        undoBtn.xScreen = undoBtnX + (ScreenManager.screenRect.width * 1.2f - undoBtnX) * uiProgress
        playerView.yScreen = stageY + (ScreenManager.screenRect.height * 1.2f - stageY) * uiProgress

        if(figProgress > 0f) {

            for(idx in 0 until Game.FIELD_SIZE) {
                for(jdx in 0 until Game.FIELD_SIZE) {
                    puzzleView.field.getFigure(idx, jdx).xScreen = figuresX[idx][jdx] + (fieldFigProgress(idx * Game.FIELD_SIZE + jdx)) * figureW / 2f
                    puzzleView.field.getFigure(idx, jdx).yScreen = figuresY[idx][jdx] + (fieldFigProgress(idx * Game.FIELD_SIZE + jdx)) * figureH / 2f
                    puzzleView.field.getFigure(idx, jdx).setScale(1f - fieldFigProgress(idx * Game.FIELD_SIZE + jdx))
                    puzzleView.field.getFigure(idx, jdx).showCost = false
                }
            }

            for(idx in 0 until Game.TEMPLATE_SIZE) {
                for(jdx in 0 until Game.FIELD_SIZE) {
                    puzzleView.template.figures[idx][jdx].xScreen = templateX[idx][jdx] + (templateFigProgress(idx * Game.TEMPLATE_SIZE + jdx)) * templateW / 2f
                    puzzleView.template.figures[idx][jdx].yScreen = templateY[idx][jdx] + (templateFigProgress(idx * Game.TEMPLATE_SIZE + jdx)) * templateH / 2f
                    puzzleView.template.figures[idx][jdx].setScale(1f - templateFigProgress(idx * Game.TEMPLATE_SIZE + jdx))
                }
            }

        }

        if(progress >= 1f) {
            finish()
            return true
        }

        return false
    }

    override fun finish() {

        Controller.unlockAll()

        puzzleView.background.xScreen = backgroundX
        resetBtn.xScreen = resetBtnX
        undoBtn.xScreen = undoBtnX
        playerView.yScreen = stageY
        for(idx in 0 until Game.FIELD_SIZE) {
            for(jdx in 0 until Game.FIELD_SIZE) {
                puzzleView.field.getFigure(idx, jdx).xScreen = figuresX[idx][jdx]
                puzzleView.field.getFigure(idx, jdx).yScreen = figuresY[idx][jdx]
                puzzleView.field.getFigure(idx, jdx).setScale(1f)
                puzzleView.field.getFigure(idx, jdx).showCost = showCost[idx][jdx]
            }
        }

        for(idx in 0 until Game.TEMPLATE_SIZE) {
            for(jdx in 0 until Game.FIELD_SIZE) {
                puzzleView.template.figures[idx][jdx].xScreen = templateX[idx][jdx]
                puzzleView.template.figures[idx][jdx].yScreen = templateY[idx][jdx]
                puzzleView.template.figures[idx][jdx].setScale(1f)
            }
        }

        StateManager.changeState(nextState)
    }

}