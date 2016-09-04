# OpenLIPS
LIPS (Learning-based Indoor Positioning System) with OSM (Open Street Map)

This is a research project for indoor GPS. It is an extension of [LIPS](https://github.com/davidmascharka/LIPS), 
originally created by [David Mascharka](https://github.com/davidmascharka), 
where we are including OpenStreetMap data into the project. 

Currently, we are working on getting setup for using the OSM map. We are using Mapzen tile server, and other 
libraries to respond to user interaction.  In the future, we will start 
adding the machine learning algorithms for the Indoor Locationalization system, and we will also collect new data,
and have a database.

##Using source code with Android Studio
1. Download/Clone this project.
2. In Android Studio, do File -> New -> Import Project. Use the directory of the project.
3. Build the project.
4. Once you build this project, copy the files in the Walkabout Style Folder to "osmlips/build/intermediates/exploded-aar/com.mapzen.tangram/tangram/0.4.1/assets/" folder
