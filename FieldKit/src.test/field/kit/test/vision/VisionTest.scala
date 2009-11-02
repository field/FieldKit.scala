/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 28, 2009 */
package field.kit.test.vision

/**
 * VM Args: -Djna.library.path=lib/vision
 * Until OpenCV.framework can be compiled as 64bit also use -d32
 */
object VisionTest extends test.Sketch {
  import kit.vision._
  import processing.core._
  import processing.core.PConstants._
  import javax.media.opengl._
  import controlP5._
  
  var index = 0
  var showStage = true
  
  // ui components
  var ui:ControlP5 = _
  var uiWindow:ControlWindow = _
  
  // -- Init -------------------------------------------------------------------
  Logger.level = Logger.FINE
  Vision.start
  
  init(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FULLSCREEN, DEFAULT_AA, {
    ui = new ControlP5(this)
//    ui.setAutoDraw(false)
    
    uiWindow = ui.addControlWindow("UI",100,100,400,200)
    uiWindow.setUpdateMode(ControlWindow.NORMAL)

    val offset = 15
    var i = 0
    def slider(name:String, default:Float) = {
      val w = 200
      val h = 10
      val x = offset
      val y = offset + i * (h + 5)
      i += 1
      
      val slider = ui.addSlider(name, 0f, 1f, default, x, y, w, h)
      slider.setWindow(uiWindow)
      slider
    }
    
    slider("background", Vision.background)
    slider("threshold", Vision.threshold)
    slider("dilate", Vision.dilate)
    slider("erode", Vision.erode)
    slider("contourMin", Vision.contourMin)
    slider("contourMax", Vision.contourMax)
    slider("contourReduce", Vision.contourReduce)
    slider("trackRange", Vision.trackRange)
  })
  
  def render {
    // update
    Vision.update
    
    // render
    background(0)
    
    if(showStage)
      drawStage
    else
      drawBlobs
  }
  
  def drawStage {
    val s = Vision.stage(index)
    val format = if(s.depth == 8) GL.GL_LUMINANCE else GL.GL_BGR
    beginGL
    gl.glPixelZoom(width / s.width.toFloat, height / s.height.toFloat)
    gl.glDrawPixels(s.width, s.height, format, GL.GL_UNSIGNED_BYTE, s.image)
    endGL
  }
  
  def drawBlobs {
    rectMode(PConstants.CENTER)
    pushMatrix
    scale(width / 320f, height / 240f, 1f)
    Vision.blobs filter (_.active == true) foreach { b =>
      val c = 128 + 128 * (b.id / Vision.blobs.size.toFloat)
      stroke(c)
      noFill
      rect(b.bounds.x1, b.bounds.y1, b.bounds.x2, b.bounds.y2)
      
      fill(c)
      rect(b.x, b.y, 10, 10)
    }
    popMatrix
  }
  
  def controlEvent(event:ControlEvent) {
    val value = event.value
    event.label match {
      case "background" => Vision.background = value
      case "threshold" => Vision.threshold = value
      case "dilate" => Vision.dilate = value
      case "erode" => Vision.erode = value
      case "contourMin" => Vision.contourMin = value
      case "contourMax" => Vision.contourMax = value
      case "contourReduce" => Vision.contourReduce = value
      case "trackRange" => Vision.trackRange = value
      case _ =>
    }
  } 
  
  override def keyPressed {
    keyCode match {
      case LEFT =>
        index -= 1
        if(index < 0) index = Vision.Stages.size - 1
      case RIGHT => 
        index += 1
        if(index == Vision.Stages.size) index = 0
      case UP => index = 0
      case _ =>
    }
    
    key match {
      case ' ' => showStage = !showStage
      case _ =>
    }
  }
}