/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created November 3, 2009 */
package field.kit.particle.behaviour


/**
 * Applies an repulsion force, that moves a particle away from its neighbours
 * @author Marcus Wendt
 */
class Repel extends RangedBehaviour {
  
  var isSameFlock = true
  
  def apply(p:Particle, dt:Float) {
    ps.space(p, rangeAbs, neighbours)

    val numNeighbours = neighbours.size
    if(numNeighbours < 1) return
    
    var i = 0
	while(i < numNeighbours) {
	  val n = neighbours(i).asInstanceOf[Particle]
      if(n != p && 
         ((isSameFlock && p.flock == n.flock) ||
          (!isSameFlock && p.flock != n.flock))
      ) {
        tmp := n -= p
        val dist = tmp.length
        
        if(dist - p.size < rangeAbs) {
          tmp /= dist
          tmp *= -weight
          p.steer += tmp
        }
        
        // simpler but not much faster
//		tmp *= -weight
//		p.steer += tmp
      }
      i += 1
    } 
  }
}

class HardRepel extends RangedBehaviour {
  def apply(p:Particle, dt:Float) {
    ps.space(p, rangeAbs, neighbours)

    val numNeighbours = neighbours.size
    if(numNeighbours < 1) return
    
    var i = 0
	while(i < numNeighbours) {
	  val n = neighbours(i).asInstanceOf[Particle]
//      if(n != p && 
//         ((isSameFlock && p.flock == n.flock) ||
//          (!isSameFlock && p.flock != n.flock))
//      ) {
        tmp := n -= p
        tmp *= -(1f + weight * 10f)
		p.steer += tmp
//      }
      i += 1
    } 
  }
}




/**
 * Applies an attraction force, that moves a particle towards its neighbours
 * @author Marcus Wendt
 */
class Attract extends RangedBehaviour {
  
  var isSameFlock = true
  
  def apply(p:Particle, dt:Float) {
    ps.space(p, rangeAbs, neighbours)

    val numNeighbours = neighbours.size
    if(numNeighbours <= 1) return
    
    var i = 0
	while(i < numNeighbours) {
	  val n = neighbours(i)
      if(n != p) {
        tmp := n -= p
        val dist = tmp.length
        if(dist - p.size < rangeAbs) {
          tmp /= dist
          tmp *= weight
          p.steer += tmp
        }
      }
      i += 1
    } 
  }
}

/**
 * Applies an attraction force, that moves a particle towards its neighbours
 * @author Marcus Wendt
 */
class Align extends RangedBehaviour {
  
  var isSameFlock = true
  
  def apply(p:Particle, dt:Float) {
    ps.space(p, rangeAbs, neighbours)

    val numNeighbours = neighbours.size
    if(numNeighbours <= 1) return
    
    var i = 0
	while(i < numNeighbours) {
	  val n = neighbours(i)
      if(n != p) {
        tmp := n.asInstanceOf[Particle].velocity
        tmp.normalize
        tmp *= weight
        p.steer += tmp
//        tmp := n -= p
//        val dist = tmp.length - p.size
//        if(dist < rangeAbs) {
//          tmp /= dist
//          tmp *= weight
//          p.steer += tmp
//        }
      }
      i += 1
    } 
  }
}