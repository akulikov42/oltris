package org.neakmobi.oltris.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Rectangle
import org.neakmobi.oltris.Player
import org.neakmobi.oltris.commands.*
import org.neakmobi.oltris.renderer.ScreenManager
import org.neakmobi.oltris.commands.basic.ActionManager
import org.neakmobi.oltris.commands.basic.Delay
import org.neakmobi.oltris.commands.basic.QueueAction
import org.neakmobi.oltris.commands.tutorial.*
import org.neakmobi.oltris.controller.basic.Controller
import org.neakmobi.oltris.controller.FigureFieldHandler
import org.neakmobi.oltris.controller.ButtonHandler
import org.neakmobi.oltris.model.Puzzle
import org.neakmobi.oltris.model.PuzzleBuilder
import org.neakmobi.oltris.model.PuzzleEvent
import org.neakmobi.oltris.renderer.Renderer
import org.neakmobi.oltris.view.*

/**
 *
 * Основной класс, отвечающий за логику игры.
 *
 *
 */

class Game: State() {

    /**
     *
     * @property CNT_FIGURE_TYPE        Число различных типов фигур
     *
     */
    companion object {
        const val CNT_FIGURE_TYPE = 6
        const val FIELD_SIZE = 6
        const val TEMPLATE_SIZE = 3
    }
    /**
     *
     * @property    gameRect                Область, занимаемая игрой
     * @property    screenRect              Область экрана
     * @property    fieldView               Игрового поле
     * @property    figureFieldHandler      Обработчик ввода игрового поля
     * @property    buttonHandler           Обработчик ввода игрового интерфейса
     *
     */


    val             puzzleBuilder   : PuzzleBuilder     = PuzzleBuilder()
    var             puzzle          : Puzzle            = Puzzle()

    var             puzzleView      : PuzzleView        = PuzzleView(0.5f, 2f, 8f, 12f, false)
    val             fieldView       : FieldView         get() = puzzleView.field
    val             templateView    : TemplateView      get() = puzzleView.template

    val             stageView       : StageView         = StageView(1.8f, 14.525f, 5.4f, 0.75f, "")

    val             undoBtn         : Button            = Button(Renderer.getRegion("main_atlas", "undo_button"), 7.2f, 14.3f, 1.2f, 1.2f)
    val             restartBtn      : Button            = Button(Renderer.getRegion("main_atlas", "restart_button"), 0.6f, 14.3f, 1.2f, 1.2f)

    val             gameRect        : Rectangle         get() = ScreenManager.gameRect
    val             screenRect      : Rectangle         get() = ScreenManager.screenRect

    val             figureFieldHandler                  = FigureFieldHandler(fieldView)
    val             buttonHandler                       = ButtonHandler()

    var             prepareToClose = false

    override fun start() {

        puzzle = Player.puzzle

        prepareToClose = false

        puzzleView.inv = !Player.templateBelow

        Controller.addHandler(figureFieldHandler)
        Controller.addHandler(buttonHandler)
        Gdx.input.setCatchKey(Input.Keys.BACK, true)

        buttonHandler.addButton(undoBtn)
        buttonHandler.addButton(restartBtn)

        Renderer.subscribe(puzzleView)
        Renderer.subscribe(stageView)
        Renderer.subscribe(undoBtn)
        Renderer.subscribe(restartBtn)

        updateView()

        if(puzzle.stage == 1 && puzzle.userMoves.isEmpty()) {
            val queueAction = QueueAction()
            queueAction.addAction(GameShow(puzzleView, stageView, undoBtn, restartBtn))
            queueAction.addAction(SwapTutorial(puzzleView, figureFieldHandler))
            queueAction.addAction(Delay(0.7f))
            queueAction.addAction(ClickTutorial(this, puzzleView, figureFieldHandler))
            queueAction.addAction(Delay(0.7f))
            queueAction.addAction(MainQuestTutorial_1(this, puzzleView))
            queueAction.addAction(UndoTutorial(this))
            queueAction.addAction(MainQuestTutorial_2(this, puzzleView))
            queueAction.addAction(MainQuestTutorial_3(this, puzzleView))
            ActionManager.addAction(queueAction)
        } else {
            ActionManager.addAction(GameShow(puzzleView, stageView, undoBtn, restartBtn))
        }



    }

    override fun finish() {
        Controller.removeAll()
        Renderer.clear()
        prepareToClose = false
    }

    override fun pause() {
        ActionManager.clearActions()
        Player.save()
    }

    override fun resume() {
        Player.load()
        puzzle = Player.puzzle
        updateView()
    }


    /**
     *
     * Обновление внутреннего состояния игры
     *
     */
    override fun update(dt: Float) {
        ActionManager.update(dt)
        handleInput()
        checkEvents()
    }

    override fun updateView() {
        fieldView.set(puzzle)
        templateView.set(puzzle)
        stageView.set(puzzle.stage)
        undoBtn.available = !puzzle.userMoves.isEmpty()
    }

    /**
     *
     * Обработка событий игры
     *
     */
    private fun checkEvents() {

        while(!puzzle.events.isEmpty()) {
            when(puzzle.events.poll()) {
                PuzzleEvent.LINE_COMPLETED  -> { ActionManager.addAction(DeleteLine(this)) }
                PuzzleEvent.STAGE_COMPLETED -> { ActionManager.addAction(NextStage(this))  }
                null -> {}
            }
        }

    }

    /**
     *
     * Обработка ввода
     *
     */
    private fun handleInput() {

        if(figureFieldHandler.haveEvents) {
            when(figureFieldHandler.eventType) {
                FigureFieldHandler.EventType.SWAP_FIGURE -> {
                    ActionManager.addAction(SwapFigure(
                            this,
                            figureFieldHandler.i0,
                            figureFieldHandler.j0,
                            figureFieldHandler.i1,
                            figureFieldHandler.j1
                    ))
                    figureFieldHandler.haveEvents = false
                }
                FigureFieldHandler.EventType.SELECT_1_FIG -> {
                    ActionManager.addAction(ClickFigure(this))
                    figureFieldHandler.haveEvents = false
                }
            }
        }

        if(buttonHandler.haveEvents) {
            for(clicked in buttonHandler.clicked) {

                when (clicked) {
                    undoBtn -> {
                        ActionManager.addAction(Undo(this))
                    }
                    restartBtn -> {
                        ActionManager.addAction(Restart(this))
                    }
                    ButtonHandler.BACK_BUTTON -> {
                        if(!prepareToClose) {
                            prepareToClose = true
                            ActionManager.clearActions()
                            figureFieldHandler.firstFigureSelected = false
                            ActionManager.addAction(GameClose(puzzleView, stageView, undoBtn, restartBtn, States.MAIN_MENU))
                        }
                    }
                    ButtonHandler.BACKSPACE_BUTTON -> {
                        if(!prepareToClose) {
                            prepareToClose = true
                            ActionManager.clearActions()
                            figureFieldHandler.firstFigureSelected = false
                            ActionManager.addAction(GameClose(puzzleView, stageView, undoBtn, restartBtn, States.MAIN_MENU))
                        }
                    }
                }

            }

            buttonHandler.clicked.clear()
        }

    }

    override fun resize(width: Int, height: Int) {
        puzzleView.setBounds()
        stageView.setBounds()
        restartBtn.setBounds()
        undoBtn.setBounds()
        updateView()
    }

}