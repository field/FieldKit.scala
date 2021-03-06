/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created March 31, 2009 */
package field.kit.particle

import field.kit.Logger
import field.kit.math._
import field.kit.colour._

object Particle {
	val UNDEFINED = -1

	// states
	val TRANSITIONING = 0
	val ALIVE = 1
	val DEAD = 2
}

/**
* A single particle from the flock
* @author Marcus Wendt
*/
class Particle extends Vec3 with Logger {
	// set when particle is being added to a flock
	var flock:Flock[_] = null
	var ps:ParticleSystem = null
	var id = 0

	// basic properties
	var age = 0f
	var lifeTime = 10 * 1000f
	var size = 1f

	var velocity = Vec3()
	var steer = Vec3()
	var steerMax = 1f
	var velocityMax = 10f

	// extended properties
	var colour:Colour = _ 
	var colourSteer:HSVA = _ 
	var colourVelocity:HSVA = _
	var colourSteerMax = 0.1f
	var colourVelocityMax = 0.5f

	// internal
	protected val absVelocity = Vec3()
	protected val absColourVelocity = HSVA()

	/** called automatically when the particle is added to the flock */
	def init {
		colour = Colour()
		colourSteer = HSVA()
		colourVelocity = HSVA()
	}

	// perform euler integration
	def update(dt:Float) {
		age += dt
		updatePosition(dt)
		updateColour(dt)
	}

	def updatePosition(dt:Float) {
		// integrate velocity -> position 
		steer.clamp(steerMax)
		velocity += steer
		velocity.clamp(velocityMax)

		// make velocity time invariant
		absVelocity := velocity *= (dt / ps.timeStep)
		this += absVelocity

		velocity *= ps.friction
		steer.zero
	}

	/** integrates colour steering*/
	def updateColour(dt:Float) {
		colourSteer.clamp(colourSteerMax)
		colourVelocity += colourSteer
		colourVelocity.clamp(colourVelocityMax)

		absColourVelocity := colourVelocity *= (dt / ps.timeStep)

		// travel through HSV space
		this.colour.hue = colour.hue + absColourVelocity.h
		this.colour.saturation = colour.saturation + absColourVelocity.s
		this.colour.value = colour.value + absColourVelocity.v
		this.colour.alpha = colour.alpha + absColourVelocity.a

		// travel through RGB space
		//this.colour += (absColourVelocity.x, absColourVelocity.y, absColourVelocity.z, absColourVelocity.w)

		colourVelocity *= ps.friction
		colourSteer.zero
	}

	override def toString = "Particle["+ toLabel +"]"
}
