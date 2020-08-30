package org.neakmobi.oltris.model

import kotlin.random.Random

class PuzzleElement {

    init { gen() }

    var i0 = 0
        set(value) {

            if(value < 0) { field = 0; return }
            if(value > 4) { field = 3; return }

            field = value

        }
    var j0 = 0
        set(value) {

            if(value < 0) { field = 0; return }
            if(value > 4) { field = 3; return }

            field = value

        }

    lateinit var moves: Array<Move>

    fun set(field: Field) {
        for(move in moves) {
            field.swapFigure(i0 + move.i0, j0 + move.j0, i0 + move.i1, j0 + move.j1, true)
        }
    }


    /**
     *
     * Сложность элемента от 0 до 1f
     *
     */
    fun gen() {

        val idxEl = Random.nextInt(0, EL.size)
        moves = EL[idxEl]

    }

    companion object {

        /**

         O # #
             #
             #

         */
        val EL_1 = arrayOf(
                Move(2, 0, 2, 1),
                Move(2, 1, 2, 2),
                Move(2, 2, 1, 2),
                Move(1, 2, 0, 2)
        )

        /**

          #
          #
          # # O

         **/
        val EL_2 = arrayOf(
                Move(0, 2, 0, 1),
                Move(0, 1, 0, 0),
                Move(0, 0, 1, 0),
                Move(1, 0, 2, 0)
        )

        /**

         # # O
         #
         #

         */
        val EL_3 = arrayOf(
                Move(2, 2, 2, 1),
                Move(2, 1, 2, 0),
                Move(2, 0, 1, 0),
                Move(1, 0, 0, 0)
        )

        /**

             #
             #
         O # #

         **/
        val EL_4 = arrayOf(
                Move(0, 0, 0, 1),
                Move(0, 1, 0, 2),
                Move(0, 2, 1, 2),
                Move(1, 2, 2, 2)
        )

        /**

           # #
           #
         O #

         **/
        val EL_5 = arrayOf(
                Move(0, 0, 0, 1),
                Move(0, 1, 1, 1),
                Move(1, 1, 2, 1),
                Move(2, 1, 2, 2)
        )

        /**

         # #
           #
           # O

         */

        val EL_6 = arrayOf(
                Move(0, 2, 0, 1),
                Move(0, 1, 1, 1),
                Move(1, 1, 2, 1),
                Move(2, 1, 2, 0)
        )

        /**

         O
         # # #
             #

         **/

        val EL_7 = arrayOf(
                Move(2, 0, 1, 0),
                Move(1, 0, 1, 1),
                Move(1, 1, 1, 2),
                Move(1, 2, 0, 2)
        )

        /**

             #
         # # #
         O

         **/
        val EL_8 = arrayOf(
                Move(0, 0, 1, 0),
                Move(1, 0, 1, 1),
                Move(1, 1, 1, 2),
                Move(1, 2, 2, 2)
        )

        val EL = arrayOf(EL_1, EL_2, EL_3, EL_4, EL_5, EL_6, EL_7, EL_8)

    }

}