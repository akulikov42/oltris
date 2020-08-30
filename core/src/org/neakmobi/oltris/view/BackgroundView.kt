package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.g2d.Batch
import org.neakmobi.oltris.renderer.Drawable
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.states.Game

class BackgroundView(x: Float, y: Float, w: Float, h: Float, var inv: Boolean = false): View(), Drawable {

    private lateinit var angles: Array<ImageView>
    private lateinit var stroke: Array<ImageView>
    private lateinit var fill: ImageView
    var angleSize = 0f

    var templateY = 0f
    var templateH = 0f
    var fieldY    = 0f
    var fieldH    = 0f

    init {
        setBounds(x, y, w, h)
    }

    override fun updateBounds() {

        angles = Array(4) { ImageView(0f, 0f, 0f, 0f) }
        stroke = Array(5) { ImageView(0f, 0f, 0f, 0f) }
        fill   = ImageView(0f, 0f, 0f, 0f)

        for(idx in angles.indices)
            angles[idx].setImage(Renderer.getRegion("main_atlas", "angle"))

        for(idx in stroke.indices)
            stroke[idx].setImage(Renderer.getRegion("main_atlas", "stroke"))

        fill.setImage(Renderer.getRegion("main_atlas", "fill"))


        angleSize = w / 25f
        // left up
        angles[0].setBounds(x, y + h - angleSize, angleSize, angleSize)
        // left down
        angles[1].setBounds(x, y, angleSize, angleSize)
        // right up
        angles[2].setBounds(x + w - angleSize, y + h - angleSize, angleSize, angleSize)
        // right down
        angles[3].setBounds(x + w - angleSize, y, angleSize, angleSize)


        fill.setBounds(x + angleSize / 2, y + angleSize / 2, w - angleSize, h - angleSize)


        // left stroke
        stroke[0].setBounds(x, y + angleSize / 2, angleSize, h - angleSize)

        // up stroke
        stroke[1].setBounds(x + angleSize / 2, y + h - angleSize, w - angleSize, angleSize)

        // right stroke
        stroke[2].setBounds(x + w - angleSize, y + angleSize / 2, angleSize, h - angleSize)

        // down stroke
        stroke[3].setBounds(x + angleSize / 2, y, w - angleSize, angleSize)

        templateH = Game.TEMPLATE_SIZE * (h - 3 * angleSize) / (Game.TEMPLATE_SIZE + Game.FIELD_SIZE)
        fieldH    = Game.FIELD_SIZE           * (h - 3 * angleSize) / (Game.TEMPLATE_SIZE + Game.FIELD_SIZE)

        // template stroke
        if(!inv) {
            templateY = y + angleSize
            fieldY    = y + angleSize * 2 + Game.TEMPLATE_SIZE * (h - 3 * angleSize) / (Game.TEMPLATE_SIZE + Game.FIELD_SIZE)
            stroke[4].setBounds(
                    x + angleSize / 2,
                    y + angleSize + Game.TEMPLATE_SIZE * (h - 3 * angleSize) / (Game.TEMPLATE_SIZE + Game.FIELD_SIZE),
                    w - angleSize,
                    angleSize
            )
        } else {
            templateY = y + angleSize * 2 + Game.FIELD_SIZE * (h - 3 * angleSize) / (Game.TEMPLATE_SIZE + Game.FIELD_SIZE)
            fieldY    = y + angleSize
            stroke[4].setBounds(
                    x + angleSize / 2,
                    y + angleSize + Game.FIELD_SIZE * (h - 3 * angleSize) / (Game.TEMPLATE_SIZE + Game.FIELD_SIZE),
                    w - angleSize,
                    angleSize
            )
        }

    }

    override fun drawObj(batch: Batch?) {
        fill.drawObj(batch)
        for(idx in stroke.indices)
            stroke[idx].drawObj(batch)
        for(idx in angles.indices)
            angles[idx].drawObj(batch)
    }

}