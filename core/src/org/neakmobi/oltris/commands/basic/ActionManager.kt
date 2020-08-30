package org.neakmobi.oltris.commands.basic

/**
 *
 * Объект управляющий действиями
 *
 */
object ActionManager {

    var readyForClear: Boolean = false

    val actions = mutableListOf<Action>()
    val actionsForAdd = mutableListOf<Action>()

    fun addAction(action: Action) {
        actionsForAdd.add(action)
    }

    fun clearActions() {
        val iterator = actions.iterator()
        while(iterator.hasNext())
            iterator.next().finish()
        actionsForAdd.clear()
        readyForClear = true
    }

    fun update(dt: Float) {

        if(readyForClear) {
            actions.clear()
            readyForClear = false
            return
        }

        val iterator = actions.iterator()
        while(iterator.hasNext()) {
            if(iterator.next().update(dt))
                iterator.remove()
        }
        actions.addAll(actionsForAdd)
        actionsForAdd.clear()
    }

}