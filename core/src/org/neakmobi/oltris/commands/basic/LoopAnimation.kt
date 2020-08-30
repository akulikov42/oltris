package org.neakmobi.oltris.commands.basic

import org.neakmobi.oltris.commands.basic.Action

open class LoopAnimation: Action {

    var maxAnimations = 10
    var cntAnimations = 0
    var currAnimation = 0
        set(value) { field = value.rem(cntAnimations) }

    var animations = Array<ContinuousAction?>(maxAnimations) { null }

    fun addAnimation(animation: ContinuousAction) {
        if(cntAnimations == maxAnimations) return
        animations[cntAnimations] = animation
        cntAnimations++
    }

    override fun update(dt: Float): Boolean {
        if(animations[currAnimation]!!.update(dt)) {
            animations[currAnimation]!!.reset()
            currAnimation++
        }
        return false
    }

    override fun finish() {
        for(idx in 0 until cntAnimations) {
            animations[idx]!!.finish()
        }
    }

}