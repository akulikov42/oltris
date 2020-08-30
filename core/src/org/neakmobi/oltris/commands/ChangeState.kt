package org.neakmobi.oltris.commands

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.states.StateManager
import org.neakmobi.oltris.states.States
import org.neakmobi.oltris.view.View

class ChangeState(val views: Array<View>, val on: Boolean): ContinuousAction(CHANGE_STATE_DURATION) {

    companion object {
        const val CHANGE_STATE_DURATION = 0.25f
    }


    var dx = 0f
    var minX = 0f
    var maxX = 16f

    var startX: Array<Float>

    init {
        startX = Array(views.size) { i -> views[i].x }
        if(on) {
            for(idx in views.indices)
                views[idx].x = views[idx].x - 16f
        }
    }

    override fun finish() {

        for(idx in views.indices)
            views[idx].x = startX[idx]

    }

    override fun update(dt: Float): Boolean {

        nowMoment += dt

        dx = (maxX - minX) * progress

        for(idx in views.indices) {
            if(!on) {
                views[idx].x = startX[idx] + dx
            } else {
                views[idx].x = startX[idx] - 16f + dx
            }
        }

        if(progress >= 1f) {
            finish()
            return true
        }

        return false

    }

}