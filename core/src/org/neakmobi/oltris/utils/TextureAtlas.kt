package org.neakmobi.oltris.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.XmlReader

class TextureAtlas(atlasName: String, descriptionName: String) {

    private var width = 0
    private var height = 0
    private var cntFrames = 0

    private var texture = Texture(Gdx.files.internal(atlasName))
    private var regions = mutableMapOf<String, TextureRegion>()

    init {
        val xmlParser       = XmlReader()
        val rootElement     = xmlParser.parse(Gdx.files.internal(descriptionName))
        val description     = rootElement.getChildByName("description")

        cntFrames  = description.getAttribute("cntFrames").toInt()
        width      = description.getAttribute("x_res").toInt()
        height     = description.getAttribute("y_res").toInt()

        var nameRegion: String
        var x: Int; var y: Int
        var w: Int; var h: Int

        var nowFrame: XmlReader.Element
        val frameIterator = rootElement.getChildrenByName("frame").iterator()
        while(frameIterator.hasNext()) {

            nowFrame    = frameIterator.next()
            nameRegion  = nowFrame.get("name")
            x           = nowFrame.getInt("x")
            y           = nowFrame.getInt("y")
            w           = nowFrame.getInt("w")
            h           = nowFrame.getInt("h")
            regions[nameRegion] = TextureRegion(texture, x, y, w, h)

        }

    }

    fun getRegion(name: String): TextureRegion? = regions[name]

}