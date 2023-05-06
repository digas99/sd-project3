#!/bin/bash

cd ..

BINPATH=/out/production/sd-project2
GENCLASS=/lib/genclass.jar
CLASSPATH=$PWD$BINPATH:$PWD$GENCLASS

java -cp $CLASSPATH $1 $2 $3 $4