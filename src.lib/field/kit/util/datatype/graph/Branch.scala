/*                                                                            *\
**           _____  __  _____  __     ____     FieldKit                       **
**          / ___/ / / /____/ / /    /    \    (c) 2009, field                **
**         / ___/ /_/ /____/ / /__  /  /  /    http://www.field.io            **
**        /_/        /____/ /____/ /_____/                                    **
\*                                                                            */
/* created April 15, 2009 */
package field.kit.util.datatype.graph

/** 
 * a special node that has a number of children
 * @author Marcus Wendt
 */
trait Branch[T <: Node] {
  import scala.collection.mutable.ArrayBuffer
  var children = new ArrayBuffer[T]
  
  def +=(child:T) = children += child
  def -=(child:T) = children -= child
}