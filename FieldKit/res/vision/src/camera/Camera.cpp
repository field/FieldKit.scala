/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#include "Camera.h"

namespace field {
	// -------------------------------------------------------------------------
	// CON- & DESTRUCTORS
	// -------------------------------------------------------------------------
	Camera::Camera() {
		isInitialized = false;
		isStarted = false;
		width = VISION_DEFAULT_WIDTH;
		height = VISION_DEFAULT_HEIGHT;
		fps = VISION_DEFAULT_FPS;
	}
	
	Camera::~Camera() {
		this->close();
	}
	
	// -------------------------------------------------------------------------
	// SETTERS
	// -------------------------------------------------------------------------
	void Camera::setSize(int width, int height) {
		if(isStarted) {
			LOG_ERR("Camera: Cannot set size, since camera is already started.");
			return;
		}
		this->width = width;
		this->height = height;
	}
	
	void Camera::setFramerate(int fps) {
		if(isStarted) {
			LOG_ERR("Camera: Cannot set framerate, since camera is already started.");
			return;
		}
		this->fps = fps;
	}

	// -------------------------------------------------------------------------
	// INIT
	// -------------------------------------------------------------------------
	int Camera::init() {
		isInitialized = true;
		return FK_SUCCESS;
	}
	
	// -------------------------------------------------------------------------
	// START
	// -------------------------------------------------------------------------
	int Camera::start() {
		// check if we need to initialize first
		if(!isInitialized) {
			err = this->init();
			if(err != FK_SUCCESS) return err;
		}
		
		isStarted = true;
		
		return FK_SUCCESS;
	}
	
	// -------------------------------------------------------------------------
	// UPDATE
	// -------------------------------------------------------------------------
	int Camera::update() {
		return FK_SUCCESS;
	}
	
	// -------------------------------------------------------------------------
	// STOP
	// -------------------------------------------------------------------------
	int Camera::stop() {
		isStarted = false;
		return FK_SUCCESS;
	}
	
	// -------------------------------------------------------------------------
	// CLOSE
	// -------------------------------------------------------------------------
	int Camera::close() {
		if(isStarted) 
			this->stop();
			
		isInitialized = false;
		return FK_SUCCESS;
	}

	// -------------------------------------------------------------------------
	// GET IMAGE
	// -------------------------------------------------------------------------
	IplImage* Camera::getImage(int channel) {
		return NULL;
	}
}