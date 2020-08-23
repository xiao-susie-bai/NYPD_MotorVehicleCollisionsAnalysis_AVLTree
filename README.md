# New York City Motor Vehicle Collisions Analysis based on NYPD open source data
## --using AVL Tree data structure

This project provides a command-line-based tool for extracting specified types of information about motor vehicle collisions in New York City according to the user inquiries and producing detail reports to display for specified zip codes and time frames. Mainly the AVL tree data structure is used in implementation for pinpointing the exact information in the data. This project is developed in Java.

The data used is from NYC Open Data website (https://data.cityofnewyork.us/Public-Safety/Motor-Vehicle-Collisions-Crashes/h9gi-nx95) provided by New York Police Department (NYPD) regarding all motor vehicle collisions that occur on streets on NYC so far since July 2012. (Some test datasets based on a smaller timeframe have been provided in the project folder.) 

In this project, the program prompts the user for the zip code first, and then the start date and end date, based on which he/she wants to make inquiries about collisions information. The program would display a report using the data from the input csv file (data provided by NYPD), which contains a summary of the collisions that occurred according to the user inquiry conditions, including the total numbers of fatalities and injuries as well as the breakdown of each for pedestrians, cyclists and motorists. The user can extract information as many times as he/she wants until he/she would like to quit the program by entering "quit" in the command line.

This project contains three classes in the three .java files -- **Collision** class (an object of this class represents a single event of collision (a row of observation from the input csv file)), **CollisionsData** class (an object of this class stores all of the collision records from the input data, in the structure of an AVL tree, and provides methods that generate the report), and **CollisionInfo** class (the Main class; mainly reading in the input file and interacting with the user with inputs and outputs). Certain assistant class like Date class is also included.

