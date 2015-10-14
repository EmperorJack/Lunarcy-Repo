# Lunarcy-Repo 

Lunarcy is a multiplay game project coded in Java.
The aim of the game is to compete against other players to escape the moon. Stranded with limited resources there is only one seat available on the escape ship. The 'rovers' or moon robots are malfunctioning and must be avoided. The first player to collect the items required to fix the ship and escape wins the game!

Team:
- Game Logic: Robbie Iversen
- Networking: Johnny Denford
- Data Storage & Map Builder: Kelly Moen
- User Interface: Ben Evans
- Renderer: Jack Purvis

Features:
- Networking with up to 5 players.
- Map builder to create unique map layouts.
- Software and hardware rendering.
- Ability to load and save a complete game state at any point in time.

Libraries:
- Processing and OpenGL for renderering.
- Saito OBJ loader for processing.
- gson for map and gamestate loading and saving.


# Instructions

Pre-Game setup, 
The jar does not run, use these steps to get it working.

1. Import the jar file into a new eclipse project

2. drag these folders into the src folder:
   bots
   game
   mapbuilder
   network
   storage
   testing
   ui
3. drag the remaining folders except for 'libs' and 'META-INF' into a new folder called 'assets' inside project directory.

Lunarcy

Pre-Game setup, 
The jar does not run, use these steps to get it working.

1. Import the jar file into a new eclipse project

2. drag these folders into the src folder:
   bots
   game
   mapbuilder
   network
   storage
   testing
   ui

NOTE: OpenGL library setup may be required.
Should be configured for the linux machines by default. To reconfigure for another OS do the following:
1. Ensure all jar libraries in the /libs folder are added to the build path.
2. Go to configure build path menu. Go to Libraries tab. Open the drop down for opengl.jar.
3. Ensure the 'Native library location' is set to /libs/~yourOSHere~ directory.

To run the game:

1. Run ServerMain
2. Choose required number of players (default update speed works well)
3. Click Start

4. Run ClientMain for the number of clients you specified in Server
5. Enter server address (localhost if you're running server on same machine)
5. Press start
6. Wait for all clients to connect, program 'pauses' until all clients are connected


SAVING and LOADING games and maps

To save a game:
  From Server, click 'Stop' and 'Yes' when save prompt appears.
To load:
  Press the load button, connect clients as before however the 
  names MUST match the previous ones
  Once all clients have connected the game will resume

Controls:

WASD  - move around
QE - rotate left/right
SPACE - Square info
CLICK/DRAG - for item interaction

Notes:

If you are outside for too long and run out of oxygen, you 'die'
If a bot catches you outside without armour, it 'kills' you.

When you die you lose an item and are randomly placed at a spawn point on the map

Keys/Doors are coloured to show which keys work with which doors, if you have
the correct key in your inventory then all the corresponding doors will be automatically
opened.

Objectives:

Find all the missing ship parts, and get to the ship before any other players do.

ENJOY
