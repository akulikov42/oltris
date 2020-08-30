package org.neakmobi.oltris.view

class DiamView(x: Float, y: Float, w: Float, h: Float): StageView(x, y, w, h, "D") {
    override fun set(value: Int) {
        current.setString(preamble + "$value")
    }
}