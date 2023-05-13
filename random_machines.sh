#!/bin/bash

if [ ! -f config ]
then
    echo "File config not found!"
    exit 1
fi

NUM_MACHINES=10
NUM_SERVERS=7

NUMBERS=$(seq 1 $NUM_MACHINES | shuf | head -$NUM_SERVERS)
SERVERS=("ASSAULT_PARTY_A_MACHINE" "ASSAULT_PARTY_B_MACHINE" "COLLECTION_SITE_MACHINE" "CONCENTRATION_SITE_MACHINE" "MUSEUM_MACHINE" "MASTER_THIEF_MACHINE" "ORDINARY_THIEF_MACHINE")

replace() {
    sed -i "s/$2=.*/$2=$3/g" $1
}

echo -e "\nRandomly renaming machines in config file..."
COUNT=0
for i in $NUMBERS
do
    N=$(printf "%02d" $i)
    SERVER=${SERVERS[$((COUNT++))]}
    replace config $SERVER "l040101-ws$N.ua.pt"
    echo "$SERVER: l040101-ws$N.ua.pt"    
done
