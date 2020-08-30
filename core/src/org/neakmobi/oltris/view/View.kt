package org.neakmobi.oltris.view

import com.badlogic.gdx.math.Rectangle
import org.neakmobi.oltris.renderer.ScreenManager

abstract class View {

    private val rect = Rectangle()

    open var x: Float
        get() = rect.x
        set(value) { rect.x = value; updateBounds() }

    open var y: Float
        get() = rect.y
        set(value) { rect.y = value; updateBounds() }

    open var w: Float
        get() = rect.width
        set(value) { rect.width = value; updateBounds() }

    open var h: Float
        get() = rect.height
        set(value) { rect.height = value; updateBounds() }

    open var xScreen: Float
        get() = ScreenManager.getX(x)
        set(value) { x = ScreenManager.getInvX(value); }

    open var yScreen: Float
        get() = ScreenManager.getY(y)
        set(value) { y = ScreenManager.getInvY(value); }

    open var wScreen: Float
        get() = ScreenManager.getW(w)
        set(value) { w = ScreenManager.getInvW(value); }

    open var hScreen: Float
        get() = ScreenManager.getH(h)
        set(value) { h = ScreenManager.getInvH(value); }

    fun setBounds(
            x: Float = rect.x,
            y: Float = rect.y,
            w: Float = rect.width,
            h: Float = rect.height
    ) {
        rect.x = x
        rect.y = y
        rect.width  = w
        rect.height = h
        updateBounds()
    }

    fun setScreenBounds(
            xScreen: Float = ScreenManager.getX(rect.x),
            yScreen: Float = ScreenManager.getY(rect.y),
            wScreen: Float = ScreenManager.getW(rect.width),
            hScreen: Float = ScreenManager.getH(rect.height)
    ) {
        rect.x = ScreenManager.getInvX(xScreen)
        rect.y = ScreenManager.getInvY(yScreen)
        rect.width = ScreenManager.getInvW(wScreen)
        rect.height = ScreenManager.getInvH(hScreen)
        updateBounds()
    }

    open fun contains(x: Float, y: Float): Boolean = (x > this.xScreen && x < (this.xScreen + this.wScreen) && y > this.yScreen && y < (this.yScreen + this.hScreen))

    protected abstract fun updateBounds()

}