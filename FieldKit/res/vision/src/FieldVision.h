/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2009, field.io             **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created October 27, 2009 */

#ifndef FIELD_VISION_H
#define FIELD_VISION_H

#include "Vision.h"
#include "Camera.h"
//
// Defines a C function interface to the C++ vision and camera classes
//

#ifdef __cplusplus
extern "C" {
#endif
	
#define FK_VISION_DATA_SIZE				300

#define FK_VISION_DATA_BLOB				-1000
#define FK_VISION_DATA_BLOB_BOUNDS		-1001
#define FK_VISION_DATA_BLOB_CONTOURS	-1002
	
// namespace
using namespace field;
	
// global variables
struct VisionData {
	int size;
	int index;
	int isLittleEndian;
	int *buffer;
};

// functions
int fvCreate();
int fvDestroy();

int fvStart();
int fvStop();
int fvUpdate();
	
// setters
void fvSet(int property, float value);
int fvSetCamera(int name);
int fvSetSize(int width, int height);
int fvSetFramerate(int fps);

// getters
float fvGet(int property);
Vision* fvGetVision();
int* fvGetBlobData();
int fvGetBlobDataLength();

	
// helpers
int fvError(int err);
inline void fvPushData(int value);
	
#ifdef __cplusplus
}
#endif

#endif