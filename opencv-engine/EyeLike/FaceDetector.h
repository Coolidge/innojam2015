#pragma once
#include <opencv2/core/core.hpp>
#include <opencv2/objdetect/objdetect.hpp>



using namespace cv;
using namespace std;




class FaceDetector
{
public:
	FaceDetector(CascadeClassifier& face_cascade, CascadeClassifier& eyes_cascade, double scale = 1, bool try_flip = false);
	~FaceDetector();
	void detect_and_display(Mat& frame);
private:
	void FaceDetector::write_to_file(int number_of_faces);
	String face_cascade_name_;
	String eyes_cascade_name_;
	CascadeClassifier face_cascade_;
	CascadeClassifier eyes_cascade_;
	String window_name_;	
	int saved_faces_;
	String output_location_;
	double scale_;
	bool try_flip_;
};


