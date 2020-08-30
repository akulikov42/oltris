package org.neakmobi.oltris.states

class EmptyState: State() {
    override fun start()            {}
    override fun finish()           {}
    override fun pause()            {}
    override fun resume()           {}
    override fun update(dt: Float)  {}
    override fun updateView()       {}
    override fun resize(width: Int, height: Int) {}
}