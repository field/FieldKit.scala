/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene

import field.kit.util.datatype.graph.Node

/** base class for all geometric objects in the scene-graph */
abstract class Geometry(name:String) extends Spatial(name) {
  import field.kit._
  import field.kit.math._
  import field.kit.gl.render.RenderState
  import field.kit.util.BufferUtil
  
  import java.nio.FloatBuffer
  import field.kit.util.datatype.collection.ArrayBuffer
  
  var colour = new Colour(Colour.WHITE)
  
  // TODO consider switching to a datastructure with a predictable order
  var states = new ArrayBuffer[RenderState]
  
  var vertices:FloatBuffer = _
  var normals:FloatBuffer = _
  var coords:FloatBuffer = _
  var colours:FloatBuffer = _
  
  /** the maximum number of vertices this geometry object can hold */
  var capacity = 0
  
  /** the number of actual vertices in the buffer */
  var vertexCount = 0

  // Buffer Management
  def allocate(capacity:Int) {
    this.capacity = capacity
    vertices = allocateVertices
    normals = allocateNormals
    coords = allocateCoords
    colours = allocateColours
    solidColour(colour)
  }
  
  protected def allocateVertices = BufferUtil.vec3(capacity)
  protected def allocateNormals = BufferUtil.vec3(capacity)
  protected def allocateCoords = BufferUtil.vec2(capacity)
  protected def allocateColours = BufferUtil.colour(capacity)
  
  def clear {
    vertexCount = 0
    vertices.clear
    coords.clear
    colours.clear
  }
  
  // -- Render States ----------------------------------------------------------
  protected def enableStates = {
//    states foreach(s => 
//    if(s.isEnabled) s.enable(this)
//  )
    var i=0
    while(i < states.size) {
      val s = states(i)
      if(s.isEnabled) s.enable(this)
      i += 1
    }
  }
  
  protected def disableStates = {
//    states foreach(s => 
//    if(s.isEnabled) s.disable(this)
//  )
	var i=0
    while(i < states.size) {
      val s = states(i)
      if(s.isEnabled) s.disable(this)
      i += 1
    }                              
  }
  
  /** @return the first <code>RenderState</code> that matches the given <code>Class</code> or null */
  def state[T <: RenderState](clazz:Class[T]):T = {
    states find (_.getClass == clazz) match {
      case Some(r:RenderState) => r.asInstanceOf[T]
      case _ => null.asInstanceOf[T]
    }
  }
  
  // -- Colours ----------------------------------------------------------------
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