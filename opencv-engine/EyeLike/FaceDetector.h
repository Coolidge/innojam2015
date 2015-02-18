#pragma once
#include <opencv2/core/core.hpp>
#include <opencv2/objdetect/objdetect.hpp>



using namespace cv;
using namespace std;




class face_detector
{
public:
	face_detector(CascadeClassifier face_cascade, const CascadeClassifier eyes_cascade);
	~face_detector();
	void detect_and_display(Mat frame);
private:
	void face_detector::write_to_file(int number_of_faces);
	String face_cascade_name_;
	String eyes_cascade_name_;
	CascadeClassifier face_cascade_;
	CascadeClassifier eyes_cascade_;
	String window_name_;	
	int saved_faces_;
	String output_location_;
};


