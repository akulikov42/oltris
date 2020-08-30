package org.neakmobi.oltris.commands.basic

/**
 *
 * Продолжительное действие
 *
 * @property    duration        Длительность действия. Если == 0, то действие моментальное
 * @property    nowMoment       Текущий момент исполнения действия
 * @property    progress        Нормированный текущий момент (от 0 до 1)
 *
 */

abstract class ContinuousAction(duration_: Float): Action {
    protected var duration = duration_
    protected var nowMoment = 0f
    protected val progress get() = nowMoment / duration
    val complete get() = nowMoment > duration
    open fun reset() { nowMoment = 0f }
}