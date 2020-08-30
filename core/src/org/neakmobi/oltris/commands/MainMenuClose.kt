package org.neakmobi.oltris.commands

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.renderer.ScreenManager
import org.neakmobi.oltris.states.StateManager
import org.neakmobi.oltris.states.States
import org.neakmobi.oltris.view.Button
import org.neakmobi.oltris.view.ImageView
import org.neakmobi.oltris.view.LeagueView
import org.neakmobi.oltris.view.StageView

class MainMenuClose(
        private val btns: Array<Button>,
        private val stage: StageView,
        private val league: LeagueView,
        private val logo: ImageView,
        private val nextState: States
): ContinuousAction(0.6f) {


    private val timeBtnL = 0.50f
    private val timeBtnR = 1f
    private val btnProgress: Float get() = (progress - timeBtnL) / (timeBtnR - timeBtnL)

    private val timeLogoL = 0.25f
    private val timeLogoR = 0.75f
    private val logoProgress: Float get() = (progress - timeLogoL) / (timeLogoR - timeLogoL)

    private val timeStageL = 0f
    private val timeStageR = 0.5f
    private val stageProgress: Float get() = (progress - timeStageL) / (timeStageR - timeStageL)

    private val yBtns = Array(btns.size) { idx -> btns[idx].yScreen }
    private val yLogo = logo.yScreen
    private val xStage = stage.xScreen
    private val xLeague = league.xScreen

    private val yBtnsEnd = (-(Array(btns.size) { idx -> btns[idx].hScreen }).max()!!) * 1.2f
    private val yLogoEnd = ScreenManager.screenRect.height * 1.2f
    private val xStageEnd = ScreenManager.screenRect.width * 1.2f
    private val xLeagueEnd = -league.wScreen * 1.2f

    private var stageTransformComplete = false
    private var btnTransformComplete   = false
    private var logoTransformComplete  = false

    init { Controller.lockAll() }

    private fun stageAndLeagueTransform() {
        if(stageTransformComplete || stageProgress < 0f)
            return
        stage.xScreen = xStage - stageProgress * (xStage - xStageEnd)
        league.xScreen = xLeague - stageProgress * (xLeague - xLeagueEnd)
        if(stageProgress >= 1f)
            stageTransformComplete = true
    }

    private fun btnTransform() {
        if(btnTransformComplete || btnProgress < 0f)
            return
        for(idx in btns.indices)
            btns[idx].yScreen = yBtns[idx] - btnProgress * (yBtns[idx] - yBtnsEnd)


        if(btnProgress >= 1f)
            btnTransformComplete = true
    }
    private fun logoTransform() {
        if(logoTransformComplete || logoProgress < 0f)
            return
        logo.yScreen = yLogo - logoProgress * (yLogo - yLogoEnd)

        if(logoProgress >= 1f)
            logoTransformComplete = true
    }

    override fun update(dt: Float): Boolean {

        nowMoment += dt

        if(progress >= 1f) {
            finish()
            return true
        }

        btnTransform()
        stageAndLeagueTransform()
        logoTransform()

        return false
    }

    override fun finish() {
        Controller.unlockAll()
        for(idx in btns.indices)
            btns[idx].yScreen = yBtns[idx]
        logo.yScreen = yLogo
        stage.xScreen = xStage
        league.xScreen = xLeague
        StateManager.changeState(nextState)
    }


}