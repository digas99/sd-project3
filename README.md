# sd-project2

Project 2 for the course of Sistemas Distribuídos (Distributed Systems) at Universidade de Aveiro.

## Heist to the Museum

### Build the project
To build the project, run the following commands in the root directory of the project:
```bash
$ chmod +x build.sh
$ ./build.sh <local | global>
```
This file compiles, builds and deploys the project. The argument can be either `local` or `global`, depending on the type of deployment you want to do.

### Run the project
To run the project, run the following commands in the root directory of the project:
```bash
$ chmod +x run_global.sh
$ ./run_global.sh
```
or
```bash
$ chmod +x run_local.sh
$ ./run_local.sh
```

### Clean the project .class files
To clean the project, run the following commands in the root directory of the project:
```bash
$ chmod +x clean.sh
$ ./clean.sh
```

### Auxiliary scripts
In the root directory of the project, there are some auxiliary scripts that are used by the previous scripts and might not work properly (or achieve any desired goal) if run directly. These scripts are: `build_server.sh`, `build_client.sh`, `deploy_global.sh`, `deploy_local.sh`, `run_server.sh` and `run_client.sh`.

### Contributors
* [Diogo Correia](https://github.com/digas99) (90327): diogo.correia99@ua.pt
* [Lara Rodrigues](https://github.com/Lararodrigues1) (93427): laravieirarodrigues@ua.pt