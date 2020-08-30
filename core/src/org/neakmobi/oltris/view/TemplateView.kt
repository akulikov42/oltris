package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.g2d.Batch
import org.neakmobi.oltris.model.Puzzle
import org.neakmobi.oltris.renderer.Drawable
import org.neakmobi.oltris.states.Game

/**
 *
 * Шаблон для сборки
 *
 */
class TemplateView(
        x: Float, y: Float,
        w: Float, h: Float,
        var inv: Boolean = true): View(), Drawable
{

    /**
     *
     * @property figures Фигуры шаблона
     *
     */
    val figures = Array(Game.TEMPLATE_SIZE) { Array(Game.FIELD_SIZE) { FigureView(0f, 0f, 0f, 0f) } }

    var figureWidth: Float = 0f
    var figureHeight: Float = 0f

    init {
        setBounds(x, y, w, h)
        for(idxLine in 0 until Game.TEMPLATE_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                figures[idxLine][idxFigure].showCost = false
            }
        }
    }

    override fun updateBounds() {

        figureWidth = w / Game.FIELD_SIZE
        figureHeight = h / Game.TEMPLATE_SIZE

        for(idxLine in 0 until Game.TEMPLATE_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                figures[idxLine][idxFigure].setBounds(
                        x + idxFigure * figureWidth,
                        y + idxLine * figureHeight,
                        figureWidth,
                        figureHeight
                )
            }
        }
    }

    fun set(puzzle: Puzzle) {


        // ==================================================================
        // ЗАВИСИМОСТЬ ОТ INV
        // ==================================================================

        for(idxLine in 0 until Game.TEMPLATE_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                if(puzzle.currentLine + (Game.TEMPLATE_SIZE - idxLine - 1) < Game.FIELD_SIZE) {
                    figures[if(!inv) idxLine else Game.TEMPLATE_SIZE - idxLine - 1][idxFigure].set(puzzle.solveField.figure(puzzle.currentLine + (Game.TEMPLATE_SIZE - idxLine - 1), idxFigure), false)
                    figures[if(!inv) idxLine else Game.TEMPLATE_SIZE - idxLine - 1][idxFigure].visible = true
                } else {
                    figures[if(!inv) idxLine else Game.TEMPLATE_SIZE - idxLine - 1][idxFigure].visible = false
                }
            }
        }
    }

    fun getLine(idx: Int): Array<FigureView> {
        if(!inv) return figures[idx]
        return figures[Game.TEMPLATE_SIZE - idx - 1]
    }

    /**
     *
     * Отрисовка шаблона
     *
     */
    override fun drawObj(batch: Batch?) {
        for(idxLine in 0 until Game.TEMPLATE_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                figures[idxLine][idxFigure].drawObj(batch)
            }
        }
    }

}