package org.neakmobi.oltris.view


import com.badlogic.gdx.graphics.g2d.Batch
import org.neakmobi.oltris.model.Puzzle
import org.neakmobi.oltris.renderer.Drawable

class PlayerInfo(x: Float, y: Float, w: Float, h: Float): View(), Drawable {


    val stageView = StageView(x, y + h / 2, w, h / 2)
    val diamView  = DiamView (x, y, w, h / 2)

    init {
        setBounds(x, y, w, h)
    }

    fun set(p: Puzzle) {
        stageView.set(p.stage)
    }

    override fun updateBounds() {
        stageView.setBounds(x, y + h / 2, w, h / 2)
        diamView.setBounds(x, y, w, h / 2)
    }

    override fun drawObj(batch: Batch?) {
        stageView.drawObj(batch)
        diamView.drawObj(batch)
    }

}