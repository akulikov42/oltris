package org.neakmobi.oltris.controller

import org.neakmobi.oltris.controller.basic.Controller.RESET_HANDLER
import org.neakmobi.oltris.controller.basic.Controller.HOLD_HANDLER
import org.neakmobi.oltris.controller.basic.InputHandler
import org.neakmobi.oltris.view.FieldView
import kotlin.math.abs

/**
 *
 *
 * Обработчик ввода для поля фигур
 * Два основных события:
 *  1. Свап двух фигур
 *  2. Выбор первой фигуры и последующий выбор второй фигуры
 *
 *
 */
class FigureFieldHandler(var fieldView: FieldView): InputHandler() {

    enum class EventType {
        SWAP_FIGURE,
        SELECT_1_FIG,
        SELECT_2_FIG,
        CANCEL_SELECT,
        NO_EVENT
    }

    var blockClick = false
    var blockSwap = false

    var i0 = 0
    var j0 = 0
    var i1 = 0
    var j1 = 0

    var firstFigureSelected : Boolean = false
    var eventType           : EventType = EventType.NO_EVENT
        set(value) {
            if((value == EventType.SWAP_FIGURE && blockSwap) || (value == EventType.SELECT_1_FIG && blockClick)) {
                firstFigureSelected = false
                haveEvents = false
            }
            field = value
        }

    override var haveEvents: Boolean = false
        set(value) {
            field = value
            if(!field) {
                eventType = EventType.NO_EVENT
                //firstFigureSelected = false
            }
        }


    override fun touchDown(x: Float, y: Float, pointer: Int): Boolean {

        haveEvents = false

        i0 = fieldView.getLineFigure(y)
        j0 = fieldView.getColumnFigure(x)

        if(firstFigureSelected) {
            firstFigureSelected = false
            haveEvents = true
            eventType = if(i0 == -1 || j0 == -1)
                EventType.CANCEL_SELECT
            else
                EventType.SELECT_2_FIG

            return RESET_HANDLER
        }

        if(fieldView.getFigure(i0, j0).blockInput || !fieldView.getFigure(i0, j0).showCost || !fieldView.getFigure(i0, j0).visible)
            return RESET_HANDLER

        if(i0 == -1 || j0 == -1)
            return RESET_HANDLER

        return HOLD_HANDLER

    }

    override fun touchDragged(x: Float, y: Float, pointer: Int): Boolean {

        i1 = fieldView.getLineFigure(y)
        j1 = fieldView.getColumnFigure(x)

        if(i1 == -1 || j1 == -1) {
            haveEvents = false
            return RESET_HANDLER
        }

        if(
                (abs(i0 - i1) + abs(j0 - j1)) == 1    &&
                fieldView.getFigure(i1, j1).showCost        &&
                fieldView.getFigure(i0, j0).showCost        &&
                !fieldView.getFigure(i1, j1).blockInput     &&
                !fieldView.getFigure(i0, j0).blockInput
        ) {
            haveEvents = true
            eventType = EventType.SWAP_FIGURE
            return RESET_HANDLER
        }

        return HOLD_HANDLER

    }

    override fun touchUp(x: Float, y: Float, pointer: Int): Boolean {

        if(!fieldView.getFigure(i0, j0).showCost) {
            haveEvents = false
            return RESET_HANDLER
        }
        firstFigureSelected = true
        haveEvents = true
        eventType = EventType.SELECT_1_FIG
        return HOLD_HANDLER

    }

    override fun canAccept(x: Float, y: Float): Boolean = fieldView.contains(x, y)


}