## Ticket Search ##
Ticket Search enables you to look for available tickets for your favorite football team's next home game.

### How to build ###
See section on [how to build](#build).

### 3rd party resources used in this project ###
* [Android Action Bar Style Generator](http://jgilfelt.github.io/android-actionbarstylegenerator) 
* [SDK Manager Gradle Plugin](https://github.com/JakeWharton/sdk-manager-plugin) 
* [ArcGIS JSON to GeoJSON converter](https://github.com/gavreh/arcgis-json-to-geojson) 
* [Maki icons for map symbology](https://www.mapbox.com/maki)

### 3rd party open source libs ###
* [Annotations library from Jetbrains](http://www.jetbrains.com/idea/webhelp/annotating-source-code.html)
* [Joda-Time - Java date and time API](http://www.joda.org/joda-time/)
* [Retrofit - Type-safe REST client for Android and Java from Square](http://square.github.io/retrofit/)
* [Guava: Google Core Libraries for Java 1.6+](https://code.google.com/p/guava-libraries/)
* [Gson - A Java library to convert JSON to Java objects and vice-versa](https://code.google.com/p/google-gson/)
* [Google Play Services](https://developer.android.com/google/play-services/index.html)

### Other Resources ###
[Stadium data converted to GeoJSON from a FeatureService](http://services1.arcgis.com/oASeSX1dVztKCgUc/arcgis/rest/services/NFL_Draft_Teams/FeatureServer/0?f=pjson)  
[View the stadiums map](https://gist.github.com/MarcBernstein/8933d0991e94160b5947) as a GeoJSON map on GitHub

<a name="build"></a>
#### Building the app ###
*Requirements:*  
* Java 7 is required
* All other Android SDK requirements (including the SDK itself) **should** be downloaded automatically by the SDK Manager gradle plugin if they are missing.  
  * If there's an error that looks like:
  ```
  Could not find com.android.support:support-v4:19.1.0.
     Required by:
         TicketSearch:TicketSearchApp:unspecified > com.google.android.gms:play-services:5.0.89
  ```
  you may need to install the `Android Support Repository` option uner Extras in the Android SDK Manager first. This can be found under your Android SDK's tools directory (~/.android-sdk/tools/android if the gradle build downloaded the Android SDK for you). Both `Google Repository` and `Android Support Repository` must be installed.

After cloning the repo, you can build from within Android Studio or on the command line.  

*Command line:*  
1. `git clone https://github.com/MarcBernstein/TicketSearch.git`  
2. `cd TicketSearch`  
3. `./gradlew build`  
The debug .apk will be located at `TicketSearchApp/build/outputs/apk/TicketSearchApp-debug.apk` 

*Android Studio:*  
1. Clone the repo locally, or from the 'Check out from Version Control' menu in the `Welcome to Android Studio` launch screen.  
2. If cloned manually, click 'Open Project' and navigate to the cloned repo location.   

#### Testing the app ####
First, connect a device to the machine where it's accessible via the adb command.

*Command line:*  
1. cd into the repo directory  
2. Run `./gradlew connectedAndroidTest`  
3. Test reports are available under `TicketSearchApp/build/outputs/reports/androidTests/connected/index.html`.  

*Android Studio:*  
1. Expand the TicketSearch folder so that the TicketSearchApp folder is visible.  
2. Right click on the TicketSearchApp folder, got to the Run menu item, then click the `All Tests` submenu item.  
3. If it's not already visible, open the `Run` tool window. You should see the tests running.  

### Screenshots ###
![](https://github.com/MarcBernstein/TicketSearch/blob/master/TicketSearchApp/img/TicketSearch01.png)
![](https://github.com/MarcBernstein/TicketSearch/blob/master/TicketSearchApp/img/TicketSearch01.png)
![](https://github.com/MarcBernstein/TicketSearch/blob/master/TicketSearchApp/img/TicketSearch01.png)
