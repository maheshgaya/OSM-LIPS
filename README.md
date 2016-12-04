# LIPS WITH MAPS
LIPS (Learning-based Indoor Positioning System) with Maps

This is a research project for indoor GPS. It is an extension of [LIPS](https://github.com/davidmascharka/LIPS), 
originally developed by [David Mascharka](https://github.com/davidmascharka), where we are including maps instead of a blank slate for showing the location of the user.

## Using source code with Android Studio
1. Clone this project.

  ```
  $ git clone https://github.com/maheshgaya/lips-with-maps.git
  ```
2. Import the project directory in Android Studio, do File -> New -> Import Project.
3. Create an xml file under res/values/ for adding the Google Maps API key. Add the code below and replace `GOOGLE MAPS KEY` with your own key from the [Google Developer Console](https://console.developers.google.com/). The key requires Google Maps API for Android and Google Places API for Android

  ```
  <?xml version="1.0" encoding="utf-8"?>
  <resources>
      <!-- enter your key -->
      <string name="google_maps_key">GOOGLE MAPS KEY</string>
  </resources>
  ```
4. Run the project on an emulator or on your Android phone. (API >= 16)


## Contributing to this project
1. Fork this repository.
2. Write your fix or feature in your own repository.
3. Once it is ready and tested, pull a request to merge your code. 
**Please make sure that you [rebase](https://github.com/edx/edx-platform/wiki/How-to-Rebase-a-Pull-Request) your repository before pulling a request.**

##License
Indoor Localization with Maps. Copyright (C) 2016 Drake University

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/.
