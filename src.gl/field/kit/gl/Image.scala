/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created May 11, 2009 */
package field.kit.gl

import javax.media.opengl.GL

import field.kit._

/**
 * Companion object for the <code>Image</code> class.
 * Has various helper methods to load and create Images.
 * @author Marcus Wendt
 */
object Image extends Logger {
 	import java.awt.image.BufferedImage
	import java.awt.image.WritableRaster
	import java.net.URL
	import javax.imageio.ImageIO
	import java.nio._

	import scala.collection.mutable.HashMap

	import field.kit.util.Buffer

	private val DEFAULT_USE_CACHE = false

	val cache = new HashMap[URL, Image]

	/** Creates a new Image with the given dimensions and alpha */
	def apply(width:Int, height:Int, alpha:Boolean) = 
		new Image(width, height, if(alpha) Format.ARGB else Format.RGB)

	/** Resolves the given string as URL and returns an Image */
	def apply(file:String) = load(file, DEFAULT_USE_CACHE)

	/** Loads the image from the given URL */
	def apply(file:URL) = load(file, DEFAULT_USE_CACHE)

	/** Resolves the given string as URL and returns an Image */
	def apply(file:String, useCache:Boolean) = load(file, useCache)

	/** Loads the image from the given URL */
	def apply(file:URL, useCache:Boolean) = load(file, useCache)

	/** Takes the given AWT image and returns an Image */
	def apply(awtImage:BufferedImage) = loadBufferedImage(awtImage)

	// -- Loading & Creating -----------------------------------------------------
	/** Resolves the given string as URL and returns an Image */
	def load(file:String, useCache:Boolean):Image = {
		import field.kit.util.Loader
		Loader.resolveToURL(file) match {
			case null => warn("load: Couldnt find file '"+ file +"'"); null
			case url:URL => load(url, useCache)
		}
	}

	/** 
	* Loads the image from the given URL and stores it in the cache 
	*/
	def load(url:URL, useCache:Boolean):Image = {
		var image:Image = null

		if(url == null) { 
			warn("load: No URL given")
			null

		} else if(cache.contains(url) && useCache) {
			fine("reusing", url)
			image = cache(url)

		} else {
			info("loading", url)

			if(url.getFile.toLowerCase.endsWith(".tga"))
				image = loadTGA(url)
			else 
				image = loadBufferedImage(ImageIO.read(url))

			// put image into cache
			if(image != null && useCache) cache(url) = image
		}

		image
	}

	protected def loadTGA(url:URL) = {
		import javax.media.opengl.GL
		import com.sun.opengl.util.texture.spi.TGAImage
		val tga = TGAImage.read(url.openStream)

		val format = if(tga.getGLFormat == GL.GL_BGR) Format.RGB else Format.ARGB
		val image = new Image(tga.getWidth, tga.getHeight, format)

//		warn("Currently not implemented!")

		//    val data = tga.getData
		//
		//    var i = 0
		//    for(y <- 0 until image.height) {
		//      for(x <- 0 until image.width) {
		//        val pixel = 0
		//        pixel += data.get
		//        pixel += data.get
		//        image.pixels(i) = pixel
		//        i += 1
		//      }
		//    }

		image
	}

	/** Creates a FieldKit Image from a Java AWT Image */
	protected def loadBufferedImage(sourceImage:BufferedImage) = {
		import com.sun.opengl.util.ImageUtil

		var width = sourceImage.getWidth
		var height = sourceImage.getHeight
		val alpha = hasAlpha(sourceImage)

		// create image
		val image = new Image(width, height, if(alpha) Format.ARGB else Format.RGB)

		// fill pixel data
		sourceImage.getRGB(0, 0, width, height, image.pixels, 0, width)

		image
	}

	// -- Helpers ----------------------------------------------------------------
	def bitdepth(format:Format.Value) = 
		format match {
			case Image.Format.ARGB => 4
			case Image.Format.RGB => 3
			case Image.Format.GREY => 1
		}

	def hasAlpha(format:Image.Format.Value) = 
		format == Image.Format.ARGB 

	/** Tests wether the given image has an alpha channel */
	def hasAlpha(image:java.awt.Image) = 
		image match {
			case null => false

			case bufImg:BufferedImage =>
				bufImg.getColorModel.hasAlpha

			case _ =>
				import java.awt.image.PixelGrabber
				val pixelGrabber = new PixelGrabber(image, 0, 0, 1, 1, false)
				pixelGrabber.grabPixels
				val colorModel = pixelGrabber.getColorModel
				if(colorModel != null)
					colorModel.hasAlpha
				else
					false
		}

	// -- Constants ------------------------------------------------------------
	/** A list of constants that can be used to set the format of an <code>Image</code> */
	object Format extends Enumeration {
		val ARGB = Value("ARGB")
		val RGB = Value("RGB")
		val GREY = Value("GREYSCALE")
	}
}

/**
 * Lightweight store for raw image data and format description
 * @author Marcus Wendt
 */
class Image(val width:Int, val height:Int, val format:Image.Format.Value) {
	import java.nio.Buffer
	import javax.media.opengl.GL

	/** the image's pixel data */
	var pixels:Array[Int] = new Array[Int](width * height)

	override def toString = "Image("+ format +" "+ width +"x"+ height +")"
	
	def apply(x:Int, y:Int) = {
		var index = y * width + x
		if(index < 0 || index >= pixels.size)
			index = 0
		pixels(index)
	}

	/** @return the alpha value at the specified pixel as normalised float */
	def alpha(x:Int, y:Int) = {
		val colour = apply(x, y)
		val value = format match {
			case Image.Format.GREY => 255
			case Image.Format.RGB => 255
			case _ => (colour >> 24) & 0xff
		}
		value / 255f
	}

	/** @return the red value at the specified pixel as normalised float */
	def red(x:Int, y:Int) = {
		val colour = apply(x, y)
		val value = format match {
			case Image.Format.GREY => colour
			case _ => (colour >> 16) & 0xff
		}
		value / 255f
	}
	
	/** @return the green value at the specified pixel as normalised float */
	def green(x:Int, y:Int) = {
		val colour = apply(x, y)
		val value = format match {
			case Image.Format.GREY => colour
			case _ => (colour >> 8) & 0xff
		}
		value / 255f
	}

	/** @return the blue value at the specified pixel as normalised float */
	def blue(x:Int, y:Int) = {
		val colour = apply(x, y)
		val value = format match {
			case Image.Format.GREY => colour
			case _ => (colour) & 0xff
		}
		value / 255f
	}
}
