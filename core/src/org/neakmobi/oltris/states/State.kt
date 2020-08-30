package org.neakmobi.oltris.states

import org.neakmobi.oltris.renderer.ScreenManager

abstract class State {

    abstract fun start()
    abstract fun finish()
    abstract fun pause()
    abstract fun resume()

    /**
     * Обновление внутреннего состояния стейта. Возвращает указатель на себя, если
     * стейт не меняется. В противном случае возвращает указатель на стейт, на который
     * необходимо перейти
     */
    abstract fun update(dt: Float)

    abstract fun resize(width: Int, height: Int)

    abstract fun updateView()

}