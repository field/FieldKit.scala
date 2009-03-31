/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field.io             **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created March 23, 2009 */
package field.kit.agent.behaviour.motor

import field.kit.agent._

/** performs euler integration of the velocity on the location vector */
class Euler(s:Simulation) extends Behaviour("euler") {
  import field.kit.math._
  
  var velocity:Vec3 = null
  var steer:Vec3 = null
  var location:Vec3 = null
  
  protected val absVelocity = new Vec3
  
  override def init {
  	// get fields
    velocity = current.get[Vec3]("velocity")
    steer = current.get[Vec3]("steer")
    location = current.get[Vec3]("location", new Vec3(s.space.center))    
  }
  
  def apply = {
    val friction = get("friction", 0.97f)
    val steerMax = get("steerMax", 1f)
    val velocityMax = get("velocityMax", 10f)
    
    // update driving force
    steer.clamp(steerMax)
    velocity += steer
    velocity.clamp(velocityMax)
    
    // make velocity time invariant
    absVelocity(velocity) *= (dt / s.timeStep)
    location += absVelocity

    // clean up
    velocity *= friction
    steer.zero
    
    // always continue
    true
  }
}