[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.1422439.svg)](https://zenodo.org/record/1422439)

Track analysis plugin for Fiji
==============================
This Fiji plugin takes pre-determined track coordinates in the results table and calculates various track analyses.


Installation
------------
- The latest version of the plugin can be downloaded from the [Releases](https://github.com/SJCross/TrackAnalysis/releases) page.
- Place this .jar file into the /plugins directory of the your Fiji installation. 


Before running the plugin
-------------------------
The plugin uses track coordinates present in the Fiji results table to perform analyses.  As such, it's necessary to have track coordinates specified in an appropriate format before running the plugin.
- This results table should contain X, Y, Z, frame and track ID number for each point in the track.
- A good example of a compatible table is the "Spots in tracks statistics" output from [TrackMate](https://imagej.net/TrackMate).
- Optionally, the corresponding image can also be open for the purpose of displaying track ID numbers.


Starting the plugin
-------------------
- In Fiji, run the plugin from Plugins > Tracking > Track analysis > Load from results table
- You'll be prompted to select the window names for the results table containing the tracks and, optionally, the image series the tracks correspond to.
- Next, the column headings in the results table need to be matched to the input fields ("Track ID column", "X-position column", etc.).  Using the TrackMate "Spots in tracks statistics" table as an example the following would be set:
  - Track ID column -> TRACK_ID
  - X-position column -> POSITION_X
  - Y-position column -> POSITION_Y
  - Z-position column -> POSITION_Z
  - Frame number column -> FRAME
- At this point it's also necessary to specify any spatial calibrations that were applied when the track coordinates were created.


Using the plugin
----------------
- Once the plugin is up and running it's possible to select the type of analysis from the drop-down menu at the top.
- The default option, "Track summary", will generate a new results table containing various track analyses.
- Other modes will output the time-varying versions of each numerical analysis.
- Both the track summary and plots can be created for all tracks or a single track, specified by its ID number.


Note
----
This plugin is still in development and test coverage is currently incomplete.  Please keep an eye on results and add an [issue](https://github.com/SJCross/TrackAnalysis/issues) if any problems are encountered.


Acknowledgements
----------------
A list of bundled dependencies along with their respective licenses can be found [here](https://cdn.statically.io/gh/SJCross/TrackAnalysis/cd9fb994/target/site/dependencies.html).
