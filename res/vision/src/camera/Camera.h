/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#ifndef CAMERA_H
#define CAMERA_H

namespace field {
	//
	// base class for all camera types
	//
	class Camera
	{
	public:
		Camera();
		~Camera();
		
		virtual int init();
		virtual int start();
		virtual int update();
		virtual int stop();
		virtual int close();
		
		// setters
		void setSize(int width, int height);
		void setFramerate(int fps);
		
		// getters
		int getWidth() { return width; };
		int getHeight() { return height; };
		int getFramerate() { return fps; };
		
		virtual IplImage* getImage(int channel = 0);
		
	protected:
		bool isInitialized;
		bool isStarted;
		bool color;
		int width, height, fps;
		
	private:
		int err;
	};
};
#endif