package LibGdxPlayground.uigrid

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

class uiBlockNinepatch : uiBlock {
    override var clickFunction : ((uiBlock) -> Unit)? = null
    override var mouseHoverFunction: ((uiBlock) -> Unit)? = null
    override var keyboardHoverFunction: ((uiBlock) -> Unit)? = null

    override var mouseHoverTimer =
        Timer({keyboardHoverFunction?.invoke(this)}, 0f, 1f, false, true)
    override var keyboardHoverTimer: Timer =
        Timer({mouseHoverFunction?.invoke(this)}, 0f, 1f, false, true)
    override var textOffsetX = 0f ; override var textOffsetY = 0f

    override var font : BitmapFont
    override var sizeCache = GlyphLayout()
    override var text = ""
        get() = field
        set(value) {
            field = value
            sizeCache.setText(font, value)
            if(textCentered) centerText()
        }

    override var unitSize = 1

    override var aimedSize = Vector2()
    // for scaling it up to the aimed size
    override val um : Vector2 // full name is getUpscaleToAimedSizeMultiplier
        get() = Vector2(aimedSize.x / (width/unitSize), aimedSize.y / (height/unitSize))

    var patch : NinePatch
    constructor(patch: NinePatch,
                aimedWidth: Float, aimedHeight: Float,
                clickFunction : ((uiBlock) -> Unit)?,
                hoverFunction: ((uiBlock) -> Unit)?,
                keyboardHoverFunction: ((uiBlock) -> Unit)?,

                unitInPixels: Int, font: BitmapFont
    ) : super()
    {
        this.mouseHoverFunction = hoverFunction
        this.clickFunction = clickFunction
        this.keyboardHoverFunction = keyboardHoverFunction

        this.patch = patch
        aimedSize.set(aimedWidth, aimedHeight)
        unitSize = unitInPixels
        setSize(regionWidth.toFloat(), regionHeight.toFloat())
        this.font = font
        centerText()
    }
    override fun copy() : uiBlockNinepatch
    {
        return uiBlockNinepatch(
            patch, aimedSize.x, aimedSize.y,
            clickFunction, mouseHoverFunction, keyboardHoverFunction,
            unitSize, font).apply {
            this.text = this@uiBlockNinepatch.text
            this.textOffsetX = this@uiBlockNinepatch.textOffsetX
            this.textOffsetY = this@uiBlockNinepatch.textOffsetY
        }
    }

    override fun drawText(batch: SpriteBatch, font: BitmapFont)
    {
        font.draw(batch, text, x + textOffsetX, y + textOffsetY)
    }

    override fun drawBody(batch : SpriteBatch)
    {   // don't use offsets for the blocks
        patch.draw(batch, x, y, 0f, 0f,
            (width*um.x)*unitSize, (height*um.y)*unitSize,
            scaleX, scaleY, rotation)

    }

    override fun centerText()
    {
        textCentered = true
        textOffsetX = aimedSize.x/2 - sizeCache.width/2
        textOffsetY = aimedSize.y/2f + font.capHeight/2
    }
    override fun uncenterText()
    {
        textCentered = false
        textOffsetX = 0f
        textOffsetY = 0f
    }
    private var textCentered = false
}