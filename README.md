# Android-Google-Maps-Custom-Pathfinder
Find and draw shortest path on Google Maps Custom Overlay with Dijkstra's algorithm

Short post about its development: https://yzahk.in/android-google-maps-custom-path-finder/

# Steps
* [1. How to use](#1-how-to-use)
* [2. Use your own image](#2-use-your-own-image)
* [3. Create your own nodes and edges](#3-create-your-own-nodes-and-edges)

# 1. How to use:
A fast way to set up the google maps part of it is to create a new Google Maps Activity. It will set up your project ot be compatible with Maps API.

Before you run your application, you need a Google Maps API key. you have to put the API key in the google_maps_api.xml file. read the comment there for more information. Without it you will not see the image overlay on the map.

After you have API key from Google and you put it in the google_maps_api.xml file you have a working Android Maps application. This contains a custom map I drawn in paint, with 26 nodes and 28 edges. Select a starting and ending note, experiment with the avoidable path function to see how the path changes so you get an idea about the workings of Dijkstra's algorithm.

# 2. Use your own image:
If you put in your own image you should use image that has width and height both sized to powers of two (it doesn't have to be a square: 1024x512 is good too). Put it in the drawable folder and modify the MapsActivity.java file accordingly.

In the MapsActivity.java file you should change the 

> private static final int overlayImageWidth = 1024;

> private static final int overlayImageHeight = 1024;

lines to represent your image's size and the

> bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.maps_f1);

line if you use another file name.

# 3. Create your own nodes and edges:
In the nodes.csv file you have to list all of the points that you might visit on the map.

Example line:
> 1,173,160,First Node,1,1

Explanation:
> Node ID,position X on the image, position Y on the image, Node Name, Level, Importance

* Node ID: An Unique identifier for the node.
* position X and Y: you have to write the X and Y pixel value on the image. I use paint for this as it shows this value on the bottom left corner. It is a good idea to use the same x values for nodes that are next to each other and same y values for the ones above each other.
* Node Name: do not use "," in it without escaping it
* Level: if you want a multiple level map (for example you do a interior map for a building with floors) you can specify which level this node is on the code is ready to avoid nodes that are not on the selectedLevel (you can try this by changing this value to other than one on some nodes)
* Importance: this is a value for a future feature, also maybe for using the A* algorithm that might be faster. Not used at this time.

In the edges.csv file you have to make connections between all the nodes. Loose nodes crashes the algorithm if you target them. Same is true for loose node networks. You need to connect everything in one network OR make start and endpoint selection so you can only select nodes in given networks (or rework the code so this is not a problem).

Example line:
> 1,1,2,100,First Edge

Explanation:
> Edge ID,From Node ID,To Node ID,Length,Edge Name

* Edge ID: An Unique identifier for the edge.
* From Node ID and To Node ID: specify the nodes that are the two ends of this edge
* Length: this specifies the length of the edge. You should be mindful about this so the algorithm give you really the shortest path
* Edge Name: do not use "," in it without escaping it

The menu_edges.csv, menu_from_nodes.csv and the menu_to_nodes.csv are list for the MainActivity spinners. As there is no point in making selectable all the nodes (lot of them are just there to make the path easier to navigate) you can list important nodes here. Same with edges. For example maybe you want the user to start from 3-4 different nodes (building has 3 entrances for example) but you want them to find 15 different places you list the respective nodes that way (Node IDs and names intact, order doesn't matter)

Set up working avoid edges functionality:
After you built your node-edge system (you have a graph) you should come up with a longest possible distance for the whole. For example you have 3 nodes, with two edges (basically imagine a line with 3 points) and those two edges have a length of 10 and 20, then your longest possible distance is 30. In the Edge.java file's 

> private final static int maxEdgeLength = 11111;

line you have to specify a number higher than the longest possible path. This is needed for the "avoid edges" functionality. If the avoided edge is smaller than the longest possible path the algorithm will still take that edge in some cases. We can't delete edges so we have to give them such a length that it doesn't worth putting in the path.

> P.S. I know I left an API key in the google xml file. I left it there as an example, it was turned off before I uploaded this.
