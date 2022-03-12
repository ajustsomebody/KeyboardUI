package LibGdxPlayground.uigrid

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

abstract class uiBlock : Sprite {
    // todo: hold-in-click function
    abstract var clickFunction : ((uiBlock) -> Unit)?
    abstract var mouseHoverFunction : ((uiBlock) -> Unit)?
    abstract var keyboardHoverFunction : ((uiBlock) -> Unit)?

    abstract var mouseHoverTimer : Timer
    abstract var keyboardHoverTimer : Timer

    abstract var textOffsetX : Float ; abstract var textOffsetY : Float

    constructor() : super()
    constructor(texture: Texture) : super(texture)
    abstract var font : BitmapFont
    abstract var sizeCache : GlyphLayout
    abstract var text : String

    abstract var unitSize : Int

    abstract var aimedSize : Vector2

    abstract val um : Vector2

    abstract fun copy() : uiBlock


    abstract fun drawText(batch: SpriteBatch, font: BitmapFont)

    abstract fun drawBody(batch : SpriteBatch)

    abstract fun centerText()

    abstract fun uncenterText()
}