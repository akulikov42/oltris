package org.neakmobi.oltris.commands

import org.neakmobi.oltris.commands.basic.Action
import org.neakmobi.oltris.states.Game

class Undo(val game: Game): Action {

    override fun update(dt: Float): Boolean {
        
        if(game.puzzle.undo()) {
            game.updateView()
        }

        return true
    }

    override fun finish() {

    }
}