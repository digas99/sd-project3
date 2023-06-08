# sd-project3

Project 3 for the course of Sistemas Distribuídos (Distributed Systems) at Universidade de Aveiro.

## Heist to the Museum

### Build, Deploy and Run
To build the project, run the following command:
```bash
chmod +x buildAndGenerateGlobal.sh
./buildAndGenerateGlobal.sh
```

To deploy and run the project, run the following command:
```bash
chmod +x deployAndRunFirstTime.sh
./deployAndRunFirstTime.sh
```

### Dependencies
To deploy and remotely run the project, you need **xterm** and **sshpass**. To install them, run the following commands:
```bash
$ sudo apt-get install xterm
$ sudo apt-get install sshpass
```

### Contributors
* [Diogo Correia](https://github.com/digas99) (90327): diogo.correia99@ua.pt
* [Lara Rodrigues](https://github.com/Lararodrigues1) (93427): laravieirarodrigues@ua.pt

### Related Projects from this course
* [**sd-project1:** Pure concurrent implementation of the problem running in a single platform.](https://github.com/digas99/sd-project1)
* [**sd-project2:** Distributed implementation of the problem, based on message passing, running in multiple platforms.](https://github.com/digas99/sd-project2)
* **sd-project3:** Distributed implementation of the problem, based on method invocation on remote objects, running in multiple platforms.