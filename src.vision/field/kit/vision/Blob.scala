/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 28, 2009 */
package field.kit.vision

/**
 * Represents a single tracked object in the Vision library
 * @author Marcus Wendt
 */
class Blob(val id:Int) extends field.kit.math.Vec3 {
  import field.kit.math.geometry.AABR
  import field.kit.util.Buffer
  
  var active = false
  val bounds = AABR()
  val contour = Buffer.float(Vision.CONTOUR_DATA_MAX)
  var contourPoints = 0
}
