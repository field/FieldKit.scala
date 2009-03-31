/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

/** base class for all geometric objects in the scene-graph */
abstract class Geometry(name:String) extends Spatial(name) {
  import field.kit._
  import field.kit.math._
  import java.nio.FloatBuffer
  
  var colour = new Colour(1,1,1)
  var vertices:FloatBuffer = null
  var texCoords:FloatBuffer = null
  var colours:FloatBuffer = null
  var size = 0
  
  def allocate(size:Int) {
    import field.kit.util.BufferUtil
    this.size = size
    vertices = BufferUtil.vec3(size)
    texCoords = BufferUtil.vec2(size)
    colours = BufferUtil.colour(size)
    solidColour(colour)
  }
  
  override def renderPre {
    super.renderPre
    gl.glColor4f(colour.r, colour.g, colour.b, colour.a)
  }
  
  // TODO add renderstate management!
  def applyStates {
    // TODO implement this
  }
  
  def disableStates {
    // TODO implement this
  }
  
  def addRenderState() {
    // TODO implement this
  }
  
  def removeRenderState() {
    // TODO implement this
  }
  
  def solidColour(c:Colour) {
    colour.set(c)
    if(colours!=null) {
      colours.clear
      for(i <- 0 until colours.capacity/4) {
        colours.put(c.r)
        colours.put(c.g)
        colours.put(c.b)
        colours.put(c.a)
      }
      colours.rewind
    }
  }
  
  def randomizeColours {
    colours.clear
    for(i <- 0 until colours.capacity/4) {
      colours.put(Random())
      colours.put(Random())
      colours.put(Random())
      colours.put(1f)
    }
    colours.rewind
  }
}