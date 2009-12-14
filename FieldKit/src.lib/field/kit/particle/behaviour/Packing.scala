/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created December 13, 2009 */
package field.kit.particle.behaviour

import field.kit.particle._

import field.kit.math._
import field.kit.math.Common._
import field.kit.math.packing._
import field.kit.math.geometry._

import scala.collection.mutable.HashMap

abstract class PackingBehaviour[T] extends Behaviour {
  var weight = 0.1f
  
  // temp
  protected var tmp = Vec3()
}

class ImageMapPacking extends PackingBehaviour[Rect] {

  var minDist = 3f 
  var minArea = 25f
  
  val position = Vec3()
  
  protected var _image:String = null
  
  def image = _image
  
  /** Sets the image to use for the shape map */
  def image_=(value:String) {
    import field.kit.util.Loader
    import javax.imageio.ImageIO
    
    this._image = value
    
    val url = Loader.resolveToURL(value)
    val bi = ImageIO.read(url)
    packer.map = new RectanglePacker.BufferedImageMap(bi, threshold)
    
    // set packer to use the map
    packer.mode = RectanglePacker.Mode.VerticalUpUsingMap
    
    reset
  }
  
  protected var _threshold = 0.5f
  
  def threshold = _threshold
  
  /** Sets the threshold to use in the shape map */
  def threshold_=(value:Float) {
    this._threshold = value
    
    if(packer.map != null) { 
      packer.map.asInstanceOf[RectanglePacker.BufferedImageMap].threshold = value
      reset
    }
  }
  
  protected val targets = new HashMap[Int,Rect]
  protected var packer = new RectanglePacker
  protected var minDistSq = 0f
  
  protected var resetRequested = false
  def reset = resetRequested = true
  
  protected def doReset {
    info("setting up targets and packer")
        
    // set packing rect
    packer.rect.x1 = ps.space.toAbsolute(position.x)
    packer.rect.y1 = ps.space.toAbsolute(position.y)
    
    packer.minArea = minArea
    
    targets.clear
    packer.clear
    packer.init
    
    resetRequested = false
  }
  
  override def prepare(dt:Float) {  
    if(resetRequested)
      doReset
    
    minDistSq = minDist * minDist
    
    // remove invalidated targets
    for(p <- flock.removed) {
      targets.get(p.id) match {
        case Some(r:Rect) =>
          packer -= r
          targets -= p.id
        case None =>
      }
    }
    
    // add target
    for(p <- flock.particles) {
      if(!targets.contains(p.id)) {
        val r = new Rect(100, 100, p.size, p.size)
        targets(p.id) = r
        packer += r
        packer.update
      }
    }
  }

  def apply(p:Particle, dt:Float) {
    targets.get(p.id) match {
      case Some(r:Rect) =>
        tmp.x = r.centerX
        tmp.y = r.centerY

      case None => return
    }
      
    val d = tmp.distanceSquared(p)
    
    if(d > minDistSq) {
      tmp -= p
      tmp.normalize
      tmp *= weight
      p.steer += tmp
    } 
  }
}



