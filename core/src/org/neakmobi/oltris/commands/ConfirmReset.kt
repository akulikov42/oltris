package org.neakmobi.oltris.commands

import org.neakmobi.oltris.Player
import org.neakmobi.oltris.commands.basic.Action
import org.neakmobi.oltris.controller.ButtonHandler
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.states.MainMenu
import org.neakmobi.oltris.view.TextButton
import org.neakmobi.oltris.view.TextView

class ConfirmReset(val mainMenu: MainMenu): Action {

    private val firstLineMessage = TextView(0f, 9f, "RESET YOUR", 0.75f,1f, 9f)
    private val secondLineMessage = TextView(0f, 8f, "PROGRESS?", 0.75f, 1f, 9f)
    private val yesButton = TextButton(1.5f, 6f, 2f, 1f, "YES")
    private val noButton  = TextButton(6f, 6f, 2f, 1f, "NO")

    init {
        mainMenu.playBtn.available  = false
        mainMenu.changeTemplateBtn.available  = false
        mainMenu.resetBtn.available = false
        mainMenu.buttonHandler.clearButtons()
        mainMenu.buttonHandler.addButton(yesButton)
        mainMenu.buttonHandler.addButton(noButton)
        Renderer.subscribe(firstLineMessage)
        Renderer.subscribe(secondLineMessage)
        Renderer.subscribe(yesButton)
        Renderer.subscribe(noButton)
    }

    override fun finish() {
        mainMenu.playBtn.available  = true
        mainMenu.changeTemplateBtn.available  = true
        mainMenu.resetBtn.available = true
        mainMenu.buttonHandler.clearButtons()
        mainMenu.buttonHandler.addButton(mainMenu.playBtn)
        mainMenu.buttonHandler.addButton(mainMenu.changeTemplateBtn)
        mainMenu.buttonHandler.addButton(mainMenu.resetBtn)
        Renderer.unsubscribe(firstLineMessage)
        Renderer.unsubscribe(secondLineMessage)
        Renderer.unsubscribe(yesButton)
        Renderer.unsubscribe(noButton)
    }

    override fun update(dt: Float): Boolean {

        if(mainMenu.buttonHandler.haveEvents) {
            for(clicked in mainMenu.buttonHandler.clicked) {
                when(clicked) {
                    yesButton -> { Player.reset(); finish(); return true }
                    noButton -> { finish(); return true }
                    ButtonHandler.BACK_BUTTON -> { finish(); return true }
                }
            }
        }
        return false
    }
}