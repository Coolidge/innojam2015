# EyeLike
Attention Detection!

# OpenCV Engine
* Detects frontal faces and write the number to a .TXT file.
* Double click on EyeLike will run the openCV.

# Ramzor
* Reads from a .TXT file the number of faces.
* Turns on a LED light in the Ramzor
* Sends POST request to a dedicated service to persist  the current number of faces. (trial application).
* Double click on EyeLike will run the Ramzor application.

# WebServer
* Java application that with GET and POST APIs to.
* WebServer is deployed from the Eclipse to the HANA

* Each API updates\retrieves the number of faces.
* GET {{host}}/EyeLikeServlet/MainServlet
* POST {{host}}/EyeLikeServlet/MainServlet?value=100

# Analyse the results
* https://eyelikeanalyzer-i305845trial.dispatcher.hanatrial.ondemand.com/oskar.html
* Git repository https://git.hanatrial.ondemand.com/i305845trial/eyelikeanalyzer
* After the lecture we can analyse the results and tell at which point there was a peak of attention 
and when nobody was listening.
* We can save the pictures from the lecture and to investigate what the audience loved.