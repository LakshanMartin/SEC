# File Comparison Application

Software Engineering Concepts: Assignment 1

Semester 2, 2021

Author: Lakshan Martin

Submission: 16th September 2021



## Purpose

This application is a demonstration of a multithreaded system, utilising blocking queues and thread pools to analyse textual similarities between two valid files.

## How to compile

Compilation of this application requires the following:

- Gradle Build Tool --version 5.6.2 (https://gradle.org/)
- Java

From the project root directory, build the application using the following command:

```bash
./gradlew build
```

## Usage

Upon executing the ```./gradlew run``` command, the application will run and present the user with a graphical user interface (GUI). The GUI comprises of a ```Compare...``` button, of which can be selected to produce a popup window used for navigating to a directory containing files to compare. 

By default, the ```Choose directory``` window open to the ```/src/main/resources/``` directory. Any files to be compared can be inserted into this directory. Additionally, the provided bash script can be used to populate the resources directory with test data. See the relevant section below for further details. Alternatively, any directory within the computer system can be used to compare valid files. 

At any point during the comparison process, the user can select the ```Stop``` button to cease any further file comparisons. 

The results of the comparisons will be output to a CSV file within the ```/src/main/output/``` project directory.

```./gradlew clean``` can be used to remove all build files. 

### Valid file extensions

The application will recognise files within the given directory of the following extensions:

-  .txt
- .md
- .java
- .cs

Any other file extension types will be ignored, and not utilised by the application.

Additionally, valid files that are empty, will also not be utilised. 

### Data Generation Script

A basic ```bash``` script has been included to generate data files for testing purposes. The script can be found within the ```/src/main/resources/``` project directory. 

#### Script Usage

The script can be run with the following command ```./dataGen.sh``` along with the following arguments:

- ```bash
  -genAll [Number of valid file sets] [Number of characters] 
  # To generate full data sets
  ```

- ```bash
  -genRand [Number of valid file sets] [Number of characters] 
  # To generate sets of random data files
  ```

- ```bash
  -gen100 # To generate a set of files with 100% similarity
  ```

- ```bash
  -gen66 # To generate two files with 66.7% similarity
  ```

- ```bash
  -gen50 # To generate two files with 50% similarity
  ```

- ```bash
  -gen0 # To generate a set of files with 0% similarity
  ```

- ```bash
  -genEmpty # To generate a set of empty files
  ```

- ```bash
  -genInvalid # To generate a set of invalid files
  ```

- ```bash
  -clean # To delete any valid files types within script directory
  ```

Not entering arguments will output a list of instructions, similar to the above dot points.

**WARNING:** Run the script and generate files in the ```src/main/resources/``` directory to avoid unnecessary clutter and accidental removal of project files of the same valid file extensions.

