#!/bin/bash

if [ ! -f config ]
then
    echo "File config not found!"
    exit 1
fi

NUM_SERVERS=7

NUMBERS=$(seq 1 $NUM_SERVERS | shuf | head -$NUM_SERVERS)
SERVERS=("ASSAULT_PARTY_A_MACHINE" "ASSAULT_PARTY_B_MACHINE" "COLLECTION_SITE_MACHINE" "CONCENTRATION_SITE_MACHINE" "MUSEUM_MACHINE" "MASTER_THIEF_MACHINE" "ORDINARY_THIEF_MACHINE")

replace() {
    sed -i "s/$2=.*/$2=$3/g" $1
}

echo -e "\nSetting all machines to localhost..."
COUNT=0
for i in $NUMBERS
do
    SERVER=${SERVERS[$((COUNT++))]}
    replace config $SERVER "localhost"
done
