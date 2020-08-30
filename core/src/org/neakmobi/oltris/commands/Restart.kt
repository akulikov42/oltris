package org.neakmobi.oltris.commands

import org.neakmobi.oltris.commands.basic.Action
import org.neakmobi.oltris.states.Game

class Restart(val game: Game): Action {

    override fun update(dt: Float): Boolean {
        game.puzzle.restart()
        finish()
        return true
    }

    override fun finish() {
        game.updateView()
    }

}