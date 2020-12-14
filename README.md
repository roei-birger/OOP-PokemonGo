# POKEMON GO!

***Authors:** Roei Birger & Yaara Barak*

## Description:
In this project we used the infrastructure of an directed weighted graph.<br />

We have developed a game where a group of agents have to perform Pokemon eating by going along the graph depending on the side direction.<br />
Each edge weight, for each agent plan a movement so that in the allotted time the agent will eat as many Pokemon as possible and thus achieve as high a score as possible.<br />


There are 0-23 stages, each with a different amount of agents, Pokemon and allotted time.<br />
Every Pokemon we eat creates a new Pokemon in a random location.<br />

In this project we received data from a server (jar file) on which the game is performed.<br />
Obtaining the information was based on strings represented as JSON.<br />

### The implementetion of the game have to parts:<br />
-  Creating the data structurs and algorithms of the graph.
-  Use the graph data structure and algorithms to implement the "POKEMON GO!" game. <br />

 <br /> 
 
# Graphs 
#### directional weighted graph


## Data Structures:

**Node:**<br />
*The node_data interface is implemented in NodeData class:*<br />
This class represents the set of operations applicable on a  node (vertex) in a (directional) weighted graph.<br />
Each node has a unique id number and location in the graph. <br />

**Edge:**<br />
*The edge_data interface is implemented in EdgeNode class:*<br />
This class represents the set of operations applicable on a directional edge with source and destination nodes in a (directional) weighted graph.<br />
Each edge has a source and destenation nodes and weight.<br />
The edge weight represents the cost of arrival from the source vertex to the destination vertex.<br />
*There is no option to have a negative edge weight and an edge from a vertex to itself.*


**Graph:**<br />
*The directed_weighted_graph interface is implemented in DWGraph_DS:*<br />
 This class represents a directional weighted graph.<br />

 The nodes and edges are implemented in a data structure - HashMap.<br />
 There are functions for adding / removing nodes and edges, obtaining lists of nodes and edges, <br />
 obtaining the amount of nodes/edges that are in the diagram and obtaining the amount of actions done on the graph (saved as MC).
  
 **myWay:**<br />
  This class represents a support object for the "shortestPath" method,
  at the "shortestPathDist" method the graph is tested to find the shortest
  path between vertices. Each vertex that found in this way is preserved
  by this object in order to know which vertex was "his parent"
  on the way and what is the weight of the edge between them.
 
 
 ## Algorithms:
 
 *The dw_graph_algorithms interface is implemented in DWGraph_Algo class:*<br />
 The DWGraph_Algo object contains a graph to activate the algorithms on.
 
 This class represents the Graph Theory algorithms including:
 1. **copy**- Deep copy of the graph.<br />
 2. **init** <br />
 3. **save**- Saves the graph to a file in JSON format.<br />
 4. **load**- Load the graph from a given file in JSON format.<br />
 5. **shortestPathDist**- Calculates the shortest path distance between 2 given nodes. <br />
 6. **shortestPath** - Finds the shortest path (at what edges should we use the path) between 2 given nodes in the graph. <br />
 7. **isConnected** -Checks whether the graph is strongly connected.<br />
 
 <br /> 
 
# The game structures

**Elements:**<br />
* CL_Agent<br />
* CL_Pokemon<br />
* Arena<br />

**GameClient:**<br />
* Ex2 - this class is the "main" method running the whole project. In order to start playing the game you need to run this class.
 in addition the class includ functions that locat and moving the agents.<br />
* MyFrame - displaying of the game in JAVA (Using JFrame and JPanel).<br />


**Utils:**<br />
* Range - represents a simple 1D range of shape [min,max].
* Point3D - represents a 3D point in space.
* Point2D - represents a 2D Range, composed from two 1D Ranges.
* Range2Range - represents a simple world 2 frame conversion (both ways).

**Data:**<br />
In this package you will find the graph files and the elemnents images.<br />

**Lib:**<br />
Ex2_Server_v0.13.jar<br />
java-json.jar<br />
gson-2.8.6.jar<br />
annotations-13.0.jar<br />
kotlin-stdlib-1.3.72.jar<br />
kotlin-stdlib-common-1.3.70.jar<br />
okhttp-4.8.0.jar<br />
okio-2.7.0.jar<br />

## Example of stage 17 in the game:<br />


![An Example:](https://github.com/roei-birger/ex2/blob/master/game%20window.png)<br />

## Our Best Grades:<br />

![An Example:](https://github.com/roei-birger/ex2/blob/master/our%20grades.png)


## **Clone repository**

```
$ git clone https://github.com/roei-birger/ex2.git
```



