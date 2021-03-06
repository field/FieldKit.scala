/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created November 3, 2009 */
package field.kit.particle.behaviour

import field.kit.particle._

/**
 * Base class for all flocking behaviours
 */
abstract class BaseFlockingBehaviour extends Behaviour {
  import field.kit.math.Common._
  import field.kit.math._
  import scala.collection.mutable.ArrayBuffer
  
  var range = 0.01f // [0,1]
  var weight = 1f // [0, 1]
  var isAppliedToOwnFlock = true
  
  var rangeAbs = 0f  
  protected val tmp = Vec3()
  //protected val neighbours = new ArrayBuffer[Vec]
  
  override def prepare(dt:Float) {
    rangeAbs = ps.space.toAbsolute(range)
  }
        
  /**
   * 1. do not apply to itself
   * 2. apply only from particles in the same flock
   */ 
  final def doesApply(p:Particle, n:Particle) =
    p != n && ((p.flock == n.flock) == isAppliedToOwnFlock)
}

// -----------------------------------------------------------------------------

/**
 * Applies a repulsion force, that moves a particle away from its neighbours
 * @author Marcus Wendt
 */
class FlockRepel extends BaseFlockingBehaviour {
  def apply(p:Particle, dt:Float) {
    //ps.space(p, rangeAbs, neighbours)
    val neighbours = ps.space(p, rangeAbs)
    
    var i = 0
	while(i < neighbours.size) {
	  val n = neighbours(i).asInstanceOf[Particle]
      i += 1
      
      if(doesApply(p, n)) {
        tmp := n -= p
        tmp.normalize
        tmp *= -weight
        p.steer += tmp
      }
    } 
  }
}

/** A hard repulsion force that applies to all particles from all flocks */
class FlockRepelHard extends BaseFlockingBehaviour {
  def apply(p:Particle, dt:Float) {
    //ps.space(p, rangeAbs, neighbours)
    val neighbours = ps.space(p, rangeAbs)
    
    var i = 0
	while(i < neighbours.size) {
	  val n = neighbours(i).asInstanceOf[Particle]
      i += 1
      
      if(doesApply(p, n)) {
        tmp := n -= p
        tmp *= -weight
        p.steer += tmp
      }
    } 
  }
}

// -----------------------------------------------------------------------------

/**
 * Applies an attraction force, that moves a particle towards its neighbours
 * @author Marcus Wendt
 */
class FlockAttract extends BaseFlockingBehaviour {
  def apply(p:Particle, dt:Float) {
    //ps.space(p, rangeAbs, neighbours)
    val neighbours = ps.space(p, rangeAbs)
    
    var i = 0
	while(i < neighbours.size) {
	  val n = neighbours(i).asInstanceOf[Particle]
      i += 1

      if(doesApply(p, n)) {
        tmp := n -= p
        tmp.normalize
        tmp *= weight
        p.steer += tmp
      }
    } 
  }
}

/** A hard repulsion force that applies to all particles from all flocks */
class FlockAttractHard extends BaseFlockingBehaviour {
  def apply(p:Particle, dt:Float) {
    //ps.space(p, rangeAbs, neighbours)
    val neighbours = ps.space(p, rangeAbs)
    
    var i = 0
	while(i < neighbours.size) {
	  val n = neighbours(i).asInstanceOf[Particle]
      i += 1
      
      if(doesApply(p, n)) {
        tmp := n -= p
        tmp *= weight
        p.steer += tmp
      }
    } 
  }
}

// -----------------------------------------------------------------------------

/**
 * Applies an alignment force making particles steer into the same direction as their neighbours
 * @author Marcus Wendt
 */
class FlockAlign extends BaseFlockingBehaviour {
  def apply(p:Particle, dt:Float) {
    //ps.space(p, rangeAbs, neighbours)
    val neighbours = ps.space(p, rangeAbs)
    
    var i = 0
	while(i < neighbours.size) {
	  val n = neighbours(i).asInstanceOf[Particle]
      i += 1
      
      if(doesApply(p, n)) {
        tmp := n.asInstanceOf[Particle].velocity
        tmp.normalize
        tmp *= weight
        p.steer += tmp
      }
    } 
  }
}
