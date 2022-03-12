package LibGdxPlayground.uigrid

import LibGdxPlayground.BMPfont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

data class xy(val y: Int, val x: Int)
enum class Options
{
    clamp, wrap
}
/**
 * Now, a few stuff to document here: The viewport should not resize stuff to fill the screen
 * on its own, else it breaks the stuff down.
 *
 * the convention for the block placement is in x, y
 */
class UiGrid {
     // the position of the blocks are independent of the Grid, it is Absolute
    private var blocks = mutableListOf<Pair<xy, uiBlock>>()

    var cursorX = 0 ; var cursorY = 0

    var bottomleftX = 0f ; var bottomleftY = 0f
    var width = 8f ; var height = 8f ; var distanceX = 1f ; var distanceY = 1f
    constructor(width: Float, height: Float, bottomleftX : Float, bottomleftY: Float) {
        this.width = width ; this.height = height
        this.bottomleftX = bottomleftX ; this.bottomleftY = bottomleftY
    }

    fun addBlock(pos: xy, block: uiBlock) {
        blocks.add(pos to block)
    }
    fun removeBlock(block: uiBlock)
    {
        blocks.removeIf{it.second == block}
    }
    // assuming that the sizes are equal
    fun positionBlocks()
    {
        var maxWidth = 0f ; var maxHeight = 0f
        var maxY = 0 ; var maxX = 0
        try {
            maxY = blocks.maxByOrNull { it.first.y }!!.first.y
            maxX = blocks.maxByOrNull { it.first.x }!!.first.x
            maxWidth = blocks.maxByOrNull { it.second.aimedSize.x }!!.second.aimedSize.x
            maxHeight = blocks.maxByOrNull { it.second.aimedSize.y }!!.second.aimedSize.y
        } catch ( e: Exception) { throw Error("no blocks were added\n\n ...or some weird shit happened") }
        // if(centerThem) leftmargin = (width - (maxwidth * maxcol-1))/2
        // if(overrideMaxRowCOL) maxrow = rows ; maxcol = cols
        for(i in blocks)
        {
            i.second.x = (i.first.x * maxWidth) + bottomleftX
            i.second.y = (i.first.y * maxHeight) + bottomleftY
        }
    }

    fun draw(batch: SpriteBatch)
    {
        for(i in blocks)
        {
            i.second.drawBody(batch)
            i.second.drawText(batch, BMPfont)
        }
    }

    fun updateKeyboardHeld(delta: Float)
    {
        getBlockAt(cursorX, cursorY)?.keyboardHoverTimer?.update(delta)
    }
    fun press()
    {
        blocks.first { it.first.x == cursorX && it.first.y == cursorY }
            .apply { second.clickFunction?.invoke(this.second) }
    }
    fun moveCursorVertical(up: Boolean, options: Options = Options.wrap)
    {
        getBlockAt(cursorX, cursorY)?.keyboardHoverTimer?.reset()?.disable()

        if(up)
        {
            var _maxY = 0
            var _minY = 0
            try{
                _maxY = blocks.maxByOrNull { if(it.first.y != cursorY) Int.MIN_VALUE else it.first.x }!!.first.x
                _minY = blocks.minByOrNull { if(it.first.y != cursorY) Int.MAX_VALUE else it.first.x }!!.first.x
            } catch (e: Error) {throw Error("There are no blocks")}
            if(cursorY == _maxY && options == Options.wrap)
            {
                cursorY = _minY
            }
            else if(cursorY == _maxY && options == Options.clamp)
            {
                cursorY = _maxY
            }
            else
            {
                cursorY++
            }

        }
        else
        {
            var _maxY = 0
            var _minY = 0
            try{
                _maxY = blocks.maxByOrNull { if(it.first.y != cursorY) Int.MIN_VALUE else it.first.x }!!.first.x
                _minY = blocks.minByOrNull { if(it.first.y != cursorY) Int.MAX_VALUE else it.first.x }!!.first.x
            } catch (e: Error) {throw Error("There are no blocks")}
            if(cursorY == _minY && options == Options.wrap)
            {
                cursorY = _maxY
            }
            else if(cursorY == _minY && options == Options.clamp)
            {
                cursorY = _minY
            }
            else
            {
                cursorY--
            }
        }

    }
    fun moveCursorHorizontal(right: Boolean, options: Options = Options.wrap)
    {
        getBlockAt(cursorX, cursorY)?.keyboardHoverTimer?.reset()?.disable()
        if(right)
        {
            var _maxX = 0
            var _minX = 0
            try{
                _maxX = blocks.maxByOrNull { if(it.first.y != cursorY) Int.MIN_VALUE else it.first.x }!!.first.x
                _minX = blocks.minByOrNull { if(it.first.y != cursorY) Int.MAX_VALUE else it.first.x }!!.first.x
            } catch (e: Error) {throw Error("There are no blocks")}
            if(cursorX == _maxX && options == Options.wrap)
            {
                cursorX = _minX
            }
            else if(cursorX == _maxX && options == Options.clamp)
            {
                cursorX = _maxX
            }
            else
            {
                cursorX++
            }

        }
        else
        {
            var _maxX = 0
            var _minX = 0
            try{
                _maxX = blocks.maxByOrNull { if(it.first.y != cursorY) Int.MIN_VALUE else it.first.x }!!.first.x
                _minX = blocks.minByOrNull { if(it.first.y != cursorY) Int.MAX_VALUE else it.first.x }!!.first.x
            } catch (e: Error) {throw Error("There are no blocks")}
            if(cursorX == _minX && options == Options.wrap)
            {
                cursorX = _maxX
            }
            else if(cursorX == _minX && options == Options.clamp)
            {
                cursorX = _minX
            }
            else
            {
                cursorX--
            }
        }
    }

    fun getBlockAt(x: Int, y: Int): uiBlock?
    {
        return blocks.firstOrNull { it.first.x == x && it.first.y == y }?.second
    }

    fun calculateInitialCursorPosition()
    {
        var abc = blocks.minByOrNull { it.first.x + it.first.y }!!.first
        cursorX = abc.x
        cursorY = abc.y
    }




}

/**
 * u need to manually activate this
 */
data class Timer(var function: ()->Unit, var time: Float = 0f, var end: Float = 1f, var loop: Boolean,
                 var overshoot: Boolean, var disableFunction : ()->Unit = {})
{
    fun update(delta: Float)
    {
        if(!active) return
        time += delta
        if(surpass())
        {
            function()
            if(loop) time = 0f
            else if(!overshoot)
            {
                time = end
                disable()
            }
        }
    }
    fun surpass() = time >= end
    fun reset() : Timer{ time = 0f ; return this }
    var active = false
    fun enable() : Timer{ active = true ; return this}
    fun disable() : Timer { active = false ; disableFunction();return this}

}