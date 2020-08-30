package org.neakmobi.oltris.controller.basic

import com.badlogic.gdx.InputProcessor

/**
 *
 * Обработчик событий ввода. Данный класс проверяет событие ввода и передает его подходящему
 * обработчику. Координаты касаний, который приходят от InputProcessor, считаются с правого
 * верхнего угла. Для перевода координат в систему, используемую в игре, применяется метод [adaptCoordinates]
 *
 *
 */
object Controller: InputProcessor {

    val HOLD_HANDLER  = true
    val RESET_HANDLER = false

    /**
     *
     * @property x                  Адаптированная Х координата
     * @property y                  Адаптированная Y координата
     * @property screenHeight       Высотка экрана (для адаптирования)
     * @property screenWidth        Ширина экрана (для адаптирования)
     *
     * @property emptyHandler       Пустой обработчик
     * @property currentHandler     Текущий обработчик
     * @property handlers           Все имеющиеся обработчики
     *
     */
    var x = 0f
    var y = 0f
    var screenHeight = 0f
    var screenWidth  = 0f
    var currentPointer = 0

    private val handlers = mutableListOf<InputHandler>()
    private val lockCounters = mutableMapOf<InputHandler, Int>()

    private val emptyHandler = InputHandler()
    private var currentHandler = emptyHandler

    /**
     *
     * Добавление обработчика
     *
     */
    fun addHandler(h: InputHandler) {
        handlers.add(h)
        lockCounters[h] = 0
    }

    fun removeHandler(h: InputHandler) {
        handlers.remove(h)
        lockCounters.remove(h)
    }

    /**
     *
     * Удаление всех обарботчиков
     *
     */
    fun removeAll() {
        currentHandler = emptyHandler
        handlers.clear()
        lockCounters.clear()
    }

    /**
     *
     * Блокирование всех обработчкиов
     *
     */
    fun lockAll() {
        for(handler in handlers) {
            handler.lock = true
            lockCounters[handler] = lockCounters[handler]!! + 1
        }
    }

    /**
     *
     * Разблокирование всех обработчкиов
     *
     */
    fun unlockAll() {
        for(handler in handlers) {
            lockCounters[handler] = if(lockCounters[handler]!! - 1 > 0) lockCounters[handler]!! - 1 else 0
            if(lockCounters[handler] == 0)
                handler.lock = false
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        currentPointer = pointer

        adaptCoordinates(screenX, screenY)

        if(currentHandler == emptyHandler)
            selectHandler(x, y)

        if(currentHandler.lock)
            currentHandler = emptyHandler

        if(currentHandler.touchDown(x, y, pointer) == RESET_HANDLER)
            currentHandler = emptyHandler

        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {

        if(pointer != currentPointer) return true

        adaptCoordinates(screenX, screenY)

        if(currentHandler.lock)
            currentHandler = emptyHandler

        if(currentHandler.touchDragged(x, y, pointer) == RESET_HANDLER)
            currentHandler = emptyHandler

        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        if(pointer != currentPointer) return true

        adaptCoordinates(screenX, screenY)

        if(currentHandler.lock)
            currentHandler = emptyHandler

        if(currentHandler.touchUp(x, y, pointer) == RESET_HANDLER)
            currentHandler = emptyHandler

        return true
    }

    override fun keyUp(keycode: Int): Boolean {

        for(handler in handlers) {
            if(handler.lock) continue
            handler.keyUp(keycode)
        }
        return true
    }

    /**
     *
     * Выбор подходящего обработчика
     *
     */
    private fun selectHandler(x: Float, y: Float) {
        for(handler in handlers) {
            if(!handler.lock && handler.canAccept(x, y)) {
                currentHandler = handler
                break
            }
        }
    }

    fun resetHandler() {
        currentHandler.haveEvents = false
        currentHandler = emptyHandler
    }

    /**
     *
     * Адаптирование координат нажатия в координаты игрвого экрана
     *
     */
    private fun adaptCoordinates(xScreen: Int, yScreen: Int) {
        x = xScreen.toFloat()
        y = screenHeight - yScreen.toFloat()
    }


    /* Заглушки */
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean                    { return true; }
    override fun keyTyped(character: Char): Boolean                                 { return true; }
    override fun scrolled(amount: Int): Boolean                                     { return true; }
    override fun keyDown(keycode: Int): Boolean                                     { return true; }

}