package org.neakmobi.oltris.commands

import org.neakmobi.oltris.commands.basic.ContinuousAction
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.renderer.ScreenManager
import org.neakmobi.oltris.view.Button
import org.neakmobi.oltris.view.ImageView
import org.neakmobi.oltris.view.LeagueView
import org.neakmobi.oltris.view.StageView

class MainMenuShow(
        private val btns: Array<Button>,
        private val stage: StageView,
        private val league: LeagueView,
        private val logo: ImageView
): ContinuousAction(0.6f) {


    private val timeBtnL = 0f
    private val timeBtnR = 0.50f
    private val btnProgress: Float get() = (progress - timeBtnL) / (timeBtnR - timeBtnL)

    private val timeLogoL = 0.25f
    private val timeLogoR = 0.75f
    private val logoProgress: Float get() = (progress - timeLogoL) / (timeLogoR - timeLogoL)

    private val timeStageL = 0.50f
    private val timeStageR = 1f
    private val stageProgress: Float get() = (progress - timeStageL) / (timeStageR - timeStageL)

    private val yBtns = Array(btns.size) { idx -> btns[idx].yScreen }

    private val yLogo = logo.yScreen

    private val xStage = stage.xScreen
    private val xLeague = league.xScreen


    private val yBtnsStart = -((Array(btns.size) { idx -> btns[idx].hScreen }).max()!!) * 1.2f
    private val yLogoStart = ScreenManager.screenRect.height * 1.2f
    private val xStageStart = -stage.wScreen * 1.2f
    private val xLeagueStart = ScreenManager.screenRect.width * 1.2f

    private var stageTransformComplete = false
    private var btnTransformComplete   = false
    private var logoTransformComplete  = false

    init {
        Controller.lockAll()
        stage.xScreen = xStageStart
        league.xScreen = xLeagueStart
        logo.yScreen = yLogoStart
        for(idx in btns.indices)
            btns[idx].yScreen = yBtnsStart
    }

    private fun stageAndLeagueTransform() {
        if(stageTransformComplete || stageProgress < 0f)
            return
        if(stageProgress >= 1f) {
            stage.xScreen = xStage
            league.xScreen = xLeague
            stageTransformComplete = true
            return
        }
        stage.xScreen = xStage - (1f - stageProgress) * (xStage - xStageStart)
        league.xScreen = xLeague - (1f - stageProgress) * (xLeague - xLeagueStart)
    }

    private fun btnTransform() {
        if(btnTransformComplete || btnProgress < 0f)
            return
        if(btnProgress >= 1f) {
            for(idx in btns.indices)
                btns[idx].yScreen = yBtns[idx]
            btnTransformComplete = true
            return
        }
        for(idx in btns.indices) {
            btns[idx].yScreen = yBtnsStart + btnProgress * (yBtns[idx] - yBtnsStart)
        }
    }
    private fun logoTransform() {
        if(logoTransformComplete || logoProgress < 0f)
            return
        if(logoProgress >= 1f) {
            logo.yScreen = yLogo
            logoTransformComplete = true
            return
        }
        logo.yScreen = yLogoStart - logoProgress * (yLogoStart - yLogo)
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
    }


}