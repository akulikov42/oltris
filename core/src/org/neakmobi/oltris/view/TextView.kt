package org.neakmobi.oltris.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import org.neakmobi.oltris.renderer.Drawable
import org.neakmobi.oltris.renderer.Renderer

class TextView(
        x: Float, y: Float,
        private var str         : String,
        private var fontSize    : Float = 0.25f,
        private var fontRatio   : Float = 1f,
        private var maxWidth    : Float = -1f,
        colors      : Array<Color>? = null): View(), Drawable {

    private var charWidth  = 0f
    private var charHeight = 0f
    private var cntChar    = 0
    private val chars = Array(MAX_TEXT_VIEW_LENGTH) { CharView() }

    override fun updateBounds() {

        var curX = x
        if(maxWidth != -1f) {
            curX = x + (maxWidth - w) / 2f
        }

        fontSize = h
        fontRatio = (w / cntChar) / h

        charHeight = fontSize
        charWidth  = charHeight * fontRatio

        for(idx in 0 until cntChar)
            chars[idx].setBounds(curX + idx * charWidth, y, charWidth, charHeight)

    }

    companion object CONSTANTS {
        const val MAX_TEXT_VIEW_LENGTH = 128
    }

    init {
        if(!setString(str, colors, x, y))
            setString("ERROR", null, x, y)
    }

    fun setScale(scale: Float) {
        for(idx in chars.indices) {
            chars[idx].setScale(scale)
        }
    }

    fun setString(str: String, colors: Array<Color>? = null, x: Float = this.x, y: Float = this.y): Boolean {

        this.str = str
        if(!calcCntChar(str)) return false

        setBounds(x, y, cntChar * fontSize * fontRatio, fontSize)

        var idx             = 0
        var idxStr          = 0
        val specSymbol      = StringBuilder()

        while(idx < cntChar) {

            if(colors == null || colors.size != cntChar)
                chars[idx].setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a)
            else
                chars[idx].setColor(colors[idx].r, colors[idx].g, colors[idx].b, colors[idx].a)

            chars[idx].isEmpty = false

            when(str[idxStr]) {

                ' '  -> { chars[idx].isEmpty = true }

                '{'  -> {

                    specSymbol.clear()
                    idxStr++

                    while(str[idxStr] != '}')
                        specSymbol.append(str[idxStr++])

                    chars[idx].setRegion(Renderer.getRegion("main_atlas", specSymbol.toString()))

                }

                '.'  -> { chars[idx].setRegion(Renderer.getRegion("main_atlas", "char_point"))       }

                ':'  -> { chars[idx].setRegion(Renderer.getRegion("main_atlas", "char_colon"))       }

                '?'  -> { chars[idx].setRegion(Renderer.getRegion("main_atlas", "char_question"))    }

                ','  -> { chars[idx].setRegion(Renderer.getRegion("main_atlas", "char_comma"))       }

                else -> { chars[idx].setRegion(Renderer.getRegion("main_atlas", str[idxStr].toString()))   }

            }

            idx++
            idxStr++

        }

        for(idxChar in cntChar until MAX_TEXT_VIEW_LENGTH)
            chars[idxChar].isEmpty = true

        return true
    }

    fun setColor(color: Color) {
        for(char in chars) {
            char.setColor(color.r, color.g, color.b, color.a)
        }
    }

    private fun calcCntChar(str: String): Boolean {

        cntChar = 0
        var isSpec = false

        for(idx in str.indices) {

            if(str[idx] == '{') { isSpec = true  }
            if(str[idx] == '}') { isSpec = false }

            if(!isSpec) cntChar++

        }

        return !isSpec

    }

    private fun draw(batch: Batch?) {
        for(idx in 0..cntChar) {
            chars[idx].drawObj(batch)
        }
    }

    override fun drawObj(batch: Batch?) {
        draw(batch)
    }

}