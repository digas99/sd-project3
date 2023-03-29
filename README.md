# sd-project1

Project 1 for the course of Sistemas Distribuídos (Distributed Systems) at Universidade de Aveiro.

## Heist to the Museum

### Run the project with IntelliJ

1. Open the project with IntelliJ
2. Simply run the project
3. The logging file can be passed as an argument to the program

### Run the project with run.sh

1. Open a terminal in the project's root directory
2. Compile the project beforehand
3. Run the the bash script `./run.sh [...args]`

The **run.sh** script accepts the following arguments:
`run.sh [-h] [-r <repetitions>] [-p <path-binaries>] [-l <logging-file>]"` where:
* `-h` prints the help message
* `-r <repetitions>` sets the number of times the program will run
* `-p <path-binaries>` sets the path to the binaries from the compilation*
* `-l <logging-file>` sets the name of the logging file

*The path to the binaries is mandatory.