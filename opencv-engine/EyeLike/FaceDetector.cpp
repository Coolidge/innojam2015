#include "FaceDetector.h"
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/contrib/contrib.hpp>
#include <fstream>

FaceDetector::FaceDetector(CascadeClassifier& face_cascade, CascadeClassifier& eyes_cascade, double scale, bool try_flip)
{
	face_cascade_ = face_cascade;
	eyes_cascade_ = eyes_cascade;
	window_name_ = "Capture - Face detection";
	saved_faces_ = 0;
	output_location_ = "data.txt";
	scale_ = scale;
	try_flip_ = try_flip_;
}

/** @function detect_and_display */
void FaceDetector::detect_and_display(Mat& frame)
{
	vector<Rect> faces, fliped_faces;
	Mat frame_gray, small_frame(cvRound(frame.rows/scale_), cvRound(frame.cols/scale_), CV_8UC1);

	cvtColor(frame, frame_gray, COLOR_BGR2GRAY);
	resize(frame_gray, small_frame, small_frame.size(), 0, 0, INTER_LINEAR);
	equalizeHist(small_frame, small_frame);

	//-- Detect faces
	face_cascade_.detectMultiScale(small_frame, faces, 1.1, 2, 0 | CASCADE_SCALE_IMAGE, Size(30, 30));
	
	if (try_flip_)
	{
		flip(small_frame, small_frame, 1);
		face_cascade_.detectMultiScale(small_frame, fliped_faces, 1.1, 2, 0 | CASCADE_SCALE_IMAGE, Size(30, 30));
		for (vector<Rect>::const_iterator fliped_face = fliped_faces.begin(); fliped_face != fliped_faces.end(); ++fliped_face)
		{
			faces.push_back(Rect(small_frame.cols - fliped_face->x - fliped_face->width, fliped_face->y, fliped_face->width, fliped_face->height));
		}
	}

	for (vector<Rect>::const_iterator face = faces.begin(); face != faces.end(); ++face)
	{
		Mat small_frame_ROI;
		vector<Rect> eyes;
		Point center;
		Scalar color(255, 0, 255);
		int radius;

		auto aspect_ratio = static_cast<double>(face->width) / face->height;
		if (0.75 < aspect_ratio && aspect_ratio < 1.3)
		{
			center.x = cvRound((face->x + face->width*0.5)*scale_);
			center.y = cvRound((face->y + face->height*0.5)*scale_);
			radius = cvRound((face->width + face->height)*0.25*scale_);
			circle(frame, center, radius, color, 3, 8, 0);
		} 
		else
		{
			rectangle(frame, cvPoint(cvRound(face->x*scale_), cvRound(face->y*scale_)),
				cvPoint(cvRound((face->x + face->width - 1)*scale_), cvRound((face->y + face->height - 1)*scale_)),
				color, 3, 8, 0);			
		}
		small_frame_ROI = small_frame(*face);		

		//-- In each face, detect eyes
		eyes_cascade_.detectMultiScale(small_frame_ROI, eyes, 1.1, 2, 0 | CASCADE_SCALE_IMAGE, Size(30, 30));

		for (vector<Rect>::const_iterator face_center = eyes.begin(); face_center != eyes.end(); ++face_center)
		{
			center.x = cvRound((face->x + face_center->x + face_center->width*0.5)*scale_);
			center.y = cvRound((face->y + face_center->y + face_center->height*0.5)*scale_);
			radius = cvRound((face_center->width + face_center->height)*0.25*scale_);
			circle(frame, center, radius, color, 3, 8, 0);
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
	setWindowProperty(window_name_, CV_WND_PROP_ASPECTRATIO, 1);
}

void FaceDetector::write_to_file(int numberOfFaces)
{
	ofstream output_;
	output_.open(output_location_, ofstream::app);
	output_ << to_string(numberOfFaces) + "\n";
	output_.close();
}

FaceDetector::~FaceDetector()
{}