package org.neakmobi.oltris.renderer

import com.badlogic.gdx.math.Rectangle
import org.neakmobi.oltris.controller.basic.Controller

object ScreenManager {

    private var height = 0f
    private var width = 0f

    private var cntHeightPart = 16f
    private var cntWidthPart  = 9f

    private var gameRatio:      Float = cntWidthPart  / cntHeightPart
    private var invGameRatio:   Float = cntHeightPart / cntWidthPart
    private var screenRatio:    Float = width  / height
    private var invScreenRatio: Float = height / width

    val gameRect   = Rectangle()
    val screenRect = Rectangle()

    fun init(width: Float, height: Float) {

        screenRect.x      = 0f
        screenRect.y      = 0f
        screenRect.width  = width
        screenRect.height = height

        screenRatio    = width  / height
        invScreenRatio = height / width

        when {
            gameRatio > screenRatio -> {

                gameRect.width  = width
                gameRect.height = invGameRatio * gameRect.width
                gameRect.x      = 0f
                gameRect.y      = (height - gameRect.height) / 2f

            }
            gameRatio < screenRatio -> {

                gameRect.height = height
                gameRect.width  = gameRatio * gameRect.height
                gameRect.x      = (width - gameRect.width) / 2f
                gameRect.y      = 0f

            }
            else -> {

                gameRect.width  = width
                gameRect.height = height
                gameRect.x = 0f
                gameRect.y = 0f

            }
        }
        Controller.screenHeight = height
        Controller.screenWidth = width

    }

    fun update(width: Float, height: Float) {
        init(width, height)
    }

    fun getX(propX: Float) = gameRect.x + (propX / cntWidthPart)  * gameRect.width
    fun getY(propY: Float) = gameRect.y + (propY / cntHeightPart) * gameRect.height
    fun getW(propW: Float) = (propW / cntWidthPart)  * gameRect.width
    fun getH(propH: Float) = (propH / cntHeightPart) * gameRect.height

    fun getInvX(x: Float) = (x - gameRect.x) * cntWidthPart / gameRect.width
    fun getInvY(y: Float) = (y - gameRect.y) * cntHeightPart / gameRect.height
    fun getInvW(w: Float) = w * cntWidthPart / gameRect.width
    fun getInvH(h: Float) = h * cntHeightPart / gameRect.height

}