/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 24, 2009 */
package field.kit.gl.scene.shape

import gl.scene._
import math._

/** 
 * Companion object to class <code>Quad</code>
 */
object Quad extends Enumeration {
  val TOP_LEFT = Value
  val CENTER = Value
  
  /** Creates a new default <code>Quad</code> */
  def apply() = 
    new Quad("Quad", new Vec3, 1f, 1f)
  
  def apply(width:Float, height:Float) = 
    new Quad("Quad", new Vec3, width, height)
  
  def apply(center:Vec3, width:Float, height:Float) = 
    new Quad("Quad", center, width, height)
  
  def apply(name:String, width:Float, height:Float) = 
    new Quad(name, new Vec3, width, height)
}


/** 
 * A quadliteral mesh, often used for billboards, shaders, etc
 */
class Quad(name:String, 
           protected var _center:Vec3,
           protected var _width:Float, protected var _height:Float) 
           extends Mesh(name) {
  
  import javax.media.opengl.GL
  import field.kit.util.Buffer
  
  // TODO could make a more convenient method for that
  data.indexModes(0) = IndexMode.QUADS
  var mode = Quad.CENTER
  init(_width, _height)
           
  /**
   * 
   */
  def init(width:Float, height:Float) {
    this._width = width
    this._height = height
    
    // -- Vertices -------------------------------------------------------------
    val vertices = data.allocVertices(4)
    val hw = width * 0.5f
    val hh = height * 0.5f
    vertices.clear
    mode match {
      case Quad.TOP_LEFT =>
        vertices put 0 put height put 0
        vertices put width put height put 0
        vertices put width put height put 0
        vertices put 0 put height put 0
      
      case Quad.CENTER => 
        vertices put -hw put hh put 0
        vertices put hw put hh put 0
        vertices put hw put -hh put 0
        vertices put -hw put -hh put 0
	  }
    
    // -- Texture Coordinates --------------------------------------------------
    val textureCoords = data.allocTextureCoords(4)
    
    textureCoords put 0f put 0f
    textureCoords put 1f put 0f
    textureCoords put 1f put 1f
    textureCoords put 0f put 1f

    // -- Normals --------------------------------------------------------------
    val normals = data.allocNormals(4)
    for(i <- 0 until 4)
      normals put 0 put 0 put 1
    
    // -- Indices --------------------------------------------------------------
    val indices = data.allocIndices(6)
    indices put Array(0, 1, 2, 0, 2, 3)
  }
  
  // -- Getters ----------------------------------------------------------------
  def width = _width
  def height = _height
}
