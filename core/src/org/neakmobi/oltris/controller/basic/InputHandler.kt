package org.neakmobi.oltris.controller.basic

import org.neakmobi.oltris.controller.basic.Controller.RESET_HANDLER

/**
 *
 * Общий класс для обработчиков событий ввода.
 *
 * @param controller ссылка на [объекта-хранителя][controller]
 *
 */
open class InputHandler {

    var lock             : Boolean = false
    open var haveEvents  : Boolean = false

    /**
     *
     * Обработка нажатия на экран (мышь)
     * @return Если возвращает false, [controller] сбрасывает обработчика
     *
     */
    open fun touchDown(x: Float, y: Float, pointer: Int): Boolean { return RESET_HANDLER }

    /**
     *
     * Обработка нажатия свапа (перетаскивание мышью)
     * @return Если возвращает false, [controller] сбрасывает обработчика
     *
     */
    open fun touchDragged(x: Float, y: Float, pointer: Int): Boolean { return RESET_HANDLER }

    /**
     *
     * Обработка подъема пальца (отщелкивания мыши)
     * @return Если возвращает false, [controller] сбрасывает обработчика
     *
     */
    open fun touchUp(x: Float, y: Float, pointer: Int): Boolean { return RESET_HANDLER }

    open fun keyUp(keycode: Int): Boolean { return true }

    /**
     *
     * Если обработчик может принять касание, то возвращает true
     * @return Если возвращает false, [controller] сбрасывает обработчика
     *
     */
    open fun canAccept(x: Float, y: Float): Boolean = false

}