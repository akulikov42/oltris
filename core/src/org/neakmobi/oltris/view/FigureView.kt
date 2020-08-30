package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.neakmobi.oltris.model.Figure
import org.neakmobi.oltris.renderer.Drawable
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.renderer.ScreenManager

/**
 * Визуальное представление фигуры
 */

class FigureView(x: Float, y: Float, w: Float, h: Float): View(), Drawable {

    private var aura = Sprite(Renderer.getRegion("main_atlas", "figure_aura"))

    enum class TypeSelection {
        GOOD_MOVE, BAD_MOVE, SELECTED, NO_SELECTED
    }

    var selection: TypeSelection = TypeSelection.NO_SELECTED
        set(value) {
            field = value
            when(field) {
                TypeSelection.BAD_MOVE      -> { aura.color = Color.RED     }
                TypeSelection.GOOD_MOVE     -> { aura.color = Color.GREEN   }
                TypeSelection.SELECTED      -> { aura.color = Color.GOLD    }
            }
        }

    private var digitMarginWidth  = 0.05f
    private var digitMarginHeight = 0.3f
    private val margin = 0.035f

    private val figureView = Sprite()
    private val costView   = TextView(x + digitMarginWidth, y + digitMarginHeight, "0")

    private var cntCharInCost = 1
    private var charWidthInCost = 0f
    private var charHeightInCost = 0f
    private var yChar = 0f
    private var xChar = 0f

    var blockInput = false
    var visible  = true
    var showCost = true

    init {
        setBounds(x, y, w, h)
        if(FIGURE_COLORS[0] == null) {
            for(idx in 0 until CNT_COLORS)
                FIGURE_COLORS[idx] = Renderer.getRegion("main_atlas", "figure_$idx")
        }
        figureView.setRegion(FIGURE_COLORS[0]!!)
    }

    override fun updateBounds() {
        figureView.setBounds(
                xScreen + ScreenManager.getW(margin),
                yScreen + ScreenManager.getH(margin),
                wScreen - ScreenManager.getW(margin * 2),
                hScreen - ScreenManager.getH(margin * 2)
        )
        aura.setBounds(
                xScreen - ScreenManager.getW(margin),
                yScreen - ScreenManager.getH(margin),
                wScreen + 2 * ScreenManager.getW(margin),
                hScreen + 2 * ScreenManager.getH(margin)
        )
        //figureView.setBounds(xScreen, yScreen, wScreen,hScreen)

        xChar = ((w - digitMarginWidth * 2) - charWidthInCost) / 2 + x + digitMarginWidth
        yChar = y + h * digitMarginHeight
        costView.setBounds(
                xChar,
                yChar,
                charWidthInCost,
                charHeightInCost)
    }

    fun setScale(scale: Float) {
        figureView.setScale(scale)
        costView.setScale(scale)
    }

    fun setAlpha(a: Float) {
        figureView.setColor(figureView.color.r, figureView.color.g, figureView.color.b, a)
    }

    fun set(figure: Figure, needShowCost: Boolean = true) {

        visible = true

        figureView.setRegion(FIGURE_COLORS[figure.type]!!)

        blockInput = figure.moves == 0
        showCost = needShowCost

        if(figure.moves > 0 && needShowCost) {

            costView.setString(figure.moves.toString(), null)

            charHeightInCost = h * (1f - digitMarginHeight * 2)
            charWidthInCost  = w * (1f - digitMarginWidth * 2)

            yChar = y + h * digitMarginHeight
            when(figure.moves) {
                in 1..9     -> {
                    charWidthInCost /= 3
                }
                in 10..99   -> {
                    charWidthInCost = charWidthInCost * 2 / 3
                }
                else        -> { }
            }
            xChar = ((w - digitMarginWidth * 2) - charWidthInCost) / 2 + x + digitMarginWidth

            costView.setBounds(
                        xChar,
                        yChar,
                        charWidthInCost,
                        charHeightInCost)

            showCost = true
        } else {
            showCost = false
        }
    }

    fun clear() {
        showCost = false
        blockInput = true
        visible = true
    }

    /**
     *
     * Отрисовка фигуры.
     *
     */
    override fun drawObj(batch: Batch?) {

        if(!visible)
            return

        if(selection != TypeSelection.NO_SELECTED)
            aura.draw(batch)

        figureView.draw(batch)

        if(showCost)
            costView.drawObj(batch)
    }

    companion object StaticFigureParams {

        private var CNT_COLORS = 6
        private val FIGURE_COLORS = Array<TextureRegion?>(CNT_COLORS) { null }

    }

}