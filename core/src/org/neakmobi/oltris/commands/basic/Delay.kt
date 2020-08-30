package org.neakmobi.oltris.commands.basic

class Delay(duration: Float): ContinuousAction(duration) {
    override fun update(dt: Float): Boolean {
        nowMoment += dt
        if(progress >= 1f)
            return true
        return false
    }

    override fun finish() {}

}