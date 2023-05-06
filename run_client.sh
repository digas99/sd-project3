#!/bin/bash

MAIN=client.main.Client$1

BINPATH=/test/Assault/dir$1
GENCLASS=/lib/genclass.jar
CLASSPATH=$PWD$BINPATH:$PWD$GENCLASS

java -cp $CLASSPATH $MAIN