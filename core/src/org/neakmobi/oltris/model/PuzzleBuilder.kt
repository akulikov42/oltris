package org.neakmobi.oltris.model

import com.badlogic.gdx.math.MathUtils.ceil
import com.badlogic.gdx.math.MathUtils.log
import org.neakmobi.oltris.model.PuzzleElement.Companion.EL_1
import org.neakmobi.oltris.model.PuzzleElement.Companion.EL_2
import org.neakmobi.oltris.model.PuzzleElement.Companion.EL_3
import org.neakmobi.oltris.model.PuzzleElement.Companion.EL_4
import org.neakmobi.oltris.model.PuzzleElement.Companion.EL_5
import org.neakmobi.oltris.model.PuzzleElement.Companion.EL_6
import org.neakmobi.oltris.model.PuzzleElement.Companion.EL_7
import org.neakmobi.oltris.model.PuzzleElement.Companion.EL_8
import org.neakmobi.oltris.states.Game
import java.util.*
import kotlin.math.sqrt
import kotlin.random.Random

/**
 *
 * Строитель головоломки.
 * Чтобы получить головоломку, необходимо вызвать метод genPuzzle()
 * Для конфигурации параметров построения головоломки можно менять
 * значения открытых полей билдера.
 *
 * Возможно, STAGE_FOR_TUTORIAL стоит перенести сюда
 *
 * @property solveField                 Поле решений
 * @property puzzleField                Поле головоломки
 *
 */

class PuzzleBuilder {

    private var stage = 1

    fun setPuzzle(puzzle: Puzzle) {
        puzzle.set(stage, solveField, puzzleField)
    }

    fun genPuzzle(stage: Int) {
        this.stage = stage
        genSolve()
        randomize()
    }

    private val solveField  = Field()
    private val puzzleField = Field()

    /**
     *
     * Генерация поля решений
     *
     * Выход: solveField
     *
     */
    private fun genSolve() {

        val anchor = Random.nextInt(Game.FIELD_SIZE)
        var forRand: Int
        var tmpType: Int

        for(idxLine in 0 until Game.FIELD_SIZE) {
            for(idxFigure in 0 until Game.FIELD_SIZE) {
                solveField.figure(idxLine, idxFigure).type = (idxLine + idxFigure).rem(Game.CNT_FIGURE_TYPE)
            }

            do {
                forRand = Random.nextInt(Game.FIELD_SIZE / 2)
            } while (forRand == anchor)

            tmpType = solveField.figure(idxLine, forRand).type
            solveField.figure(idxLine, forRand).type = solveField.figure(idxLine, Game.FIELD_SIZE - forRand - 1).type
            solveField.figure(idxLine, Game.FIELD_SIZE - forRand - 1).type = tmpType

        }

        puzzleField.copy(solveField)

    }


    private fun randomize() {

        val els: Array<PuzzleElement>

        when(stage) {
            1 -> {
                els = Array(1) { PuzzleElement() }
                els[0].i0 = 0; els[0].j0 = 0; els[0].moves = EL_1
            }
            2 -> {
                els = Array(2) { PuzzleElement() }
                els[0].i0 = 0; els[0].j0 = 0; els[0].moves = EL_1
                els[1].i0 = 3; els[1].j0 = 3; els[1].moves = EL_3
            }
            3 -> {
                els = Array(3) { PuzzleElement() }
                els[0].i0 = 0; els[0].j0 = 0; els[0].moves = EL_1
                els[1].i0 = 3; els[1].j0 = 3; els[1].moves = EL_3
                els[2].i0 = 3; els[2].j0 = 0; els[2].moves = EL_4
            }
            4 -> {
                els = Array(4) { PuzzleElement() }
                els[0].i0 = 3; els[0].j0 = 0; els[0].moves = EL_2
                els[1].i0 = 3; els[1].j0 = 3; els[1].moves = EL_4
                els[2].i0 = 0; els[2].j0 = 0; els[2].moves = EL_3
                els[3].i0 = 0; els[3].j0 = 3; els[3].moves = EL_1
            }
            5 -> {
                els = Array(4) { PuzzleElement() }
                els[0].i0 = 0; els[0].j0 = 0; els[0].moves = EL_1
                els[1].i0 = 1; els[1].j0 = 3; els[1].moves = EL_3
                els[2].i0 = 3; els[2].j0 = 0; els[2].moves = EL_2
                els[3].i0 = 2; els[3].j0 = 2; els[3].moves = EL_4
            }
            6 -> {
                els = Array(1) { PuzzleElement() }
                els[0].i0 = 0; els[0].j0 = 0; els[0].moves = EL_5
            }
            7 -> {
                els = Array(2) { PuzzleElement() }
                els[0].i0 = 0; els[0].j0 = 0; els[0].moves = EL_5
                els[1].i0 = 0; els[1].j0 = 3; els[1].moves = EL_6
            }
            8 -> {
                els = Array(3) { PuzzleElement() }
                els[0].i0 = 0; els[0].j0 = 0; els[0].moves = EL_5
                els[1].i0 = 0; els[1].j0 = 3; els[1].moves = EL_6
                els[2].i0 = 3; els[2].j0 = 0; els[2].moves = EL_7
            }
            9 -> {
                els = Array(4) { PuzzleElement() }
                els[0].i0 = 0; els[0].j0 = 0; els[0].moves = EL_5
                els[1].i0 = 0; els[1].j0 = 3; els[1].moves = EL_6
                els[2].i0 = 3; els[2].j0 = 0; els[2].moves = EL_7
                els[3].i0 = 3; els[3].j0 = 3; els[3].moves = EL_8
            }
            10 -> {
                els = Array(4) { PuzzleElement() }
                els[0].i0 = 0; els[0].j0 = 0; els[0].moves = EL_6
                els[1].i0 = 0; els[1].j0 = 2; els[1].moves = EL_7
                els[2].i0 = 2; els[2].j0 = 0; els[2].moves = EL_5
                els[3].i0 = 2; els[3].j0 = 2; els[3].moves = EL_8
            }
            else -> {

                val cntElements: Int = when(stage) {
                    in 11       ..  15       -> 3
                    in 15       ..  20       -> 4
                    in 21       ..  30       -> 5
                    in 31       ..  45       -> 6
                    in 46       ..  60       -> 7
                    in 61       ..  80       -> 8
                    in 80       ..  160      -> 9
                    in 161      ..  300      -> 10
                    in 301      ..  600      -> 11
                    in 601      ..  1200     -> 12
                    in 1201     ..  3600     -> 13
                    in 3601     ..  12000    -> 14
                    in 12001    ..  60000    -> 15
                    else                     -> 16
                }

                els = Array(cntElements) { PuzzleElement() }

                //
                // Размещение элементов специально оставлено таким образом,
                // чтобы дать игроку определенную стратегию для решения головломки
                // иначе она очень лютая
                //
                for(idx in els.indices) {
                    els[idx].i0 = idx / 4
                    els[idx].j0 = idx % 4
                }
            }
        }


        //
        // На поле устанавливаются в обратном порядке,
        // тем самым при решении первым будет левый
        // нижний прямоугольник
        //
        //
        for(idx in els.indices) {
            els[els.size - idx - 1].set(puzzleField)
        }

    }


}