package org.neakmobi.oltris.states

object StateManager {

    private var states = mutableMapOf<States, State>()

    var currentState: State = EmptyState()

    fun addState(key: States, state: State) {
        states[key] = state
    }

    fun removeState(key: States) {
        states.remove(key)
    }

    fun state(key: States): State? {
        return states[key]
    }

    fun changeState(nextStateKey: States) {

        if(states[nextStateKey] == null)
            return

        currentState.finish()
        currentState = states[nextStateKey]!!
        currentState.start()

    }

    fun update(dt: Float) {
        currentState.update(dt)
    }

    fun pause() {
        currentState.pause()
    }

    fun resume() {
        currentState.resume()
    }

    fun resize(width: Int, height: Int) {
        currentState.resize(width, height)
    }

}