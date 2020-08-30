package org.neakmobi.oltris.commands.basic

import org.neakmobi.oltris.commands.basic.Action

/**
 *
 * Очередь действий
 *
 */
open class QueueAction: Action {

    private val MAX_ACTIONS = 15
    var cntActiveAction = 0
    private val actions = Array<Action?>(MAX_ACTIONS) { null }

    fun addAction(action: Action) {
        actions[cntActiveAction++] = action
    }

    private fun completeAction() {
        for(idx in 1 until cntActiveAction) {
            actions[idx - 1] = actions[idx]
        }
        actions[cntActiveAction - 1] = null
        cntActiveAction--
    }

    override fun update(dt: Float): Boolean {
        if(cntActiveAction == 0) {
            finish()
            return true
        }
        if(actions[0]!!.update(dt))
            completeAction()
        return false
    }

    override fun finish() {
        for(idx in 0 until cntActiveAction)
            actions[idx]?.finish()
    }

}