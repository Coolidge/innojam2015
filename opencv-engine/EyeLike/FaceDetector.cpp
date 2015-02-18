#include "FaceDetector.h"
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/contrib/contrib.hpp>
#include <fstream>

ofstream output_;

face_detector::face_detector(CascadeClassifier face_cascade, CascadeClassifier eyes_cascade)
{
	face_cascade_ = face_cascade;
	eyes_cascade_ = eyes_cascade;
	window_name_ = "Capture - Face detection";
	saved_faces_ = 0;
	output_location_ = "data.txt";
}

/** @function detect_and_display */
void face_detector::detect_and_display(Mat frame)
{
	vector<Rect> faces;
	Mat frame_gray;

	cvtColor(frame, frame_gray, COLOR_BGR2GRAY);
	equalizeHist(frame_gray, frame_gray);

	//-- Detect faces
	face_cascade_.detectMultiScale(frame_gray, faces, 1.1, 2, 0 | CASCADE_SCALE_IMAGE, Size(30, 30));

	for (size_t i = 0; i < faces.size(); i++)
	{
		Point center(faces[i].x + faces[i].width / 2, faces[i].y + faces[i].height / 2);
		ellipse(frame, center, Size(faces[i].width / 2, faces[i].height / 2), 0, 0, 360, Scalar(255, 0, 255), 4, 8, 0);

		auto faceROI = frame_gray(faces[i]);
		vector<Rect> eyes;

		//-- In each face, detect eyes
		eyes_cascade_.detectMultiScale(faceROI, eyes, 1.1, 2, 0 | CASCADE_SCALE_IMAGE, Size(30, 30));

		for (size_t j = 0; j < eyes.size(); j++)
		{
			Point eye_center(faces[i].x + eyes[j].x + eyes[j].width / 2, faces[i].y + eyes[j].y + eyes[j].height / 2);
			auto radius = cvRound((eyes[j].width + eyes[j].height)*0.25);
			circle(frame, eye_center, radius, Scalar(255, 0, 0), 4, 8, 0);
		}
	}
	//-- Show what you got	
	auto current_faces = faces.size();
	putText(frame, "Faces: " + to_string(current_faces), cvPoint(30, 30), CV_FONT_HERSHEY_COMPLEX_SMALL, 0.8, cvScalar(200, 200, 250));
	if (current_faces != saved_faces_)
	{
		saved_faces_ = current_faces;
		write_to_file(saved_faces_);
	}
	imshow(window_name_, frame);
}

void face_detector::write_to_file(int numberOfFaces)
{		
		output_.open(output_location_, ofstream::app);
		output_ << to_string(numberOfFaces) + "\n";
		output_.close();
}

face_detector::~face_detector()
{}