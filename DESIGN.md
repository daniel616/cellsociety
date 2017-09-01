#DESIGN.md
##Cell Society Team 10
###Design goals:
The primary design goals of this program is to create an easily extendable (adding new simulations, adding new user inputs, etc) and intuitive-to-use application for users to run multiple types of simulations. The program should be flexible enough to add additional functionality to the program, but also closed enough to modification in certain areas that the programmer wishing to extend the code is only required to alter specific parts of the program's code. As such, this program is most flexible in its implementation of the simulations that the program can run, and it is easily extendable and highly flexible in that regard. Classes should communicate to each other on a need-to-know basis (communicated through public access methods) while the implementation details are encapsulated by private helper methods. These goals should be achieved through the OOP concepts of data abstraction, encapsulation, and polymorphism. Another critical goal was to make it robust and tolerant of faulty data by handling exceptions adequately. 

###Adding features:
####Adding new Simulations
UI: Main contains an array of Simulation ids that allows a function that loops through the array and create buttons for each element of the array to set on the screen just the right number of buttons with the right display text, which in turn launch the right simulation by passing the value (simulation id) to InitiateCS class. 
Create new subclass of Simulation abstract parent class and implement the abstract methods. 
Create new subclass of Cell abstract parent class and implement the abstract methods as necessary by the particular simulation rules
Potentially create a subclass of BaseFrontEndGrid to implement more complicated drawing functions
Create new XML files to initialize simulation from file

####Adding new Parameters to simulations:
Add entries in XML files corresponding to parameter names:
Add new sliders in GUIForUser to adjust parameters, call the initiateParameterSlider function to add it to the GUI, passing itself in the change listener
Add setters in the corresponding Simulation class’ updateParameter block

####Adding new lattice structures:
Modify FrontEndGrid to add another helper method with specific implementation details on the update/initialize methods for that lattice structure
Add it to GUI as a user option within the combobox, to do this, all that must be done is adding the String option to the array

###Major design choices:

One important design choice that we made during the project was to use a 2D array instead of a hashmap of points to store cell positions within the BackEndGrid. This was convenient in some ways because it imposed restrictions upon cell positions- you could only store cells in integer positions within fixed bounds, so this made visualizing arrays of square cells easier. It also makes certain operations such as swapping cells easier. On the other hand, 2D arrays are not well suited to expansion, which made it more difficult to implement an expanding grid shape. It is also not possible to tessellate triangles or hexagons in a grid, so implementing triangular tessellations with our grid type required some geometric rearrangements, so that the positions of cells in the array did not correspond directly to positions on the canvas. Also, there was a bit of redundancy in the XML parser passing a Hashmap of cell location to celltype, which had to be transcribed to a 2-D array to be compatible with back end simulation functionalities. It would've been better if we consistently stuck to either one, instead of having to convert between the two (XML converting directly to 2-D array or scrapping original back end functionalities and consistently using hashmaps). Because of this, Simulation classes also had to update both grid and cell hashmap (which is needed for StateSaver), which is redundant and poor design. 

Communication between front end display and back end Simulation classes happens through a 2-D back end grid that each Simulation (upon launch) creates an instance of, and front end grid that continually updates the 2-D grid at each step based on back end updates to the grid and cells contianed, and returns a node that UI can call to display. Front end display also gets passed from UI the user preferences for stylizing the grid (lattice structure, etc)

The BackEndGrid implemented getNeighbors functions for cells for hexagonal and triangular lattice through the use of subclasses. Subclasses of BackEndGrid, such as triangleNeighborGrid, override getNeighbors functions in different ways depending on whether the Grid utilized hexagonal, triangular or square configurations. This was done so that programmers could use a single overrideable getNeighbors function for the grid. However, this complicated the code somewhat, and caused inconvenience by forcing peers to choose the subclass of grid to implement depending on the grid type.

We decided not to allow Cells to hold references to the BackEndGrids that were storing them. This meant that Cell functionality was limited largely to storing data specific to each Cell, and that Cells could have relatively limited interactions with their peer cells. This made Cell classes very small and Simulation classes somewhat larger. However, this improved the modularity of our code, and made our code less interdependent.

Between UI and Back end simulation, information is passed primarily through a Hashmap of Parameters and a HashMap of cells and their locations. The HashMaps are passed into the constructors of the Simulation classes. Sliders on the UI side dynamically update/change these parameters, which it passes back to Simulation public setters for update. This was a design choice made based on the reasoning that we didn’t want to pass large data structures across classes, and we wanted a data structure general enough that parameters of all simulations could be represented this way. Although it is somewhat limiting in that it can only store Parameters with values that can be represented by doubles. While some conversion were simple (eg. int to double), others wouldn’t be as much (eg. Strings)
	

###Assumptions:
Assumes that parameter tags are known by all classes that have the capability of modifying the parameter. To modify parameter values, you need the parameter tag to modify the value in the parameter map. 

Assumes that all classes that launch the different simulation have the correct ids 
XML parser assumes a specific format of the data (eg. cells stored as “cellType x-coordinate y-coordinate”) and StateSaver saves state to xml based on this format
Generalized all the simulation parameters to be stored as doubles so that we could keep all parameters in a single data structure across simulations, and so that they can all be accessed in the same way.

Assumes that parameters such as lattice type and state of cells are of one of the options we wrote implementations before. Since we use button implementations to select lattice type and Simulation classes pass strings representing states, we thought they would be appropriate assumptions for simplicity. However, we could throw an exception when the values given are not one of the options we accounted for. 

Assumes that parameters such as gridSize are valid as we specify no maximum or minimum number. To resolve this ambiguity, we could’ve set maxes/mins and used try/catch to adjust these inappropriate values. 
