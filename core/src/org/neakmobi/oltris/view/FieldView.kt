package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.g2d.Batch
import org.neakmobi.oltris.model.Puzzle
import org.neakmobi.oltris.renderer.Drawable
import org.neakmobi.oltris.renderer.ScreenManager
import org.neakmobi.oltris.states.Game
import kotlin.math.floor

/**
 *
 * Визуальное представления поля фигур
 *
 */

class FieldView(
        x: Float, y: Float,
        w: Float, h: Float,
        var inv: Boolean = false): View(), Drawable
{
    /**
     *
     * @property    figureViews        Массив линий фигур
     *
     */

    var figureWidth: Float = 0f
    var figureHeight: Float = 0f

    var screenFigureWidth: Float = 0f
    var screenFigureHeight: Float = 0f

    val figureViews = Array(Game.FIELD_SIZE) { Array(Game.FIELD_SIZE) { FigureView(0f, 0f, 0f, 0f) } }


    init {
        setBounds(x, y, w, h)
    }


    fun set(puzzle: Puzzle) {

        if(!inv) {
            for (idxLine in puzzle.currentLine until Game.FIELD_SIZE) {
                for (idxFigure in 0 until Game.FIELD_SIZE) {
                    figureViews[idxLine - puzzle.currentLine][idxFigure].set(puzzle.currentField.figure(idxLine, idxFigure))
                }
            }
            for (idxLine in 0 until puzzle.currentLine) {
                for (idxFigure in 0 until Game.FIELD_SIZE) {
                    figureViews[Game.FIELD_SIZE - idxLine - 1][idxFigure].visible = false
                }
            }
        } else {
            for (idxLine in 0 until puzzle.currentLine) {
                for (idxFigure in 0 until Game.FIELD_SIZE) {
                    figureViews[idxLine][idxFigure].visible = false
                }
            }
            for (idxLine in puzzle.currentLine until Game.FIELD_SIZE) {
                for (idxFigure in 0 until Game.FIELD_SIZE) {
                    figureViews[idxLine][idxFigure].set(puzzle.currentField.figure(Game.FIELD_SIZE - idxLine - 1 + puzzle.currentLine, idxFigure))
                }
            }
        }
    }

    /**
     *
     * Отрисовка поля фигур
     *
     */
    override fun drawObj(batch: Batch?) {

        // Отрисовка фигур
        for(idxLine in 0 until Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                figureViews[idxLine][idxFigure].drawObj(batch)
            }
        }

    }

    override fun updateBounds() {

        figureWidth  = w / Game.FIELD_SIZE
        figureHeight = h / Game.FIELD_SIZE

        screenFigureWidth = ScreenManager.getW(figureWidth)
        screenFigureHeight = ScreenManager.getH(figureHeight)

        for(idxLine in figureViews.indices) {
            for (idxFigure in figureViews[idxLine].indices) {
                figureViews[idxLine][idxFigure].setBounds(
                        x + idxFigure * figureWidth,
                        y + idxLine * figureHeight,
                        figureWidth,
                        figureHeight)
            }
        }

    }

    fun getLineFigure(y: Float): Int {
        val yNorm = ScreenManager.getInvY(y)
        val idx = floor(((yNorm - this.y) / figureHeight).toDouble()).toInt()
        if(idx < 0 || idx >= figureViews.size)
            return -1

        return if(!inv) idx else Game.FIELD_SIZE - idx - 1
    }

    fun getColumnFigure(x: Float): Int {
        val xNorm = ScreenManager.getInvX(x)
        //val idx = floor(((xNorm - this.x) / figureWidth).toDouble()).toInt()
        val idx = ((x - this.xScreen) / screenFigureWidth).toInt()
        if(idx < 0 || idx >= figureViews[0].size)
            return -1
        return idx
    }

    fun getFigure(i: Int, j: Int): FigureView {
        if(!inv) return figureViews[i][j]
        return figureViews[Game.FIELD_SIZE - i - 1][j]
    }

    fun getLine(i: Int): Array<FigureView> {
        if(!inv) return figureViews[i]
        return figureViews[Game.FIELD_SIZE - i - 1]
    }

}