# cellsociety 
## Team 10

####Names: 

Daniel, Moses, Sarah

####Date Started: 

Jan 21

####Date Finished: 

February 13

####Estimate number of hours: 

200+ hours

####Roles:
 - Daniel: PredatorPRey, Segregation, backend and frontend grid functionalities
 
 - Moses: All front end implementation (main screen, ui, file opener, simulation initializers, user controls, css formatting, graph state, etc) aside from the grid, wrote random cell generator function for back end implementation. 
  
 - Sarah: XML Parser, XML files, state saver, Fire, Game of Life, Ant Foraging, Cell, Simulation, and Grid functionalities/behaviors

####Books, papers, online, or human resources that you used in developing the project:

 - TutorialsPoint
 
 - StackExchange
 
 - JavaDocs
 
 - TAs: Kelly Cochran, Matthew Faw, Jacob Lettie
 
 - CSS resources: http://fxexperience.com/2011/12/styling-fx-buttons-with-css/, http://www.w3schools.com/colors/colors_picker.asp, http://www.cssfontstack.com/, http://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html, http://code.makery.ch/library/javafx-8-tutorial/part4/
 
 - Several UI methods are inspired greatly by Professor Duval's browser code
 

####Files used to start the project: 

Main

####Files used to test the project: 

none

####Data/Resource files required by the project: 

All files in data folder, and resources package in src folder

####Bugs/crashes/problems: 
 - Ant Forage Simulation runs but I was confused on how to exactly implement the algorithm. From the first step, the desired Pheromones level is 0 because the ants start in the nest, and none of its neighbors have any level of Pheromones. This means that the ants will walk randomly, and never drop any pheromones since its desired pheromone level (as calculated by the pheromone levels of its neighbor with the max pheromone levels ) is always 0.
 
 - Daniel tried to refactor the back end grid using generics so that we could declare what type back end grid stores, thus, eliminating casting problems. However, this was not successful so we scrapped it and left in the typecasting. 
 
 - Although there is error checking implemented into the program, catching the errors still causes the exceptions to be printed to the console.
 
 - The state saver can be finnicky at times.
 
####Impressions of the assignment to help improve it in the future:

 - Our abstract Simulation and Cell classes were created with only the first four simulations in mind. Those simulations were much more basic in implementation and there was a lot of functionality/behavior needed by the latter four simulations that we didn't account for in the original design. Had we considered these additonal functionalities, we could've designed the classes and avoided the code smell of needing to typecast for the more complex simulations.
 
 - On the UI side, one thing that could be improved on is spacing. Although the screen is dynamically sized to the grid, it is a hard-coded ratio to display the from end grid component on the application window.
 
 - UI allows for extensive addition of features given the modularity of both its methods and its classes.
 
 - The UI also did not implement two features in finished in the back end (axis titles for graph, and unbounded grid) 
 
 