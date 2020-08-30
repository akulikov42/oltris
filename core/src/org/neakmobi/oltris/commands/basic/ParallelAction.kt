package org.neakmobi.oltris.commands.basic

import org.neakmobi.oltris.commands.basic.Action

/**
 *
 * Группа параллельно исполняющихся действий
 *
 */

abstract class ParallelAction: Action {

    private val MAX_ACTIONS = 10
    private var cntActiveAction = 0
    private val actions = Array<Action?>(MAX_ACTIONS) { null }

    fun addAction(action: Action) {
        actions[cntActiveAction++] = action
    }

    override fun update(dt: Float): Boolean {
        for(idx in actions.indices) {
            if(actions[idx] == null)
                continue
            if(actions[idx]!!.update(dt))
                cntActiveAction--
        }
        return cntActiveAction == -1
    }

}