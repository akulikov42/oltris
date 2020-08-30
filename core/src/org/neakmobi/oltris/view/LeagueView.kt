package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import org.neakmobi.oltris.renderer.Drawable
import org.neakmobi.oltris.renderer.Renderer

class LeagueView(x: Float, y: Float, w: Float, h: Float): View(), Drawable {

    private val easyView = TextView(
            x,
            y + 2 * h / 3,
            "EASY",
            h / 3
    )
    private val hardView = TextView(
            x + 3 * w / 4,
            y + 2 * h / 3,
            "HARD",
            h / 3
    )

    private val background = ImageView(
            x,
            y + h / 3,
            w,
            h / 3
    )

    private val nextLeagueView = StageView(
            x,
            y,
            w,
            h / 3,
            "NEXT LEAGUE:"
    )


    var partMargin = w / 126
    var partWidth = (w - partMargin * 7) / 15
    val leaguePart = Array<ImageView>(14) { idx -> ImageView(
            x + partMargin * (idx + 1) + partWidth * idx,
            y + partMargin + h / 3,
            partWidth,
            h / 3 - partMargin * 2
    ) }

    private var currLeague = 0
    private var nextLeague = 0

    init {
        setBounds(x, y, w, h)
        background.setImage(Renderer.getRegion("main_atlas", "league_background"))
        for(idx in leaguePart.indices)
            leaguePart[idx].setImage(Renderer.getRegion("main_atlas", "league_filler"))
    }

    fun set(stage: Int) {
        when(stage) {
            in 0        ..  10       -> { currLeague = 0;  nextLeague = 10    }
            in 11       ..  15       -> { currLeague = 1;  nextLeague = 15    }
            in 15       ..  20       -> { currLeague = 2;  nextLeague = 20    }
            in 21       ..  30       -> { currLeague = 3;  nextLeague = 30    }
            in 31       ..  45       -> { currLeague = 4;  nextLeague = 45    }
            in 46       ..  60       -> { currLeague = 5;  nextLeague = 60    }
            in 61       ..  80       -> { currLeague = 6;  nextLeague = 80    }
            in 80       ..  160      -> { currLeague = 7;  nextLeague = 160   }
            in 161      ..  300      -> { currLeague = 8;  nextLeague = 300   }
            in 301      ..  600      -> { currLeague = 9;  nextLeague = 600   }
            in 601      ..  1200     -> { currLeague = 10; nextLeague = 1200  }
            in 1201     ..  3600     -> { currLeague = 11; nextLeague = 3600  }
            in 3601     ..  12000    -> { currLeague = 12; nextLeague = 12000 }
            in 12001    ..  60000    -> { currLeague = 13; nextLeague = 60000 }
            else                     -> { currLeague = 14; nextLeague = -1    }
        }
        if(nextLeague == -1) {
            val colorsForDollars = arrayOf(
                    Color.ROYAL,
                    Color.ROYAL,
                    Color.ROYAL,
                    Color.BLACK,
                    Color.BLACK,
                    Color.BLACK,
                    Color.BLACK,
                    Color.BLACK,
                    Color.BLACK,
                    Color.BLACK,
                    Color.BLACK,
                    Color.BLACK,
                    Color.BLACK,
                    Color.ROYAL,
                    Color.ROYAL,
                    Color.ROYAL
            )
            nextLeagueView.set("{dollar}{dollar}{dollar} BIG BOSS {dollar}{dollar}{dollar}", colorsForDollars)
        } else nextLeagueView.set(nextLeague)

    }

    override fun updateBounds() {
        easyView.setBounds(
                x,
                y + 2 * h / 3,
                w / 4,
                h / 3
        )
        hardView.setBounds(
                x + 3 * w / 4,
                y + 2 * h / 3,
                w / 4,
                h / 3
        )
        background.setBounds(x, y + h / 3, w, h / 3)
        nextLeagueView.setBounds(x, y, w, h / 3)
        partMargin = w / 126
        partWidth = (w - partMargin * 7) / 15
        for(idx in leaguePart.indices)
            leaguePart[idx].setBounds(x + partMargin * (idx + 1) + partWidth * idx, y + partMargin + h / 3)
    }

    override fun drawObj(batch: Batch?) {
        easyView.drawObj(batch)
        hardView.drawObj(batch)
        background.drawObj(batch)
        nextLeagueView.drawObj(batch)
        for(idx in 0 until currLeague)
            leaguePart[idx].drawObj(batch)
    }
}