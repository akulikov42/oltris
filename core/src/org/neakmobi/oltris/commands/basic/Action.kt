package org.neakmobi.oltris.commands.basic

/**
 *
 * Команда/действие (простое)
 *
 */

interface Action {
    fun update(dt: Float): Boolean
    fun finish()
}
