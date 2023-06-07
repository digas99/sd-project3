#!/bin/bash

MAIN=client.main.Client$1

BINPATH=/test/Assault/dir$1
GENCLASS=$2
CLASSPATH=$PWD$BINPATH:$PWD$GENCLASS

java -cp $CLASSPATH $MAIN