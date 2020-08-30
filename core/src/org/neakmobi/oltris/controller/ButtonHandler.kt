package org.neakmobi.oltris.controller

import com.badlogic.gdx.Input
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.controller.basic.InputHandler
import org.neakmobi.oltris.view.Button

class ButtonHandler: InputHandler() {

    companion object {
        val BACK_BUTTON = Button(null, 0f, 0f, 0f, 0f)
        var BACKSPACE_BUTTON = Button(null, 0f, 0f, 0f, 0f)
    }

    private val buttons = mutableListOf<Button>()
    private var focusButton: Button? = null

    val clicked = mutableListOf<Button>()
    override var haveEvents: Boolean
        get() = clicked.isNotEmpty()
        set(value) {}

    override fun touchDown(x: Float, y: Float, pointer: Int): Boolean {

        focusButton?.push()
        return Controller.HOLD_HANDLER
    }

    override fun touchDragged(x: Float, y: Float, pointer: Int): Boolean {

        if(focusButton != null && focusButton!!.contains(x, y)) {
            return Controller.HOLD_HANDLER
        }

        focusButton!!.release(false)

        focusButton = null
        return Controller.RESET_HANDLER
    }

    override fun touchUp(x: Float, y: Float, pointer: Int): Boolean {

        focusButton?.release()
        clicked.add(focusButton!!)
        focusButton = null
        return Controller.RESET_HANDLER

    }

    override fun keyUp(keycode: Int): Boolean {

        when(keycode) {
            Input.Keys.BACK ->      { clicked.add(BACK_BUTTON)      }
            Input.Keys.BACKSPACE -> { clicked.add(BACKSPACE_BUTTON) }
        }
        return true
    }

    fun addButton(btn: Button)          { buttons.add(btn)     }
    fun removeButton(btn: Button)       { buttons.remove(btn)  }
    fun clearButtons()                  { buttons.clear()      }

    override fun canAccept(x: Float, y: Float): Boolean {

        for(btn in buttons) {
            if(btn.contains(x, y) && btn.available) {
                focusButton = btn
                return true
            }
        }

        focusButton = null
        return false
    }

}