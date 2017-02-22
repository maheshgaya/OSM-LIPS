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
3. Create an xml file `app/src/main/res/values/google_maps_api_key.xml` for adding the Google Maps API key. Add the code below and replace `GOOGLE MAPS KEY` with your own key from the [Google Developer Console](https://console.developers.google.com/). The key requires Google Maps API for Android and Google Places API for Android

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
Copyright 2017 Mahesh Gaya

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
