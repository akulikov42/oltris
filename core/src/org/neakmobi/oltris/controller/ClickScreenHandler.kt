package org.neakmobi.oltris.controller

import org.neakmobi.oltris.controller.basic.Controller.HOLD_HANDLER
import org.neakmobi.oltris.controller.basic.Controller.RESET_HANDLER
import org.neakmobi.oltris.controller.basic.InputHandler

class ClickScreenHandler: InputHandler() {
    override fun touchDown(x: Float, y: Float, pointer: Int): Boolean {
        return HOLD_HANDLER
    }

    override fun canAccept(x: Float, y: Float): Boolean {
        return true
    }

    override fun touchDragged(x: Float, y: Float, pointer: Int): Boolean {
        return HOLD_HANDLER
    }

    override fun touchUp(x: Float, y: Float, pointer: Int): Boolean {
        haveEvents = true
        return RESET_HANDLER
    }

}