
#Cell Society Design Document
####Team 10:
- Moses Wayne - msw38
- Daniel Li - drl21
- Sarah Zhou - sz107
##Specification

###Introduction:
The Cell Society program's primary goal in its function is to provide a medium for the simulation of various situations that involve the interactions between independent or group actors (cells) that behave according to a set of rules or specifications. The original simulations that are put into the program are segregation, predator-prey, fire, and Conway's game of life, but the design of the program will be such that any simulation that involves the interaction between individuals or groups of cells is capable of being run in this Cell Society program.

The primary design goals of this program is to create an easily extendable (adding new simulations, adding new user inputs, etc) and intuitive-to-use application for users to run multiple types of simulations. The program should be flexible enough to add additional functionality to the program, but also closed enough to modification in certain areas that the programmer wishing to extend the code is only required to alter specific parts of the program's code. As such, this program is most flexible in its implementation of the simulations that the program can run, and it is easily extendable and highly flexible in that regard.

The cell abstract class would be an example of the Open Close principle as each subclass of cell (Predator, prey, etc) would implement their own versions of functions such as interactWith(Prey) and interactWith(Predator). This is also the case with the Simulation class and its subclasses (Fire, Predator-Prey)  as they would have their own step() method implementations. 

###Overview

The program is started by running the Main class, which serves the sole functionality of adding an Application to the scenario. Upon initialization, the Application adds a SplashScreen to the scenario, which allows the user to navigate between the various different kinds of CA simulations that our program provides. Once the user selects a simulation, new subclasses of the abstract classes Simulation and userInterface are added to the scenario. 

The Simulation class contains a reference to a Grid, which in turn contains a reference to a 2D array of Cell objects. The Grid class contains the method displayGrid, which draws the Cells of the Grid in their current position in each step of the simulation. The Simulation also contains a reference to the FileReader class, which is responsible for reading XML files and setting up the initial states of a simulation. The abstract Simulation class contains the method updateGrid. updateGrid is overriden by each subclass of the Simulation, and is responsible for creating a new 2D array of Cells during each step of the animation which corresponds to how the grid should change during a single step. In each step, the Simulation performs updateGrid and then calls the Grid to displayGrid. If the equals() method for the Grid shows that the updated Grid and the previous grid are identical, the scenario pauses and a completion message is displayed.

The Simulation also holds a reference to a subclass of the userInterface that is specific to which kind of simulation is currently being run. In each different simulation, the userInterface serves as a means for the player to adjust the settings of the simulation. Some options, such as adjustment of animation speed, grid size, and reset buttons, will be universally available, and hence these functionalities will be implemented within the abstract superclass. Other options such as the selection of simulation start conditions and probabilities of various random phenomenon occurring will be different between simulations, and hence these options will be implemented within the various subclasses. The userInterface will also provide an option to call the Application to reset to the splash screen. User inputs will be collected through the Simulation’s respondInput() method, which observes changes in the userInterface and adjusts the Simulation’s variables accordingly.

###User Interface
The user interface for this program is a very simple two stage design, starting with a selection screen to choose between which simulation the user would like to run. The second screen offers settings for the current simulation chosen, such as speed of the simulation, as well as buttons to start, stop, and step through the simulation. The simulation will be displayed in the window adjacent to the user interface, displaying updated information about cell location and the visualization of the grid of cells. The method of interaction between the user and the program would be dictated by moving sliders for various settings and buttons for functions such as the selection of type of simulation and the functions of starting, stopping, and stepping. An example of the user interface is attached to this markdown file in the folder.

The interface will also return error messages for erroneous situations such as errors in the input file. An additional erroneous situation that will be flagged by the program is the input of an empty data set that leads to a null pointer exception. 

###Design Details
####Cell:
Contains information on what its individual data and function are, depending on the type of cell that it is. This class is used by the Grid class to determine the layout of the world of cells in the Grid.
Subclasses: Fire, Predator, Prey to differentiate interaction between cells
Can never have just a “cell” so it is abstract

Cell can be extended in the future by extending the class with new cells that contain different information and achieve different functionality. 
Justification: By implementing a design that can function with multiple different kinds of cells with various functionality, the design of the overall application becomes more flexible and extendible. Cell acts largely as a data structure to be read by the simulation and grid, allowing some of the work that simulation and grid would have done to be achieved in another class.

####Grid
Contains information about a two dimensional array of cells that are displayed on the main window of the application's user interface. The class should be able to instantiate grids of various sizes and be able to move cells around based on the specifications of the simulation that is running. The Grid class will contain functions to push display information to the scene (displayGrid()) and functions to alter cells based on the conditions of the simulation (swapCell()).

Grid's extendability lies in the flexibility of the class. Because the class relies on several flexible class' polymorphism, the class should be robust enough to handle various different situations that extend upon Cell Society's current functionality.
Justification: The design goal of this program is to limit the effort of the user in what is required action in extending the program's functionality, and in keeping the class flexible to input, Grid follows the goals of the program design.

####Simulation:
Simulation contains functionality to implement the various interactions between the Cells in the current Grid. The class is abstract as to allow for differing interaction between cells according to different rules in the simulation. The class' primary functionality extended to all subclasses is the use of updateGrid(Grid grid) to update the grid and its content to progress the simulation. It is also found in the equals() method which allows the simulation to check if the current simulation has reached a stasis. Because different simulations will often require different types of input for settings, the different simulations take the base class of the user interface and add additional setting through respondInput(userInterface u), a method that adjusts the variables of the simulation in response to the user’s inputs in the userInterface.
The simulation also interacts with its collaborating classes by instantiating both Grid and Cells through parameters specific to the type of simulation and calls basic functionality of these classes to remove dependencies in order of method calls.
Subclasses: Segregation, Predator Prey, Fire, Game of Life

The extendability of the entire CellSociety program lies in the abstractness of the Simulation class. Because the class is abstract and uses abstract methods for the simulation's different functionalities, the CellSociety program can take advantage of polymorphism to function under new and different kinds of simulations, even if they were added to CellSociety after it is coded.
Justification: The CellSociety program requires a class to dictate the interactions between different cells under different conditions for interaction. By providing an abstract class to achieve this, Simulation falls in line with the overarching goals of the design of the program in maintaining flexibility of the program to different kinds of simulations and limiting the number of areas in the code that must be updated to add additional functionality to the program.

####Application
The Application class of the CellSociety program allows for the organization of basic user interface of each of the simulations, upon which the simulations add additional functionality. Its primary purpose is to organize information between the classes and the display of this information and maintaining the options of the user interface to the simulation. Simulation interacts with most classes in that it instantiate most of the instances of the classes in the program, and it also interacts with the scene of the program.

The extendability of the Application class is limited, which is why the classes that it instantiates are so flexible. In maintaining the flexibility of the classes instantiated by Application, the design of the program ensures that Application itself is flexible to the addition of functionality to the program.
Justification: Application falls in line with the design goals and principles of the program in that it limits the amount of code that a programmer wishing to extend the program must alter. Since it instantiates most of the classes used by the program, Application only needs to be altered in its containment of information about the classes that it is instantiating. This simplifies the extension of this program, easily fulfilling the goals of the design.

####File Reader:
FileReader is a simple class that allows the initialization of the Grid and Cells by the reading in of information from data files provided by the user. The functionality of this class can be used by any other class that requires information to be read in from data files supplied by the user. The class will have methods such as parseFile(File f) which will parse files and produce information in a data structure that can be easily read by the class that the data is passed to.

The FileReader should be able to be extended fairly easily to added functionality such as handling different types of files simply because of how basic its implementation is. By abstracting away all of the input handling of the program to one class, CellSociety ensures that the program can be easily extended by only worrying about input in one location.
Justification: Not only does the FileReader class limit repetition of code throughout the program, it also allows for the alteration of the program in only one location. Both of these functions add to the goals and principles of the design of this program. Be ensuring both of these things, the program maintains flexibility and ease of extension while staying "clean".

####userInterface for User Options
The userInterface of CellSociety will interact primarily with the Simulation and Application classes, providing parameters for both that influence the function of the program. There are multiple levels of the user interface, and it allows for the switching between simulation selection and the splash screen as well as the initialization of simulations in the Application class. It installs handlers to perform action based on user input: play stop, pause, speed up / slow down, set initial parameters. The user interface has basic functionality for all of the subclasses of Simulation, but individual subclasses of Simulation may build upon the UI options presented in the userInterface class.

userInterface is especially extendable in that it is built upon by the Simulations that it helps Application call. Because it provides core functionality for user input, it limits the tasks of the simulations it runs.
Justification: Without the addition of a user interface class, the classes of simulation would each have to implement an endogenous user interface, a design structure that would not only violate a principle of non-repetition, but also make interpretation of the user interface incredibly difficult if it were not consistent across all of the classes. In maintaining this userInterface class, the UI of the program becomes a lot more simplified.

####SplashScreen
The splash screen functions as the main menu screen that demonstrates the information of the program to the user. This class does not have much interaction with the other classes, but it provides important information for the function of the rest of the program by the user. 

Because its function is fairly limited, the SplashScreen class does not need to be especially extendable. However, its simplicity also leads to an ease of extendability since its function remains fairly malleable. 
Justification: A screen that can easily display information from the README will greatly improve ease of use by the user and allows for the reduction of a lot of confusion. On the design front, the splash screen does not do much to make the code cleaner or more extendable, but it does make the program more useable. 


####Main
Main functions to launch Application. This is done to maintain a semblance of cleanliness between classes and delineate a clear cut start position to the program.

####Note:
To see the steps needed to complete the Use Cases of the program, read below at the section heading "Use Cases"
	
###Design Considerations
####Some possible issues:
 - Cluttered code when designing UIs for different simulations
	 - This was resolved by using one base class for the implementation of the user interface for each of the simulations, and if a given simulation requires additional settings and alterations in its user interface, this is rectified by the addition of an abstract instantiation method that adds components of the UI to the screen based on the simulation.
	 - This solution to the UI problem was debated thoroughly in the group, with the cons of the implementation being that some of the UI tasks are delegated to the simulations that the UI is supposed to be responding to. However, the pro of extendability given this architecture outweighed the cons. Even if the program in its current state were to be able to modify the necessary settings enough to accommodate the four base simulations, it will not necessarily be flexible enough to adapt to future simulation cases that may be added to the function. Ultimately, extendability was emphasized.
 - The delegation of responsibilities between Cell, 2-D grid, and Simulation 
	 - This was ultimately resolved by splitting the responsibility for the upkeep of cell interaction between the Simulation and Grid classes and maintaining the Cell class as primarily a class that holds a data structure and additional information that dictates behavior. 
	 - This was seen as optimal over the cells themselves having functions between each other because it obviated convoluted code that required the Cell class taking itself as a parameter when determining action. The cell class would also have to reference the data structure that it was held within, something that would not only be difficult to implement, but also potentially prone to problems. By maintaining the interaction between cells at a high level, the modularity add extendability of the program is also preserved, something that is a goal of the overarching design.
 - One of the classes doing too many things (at the moment it seems like that will be the Simulation classes)
	 - Delegating responsibility between the Simulation and Grid classes should limit the number of methods that the Simulation class must do. 
	 - Additionally, ensuring that the extensions to the abstract class only perform their specific abstract functions can make the modularity of the program more apparent and preserve a more compact function of the overarching abstract Simulation class. 

####Components:
 - Cell: purpose as an abstract class is to set a contract for its subclasses to implement their own particular functionalities (eat(), die(), etc.
 - Grid: to perform basic grid operations, hold cells, set cells in correct positions, keep track of grid state
 - Simulation (Abstract): To instantiate, update, and display grid, and calculate progression/status of simulation
 - UI for User Options: installs handlers to perform action based on user input, instruct whether to play stop, pause, and speed up / slow down simulation, set initial parameters to pass into simulation
 - Application: sets simulation and UI on screen, while (simulation has not ended) keep playing simulation
 - File Reader: Reads in file info, parses it, and passes info to other objects
 - Main: A static class for launching the application


###Team Responsibilities

We want to plan all the methods and classes together (write comments for purpose, and functionality) and assign actual implementation to individuals based on projected number of lines of code. The general format is: we will write all abstract classes together alongside Main and Application, and then write our assigned subclasses, and regroup to see if we need to make any changes to abstract classes. 

Sarah: Fire, Game of Life, XML Parser
Daniel: Wator Predator-Prey
Moses: userInterface, segregation


##Use Cases:
 - Apply rules to a middle cell: The Game of Life Simulation’s step() method will call checkNeighborCount(Cell cell) for each of the cells that are still alive and return number of neighbors for that cell and then updateState(Cell cell) to remove the old Alive cell object if necessary and reinitialize it with a new Dead cell object.
 - Apply rules to an edge cell: Same process as a middle cell but add a check to see if it’s neighbor cell doesn’t exist (i or j coordinate is off the grid)
 - Move to the next generation: Call step() on the simulation, an abstract method that must be overwritten by each subclass of Simulation class that specifies the rules to apply on the Grid object (array manipulations) that contains the cells. Then call show(), which will graphically display the updated simulation state to the user. 
 - Set a simulation parameter: The following steps only apply if the parameter is different from the initial settings specified in the XML file. For the example given, the Simulation abstract class would have an abstract method setParameters that would be overwritten by each of its subclasses. eg) for the Fire simulation, you would call FireSimulation.setParameters(0.4) which would set probCatch to 0.4.
 - Switch simulations: The GUI will have buttons that the user can click on to switch simulations. When the user clicks a particular button, the EventHandler associated with that button will pass the String constant representing that simulation to a method in the Application class switchSimulation(STRING_CONSTANT) that will delete the old simulation from the pane, instantiate the new simulation with the right STRING_CONSTANT parameter, and play it.




