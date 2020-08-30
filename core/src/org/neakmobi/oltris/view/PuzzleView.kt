package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.g2d.Batch
import org.neakmobi.oltris.model.Puzzle
import org.neakmobi.oltris.renderer.Drawable

class PuzzleView(x: Float, y: Float, w: Float, h: Float, _inv: Boolean = false): View(), Drawable {

    val field = FieldView(0f, 0f, 0f, 0f)
    val template = TemplateView(0f, 0f, 0f, 0f)
    var inv: Boolean = _inv
        set(value) {
            field = value
            updateBounds()
        }

    val background  = BackgroundView(0f, 0f, 0f, 0f)

    private val fieldY      : Float get() = background.fieldY
    private val fieldH      : Float get() = background.fieldH
    private val templateY   : Float get() = background.templateY
    private val templateH   : Float get() = background.templateH
    private val strokeSize  : Float get() = background.angleSize

    init {

        setBounds(x, y, w, h)

    }

    override fun updateBounds() {

        field.inv       = inv
        template.inv    = inv
        background.inv  = inv

        background.setBounds(x, y, w, h)
        field.setBounds(x + strokeSize, fieldY, w - strokeSize * 2, fieldH)
        template.setBounds(x + strokeSize, templateY, w - strokeSize * 2, templateH)

    }

    override fun drawObj(batch: Batch?) {
        background.drawObj(batch)
        field.drawObj(batch)
        template.drawObj(batch)
    }

    fun set(p: Puzzle) {
        field.set(p)
        template.set(p)
    }

}