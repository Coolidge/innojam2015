# EyeLike
Attention Detection!

# OpenCV Engine
* Detect frontal faces and write the number to a txt file.

# Ramzon
* Reads from the txt file the number of faces.
* Open LED light in the ramzor
* Send POST request to save the number in the cloud. (trial application).

# WebServer
* Java application that has GET and POST APIs for the number of faces.

* GET {{host}}/EyeLikeServlet/MainServlet
* POST {{host}}/EyeLikeServlet/MainServlet?value=100

# Analyze the results
* After the lecture we can analyze the results and tell at which point there was a peak of attention 
and when nobody was listening.
* We can save the pictures from the lecture and to investigate what the audience loved.

# Run
* Double click on EyeLike will run the Ramzor application and opencv.
* WebServer is deployed from the Eclipse to the HANA