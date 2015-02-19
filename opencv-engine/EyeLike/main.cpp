#include <opencv2/highgui/highgui.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include "FaceDetector.h"

using namespace std;
using namespace cv;


/** @function main */
int main(void)
{
	VideoCapture capture;
	Mat frame;
	String face_cascade_name = "haarcascade_frontalface_alt_tree.xml";
	String eyes_cascade_name = "haarcascade_eye_tree_eyeglasses.xml";	
	CascadeClassifier face_cascade;
	CascadeClassifier eyes_cascade;

	//-- 1. Load the cascades
	if (!face_cascade.load(face_cascade_name))
	{
		printf("--(!)Error loading face cascade\n"); return -1;
	};
	if (!eyes_cascade.load(eyes_cascade_name))
	{
		printf("--(!)Error loading eyes cascade\n"); return -1;
	};
	
	FaceDetector face_detector(face_cascade, eyes_cascade, 1);	
	
	//-- 2. Read the video stream
	capture.open(0);
	if (!capture.isOpened()) { printf("--(!)Error opening video capture\n"); return -1; }

	while (capture.read(frame))
	{
		if (frame.empty())
		{
			printf(" --(!) No captured frame -- Break!");
			break;
		}

		//-- 3. Apply the classifier to the frame
		face_detector.detect_and_display(frame);

		auto c = waitKey(10);
		if (static_cast<char>(c) == 27) { break; } // escape
	}
	return 0;
}



