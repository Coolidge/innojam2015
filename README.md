# EyeLike
Attention Detection!

# OpenCV Engine
* Detects frontal faces and write the number to a txt file.

# Ramzor
* Reads from a txt file the number of faces.
* Turns on a LED light in the ramzor
* Sends POST request to a dedicated service to persist  the current number of faces. (trial application).

# WebServer
* Java application that with GET and POST APIs to.
* Each API updates\retrives the number of faces.

* GET {{host}}/EyeLikeServlet/MainServlet
* POST {{host}}/EyeLikeServlet/MainServlet?value=100

# Analyze the results
* After the lecture we can analyze the results and tell at which point there was a peak of attention 
and when nobody was listening.
* We can save the pictures from the lecture and to investigate what the audience loved.

# Run
* Double click on EyeLike will run the Ramzor application and opencv.
* WebServer is deployed from the Eclipse to the HANA
