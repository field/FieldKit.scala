/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

//#include "Vision.h"
//#include "OpenCVCamera.h"

#include "FieldVision.h"
#include "CVBlobDetector.h"

using namespace field;

//--------------------------------------------------------------------------------------------
const int SCREEN_WIDTH = 1920;
const int SCREEN_HEIGHT = 480;

//--------------------------------------------------------------------------------------------
int main(int argc, char* argv[])
{
	LOG("************************************************");
	LOG("TestVisionBasic");
	LOG("************************************************");
	LOG(" ");
	
	
	LOG("** init vision **");
	fvCreate();
//	fvSetCamera(CAMERA_OPENCV);
	fvSetCamera(CAMERA_PORT_VIDEO);
	fvSetSize(320, 240);
	fvSetFramerate(20);
	fvStart();
	
	LOG("** init blob detector **");
	CVBlobDetector* proc = (CVBlobDetector*) fvGetVision()->getProcessor();
	proc->setStageEnabled(true);
	//proc->setStageEnabled(false);
	
//	fvSet(PROC_BACKGROUND, 0.15f);
//	fvSet(PROC_THRESHOLD, 0.1f);
//	fvSet(PROC_DILATE, 0.15f);
//	fvSet(PROC_ERODE, 0.06f);
//	fvSet(PROC_CONTOUR_MIN, 0.001f);
//	fvSet(PROC_CONTOUR_MAX, 1.0f);
//	fvSet(PROC_CONTOUR_REDUCE, 0.5f);
//	fvSet(PROC_TRACK_RANGE, 0.5f);
////	 proc->setWarp(0, 0,
////	 320, 0,
////	 320, 239,
////	 0, 240);	
////	proc->setROI(320, 240, 320, 240);
	
	LOG("** init gui **");
	CvSize windowSize = cvSize(proc->getROI().width, proc->getROI().height);
	if(windowSize.height > 240) windowSize.height = 240;
	int tmpX = 0;
	int tmpY = 45;
	
	char *windows[CVBlobDetector::STAGE_MAX];
	for(int i=0; i<CVBlobDetector::STAGE_MAX; i++) {
		char *windowName = (char *) malloc(50);
		sprintf(windowName, "STAGE %i", i);
		windows[i] = windowName;
		
		cvNamedWindow(windowName, NULL); //CV_WINDOW_AUTOSIZE);
		cvResizeWindow(windowName, windowSize.width, windowSize.height);
		
		if(tmpX + windowSize.width > SCREEN_WIDTH) {
			tmpX = 0;
			tmpY += windowSize.height + 23;
		}
		
		cvMoveWindow(windowName, tmpX, tmpY);		
		tmpX += windowSize.width;
		
	}
	
	// start vision
	LOG("** starting ** ");	
	bool updateStages = true;
	while(true) {
		fvUpdate();
		
		if(updateStages) {
			for(int i=0; i<CVBlobDetector::STAGE_MAX; i++) {
				IplImage *image = proc->getImage(i);
				if(image!=NULL) {
					cvShowImage(windows[i], image);
				}
			}
		}
		
		// pause thread
        int key = cvWaitKey(10);
        if (key == 'q' || key == 'Q')
            break;
		
        if (key == 'u' || key == ' ')
			updateStages = !updateStages;
	}
	
	LOG("** stopping **");	
	for(int i=0; i<CVBlobDetector::STAGE_MAX; i++) {
		cvDestroyWindow(windows[i]);
	}
	
	fvDestroy();
	
	LOG("** finished **");
	return 0;
}