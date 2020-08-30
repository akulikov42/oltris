package org.neakmobi.oltris.commands

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.states.Game
import org.neakmobi.oltris.view.FigureView

class DeleteLine(val game: Game): ContinuousAction(0.40f) {

    private val lineDeleteDurationL  = 0.0f
    private val lineDeleteDurationR  = 0.4f
    private var lineDeleteComplete  = false

    private val fieldFallDurationL   = 0.4f
    private val fieldFallDurationR   = 0.7f
    private var fieldFallComplete   = false

    private val newTemplateDurationL = 0.7f
    private val newTemplateDurationR = 1f
    private var newTemplateComplete = false

    private val lineDeleteProgress:  Float get() = (progress - lineDeleteDurationL)  / (lineDeleteDurationR  - lineDeleteDurationL  )
    private val fieldFallProgress:   Float get() = (progress - fieldFallDurationL)   / (fieldFallDurationR   - fieldFallDurationL   )
    private val newTemplateProgress: Float get() = (progress - newTemplateDurationL) / (newTemplateDurationR - newTemplateDurationL )

    private var inv = game.fieldView.inv

    private var fieldFigureLine: Array<FigureView> = game.fieldView.getLine(0)
    private var fieldFigureX = Array(Game.FIELD_SIZE) { idx -> fieldFigureLine[idx].xScreen }
    private var fieldFigureY = Array(Game.FIELD_SIZE) { idx -> fieldFigureLine[idx].yScreen }

    private var templateFigureLine: Array<FigureView> = game.templateView.getLine(Game.TEMPLATE_SIZE - 1)
    private var templateFigureX = Array(Game.FIELD_SIZE) { idx -> templateFigureLine[idx].xScreen }
    private var templateFigureY = Array(Game.FIELD_SIZE) { idx -> templateFigureLine[idx].yScreen }

    private var newTemplateLine = game.templateView.getLine(0)
    private var ntLineX = Array(Game.FIELD_SIZE) { idx -> newTemplateLine[idx].xScreen }
    private var ntLineY = Array(Game.FIELD_SIZE) { idx -> newTemplateLine[idx].yScreen }
    private var ntLineW: Float = newTemplateLine[0].wScreen
    private var ntLineH: Float = newTemplateLine[0].hScreen

    private var fieldY = game.fieldView.yScreen
    private var templateY = game.templateView.yScreen

    init {
        Controller.lockAll()
    }

    private fun lineDelete() {

        if(lineDeleteComplete || lineDeleteProgress < 0f)
            return

        var scale = 1f - lineDeleteProgress
        if(scale > 1f) scale = 1f
        if(scale < 0f) scale = 0f

        for(idx in 0 until Game.FIELD_SIZE) {

            fieldFigureLine[idx].setScreenBounds(
                    fieldFigureX[idx] + fieldFigureLine[idx].wScreen * lineDeleteProgress / 2f,
                    fieldFigureY[idx] + fieldFigureLine[idx].hScreen * lineDeleteProgress / 2f
            )
            fieldFigureLine[idx].setScale(scale)

            templateFigureLine[idx].setScreenBounds(
                    templateFigureX[idx] + templateFigureLine[idx].wScreen * lineDeleteProgress / 2f,
                    templateFigureY[idx] + templateFigureLine[idx].hScreen * lineDeleteProgress / 2f
            )
            templateFigureLine[idx].setScale(scale)
        }

        if(lineDeleteProgress >= 1f) {
            lineDeleteComplete = true
            for(idx in 0 until Game.FIELD_SIZE) {
                fieldFigureLine[idx].setScreenBounds(fieldFigureX[idx], fieldFigureY[idx])
                fieldFigureLine[idx].setScale(1f)
                fieldFigureLine[idx].visible = false

                templateFigureLine[idx].setScreenBounds(templateFigureX[idx], templateFigureY[idx])
                templateFigureLine[idx].setScale(1f)
                templateFigureLine[idx].visible = false
            }
        }

    }

    private fun fieldFall() {

        if(fieldFallComplete || fieldFallProgress < 0f)
            return

        val mod = if(!inv) 1 else -1
        game.fieldView.yScreen    = fieldY    - mod * fieldFigureLine[0].wScreen    * fieldFallProgress
        game.templateView.yScreen = templateY + mod * templateFigureLine[0].wScreen * fieldFallProgress


        if(fieldFallProgress >= 1f) {
            fieldFallComplete = true
            game.puzzle.nextLine()

            game.puzzleView.set(game.puzzle)
            game.templateView.yScreen = templateY
            game.fieldView.yScreen = fieldY

            if(game.puzzle.currentLine == Game.FIELD_SIZE) {
                nowMoment = duration + 1f
                newTemplateComplete = true
                return
            }

            newTemplateLine = game.templateView.getLine(0)

            ntLineX = Array(Game.FIELD_SIZE) { idx -> newTemplateLine[idx].xScreen }
            ntLineY = Array(Game.FIELD_SIZE) { idx -> newTemplateLine[idx].yScreen }
            ntLineW = newTemplateLine[0].wScreen
            ntLineH = newTemplateLine[0].hScreen

            for(idx in 0 until Game.FIELD_SIZE)
                newTemplateLine[idx].setScale(0f)
        }

    }

    private fun newTemplate() {

        if(newTemplateComplete || newTemplateProgress < 0f)
            return

        var scale = newTemplateProgress
        if(scale > 1f)
            scale = 1f

        for(idx in 0 until Game.FIELD_SIZE) {
            newTemplateLine[idx].setScreenBounds(
                    ntLineX[idx] + (1f - scale) * ntLineW / 2f,
                    ntLineY[idx] + (1f - scale) * ntLineH / 2f
            )
            newTemplateLine[idx].setScale(scale)
        }

        if(newTemplateProgress >= 1f) {
            for(idx in 0 until Game.FIELD_SIZE) {
                newTemplateLine[idx].setScreenBounds(ntLineX[idx], ntLineY[idx])
                newTemplateLine[idx].setScale(1f)
            }
            newTemplateComplete = true
        }

    }

    override fun finish() {

        for (idx in 0 until Game.FIELD_SIZE)
            newTemplateLine[idx].setScale(1f)

        for(idx in 0 until Game.FIELD_SIZE) {
            fieldFigureLine[idx].setScale(1f)
            templateFigureLine[idx].setScale(1f)
        }

        Controller.unlockAll()

    }



    override fun update(dt: Float): Boolean {

        nowMoment += dt

        lineDelete()
        fieldFall()
        newTemplate()

        if(progress >= 1f) {
            finish()
            return true
        }

        return false

    }




}